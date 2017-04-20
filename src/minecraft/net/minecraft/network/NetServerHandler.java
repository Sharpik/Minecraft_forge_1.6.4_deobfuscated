package net.minecraft.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet0KeepAlive;
import net.minecraft.network.packet.Packet101CloseWindow;
import net.minecraft.network.packet.Packet102WindowClick;
import net.minecraft.network.packet.Packet103SetSlot;
import net.minecraft.network.packet.Packet106Transaction;
import net.minecraft.network.packet.Packet107CreativeSetSlot;
import net.minecraft.network.packet.Packet108EnchantItem;
import net.minecraft.network.packet.Packet10Flying;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet13PlayerLookMove;
import net.minecraft.network.packet.Packet14BlockDig;
import net.minecraft.network.packet.Packet15Place;
import net.minecraft.network.packet.Packet16BlockItemSwitch;
import net.minecraft.network.packet.Packet18Animation;
import net.minecraft.network.packet.Packet19EntityAction;
import net.minecraft.network.packet.Packet202PlayerAbilities;
import net.minecraft.network.packet.Packet203AutoComplete;
import net.minecraft.network.packet.Packet204ClientInfo;
import net.minecraft.network.packet.Packet205ClientCommand;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet255KickDisconnect;
import net.minecraft.network.packet.Packet27PlayerInput;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet53BlockChange;
import net.minecraft.network.packet.Packet7UseEntity;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.BanEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class NetServerHandler extends NetHandler
{
    /** The underlying network manager for this server handler. */
    public final INetworkManager netManager;

    /** Reference to the MinecraftServer object. */
    private final MinecraftServer mcServer;

    /** This is set to true whenever a player disconnects from the server. */
    public boolean connectionClosed;

    /** Reference to the EntityPlayerMP object. */
    public EntityPlayerMP playerEntity;

    /** incremented each tick */
    private int currentTicks;

    /**
     * player is kicked if they float for over 80 ticks without flying enabled
     */
    public int ticksForFloatKick;
    private boolean field_72584_h;
    private int keepAliveRandomID;
    private long keepAliveTimeSent;
    private static Random randomGenerator = new Random();
    private long ticksOfLastKeepAlive;
    private int chatSpamThresholdCount;
    private int creativeItemCreationSpamThresholdTally;

    /** The last known x position for this connection. */
    private double lastPosX;

    /** The last known y position for this connection. */
    private double lastPosY;

    /** The last known z position for this connection. */
    private double lastPosZ;

    /** is true when the player has moved since his last movement packet */
    private boolean hasMoved = true;
    private IntHashMap field_72586_s = new IntHashMap();

    public NetServerHandler(MinecraftServer par1MinecraftServer, INetworkManager par2INetworkManager, EntityPlayerMP par3EntityPlayerMP)
    {
        this.mcServer = par1MinecraftServer;
        this.netManager = par2INetworkManager;
        par2INetworkManager.setNetHandler(this);
        this.playerEntity = par3EntityPlayerMP;
        par3EntityPlayerMP.playerNetServerHandler = this;
    }

    /**
     * run once each game tick
     */
    public void networkTick()
    {
        this.field_72584_h = false;
        ++this.currentTicks;
        this.mcServer.theProfiler.startSection("packetflow");
        this.netManager.processReadPackets();
        this.mcServer.theProfiler.endStartSection("keepAlive");

        if ((long)this.currentTicks - this.ticksOfLastKeepAlive > 20L)
        {
            this.ticksOfLastKeepAlive = (long)this.currentTicks;
            this.keepAliveTimeSent = System.nanoTime() / 1000000L;
            this.keepAliveRandomID = randomGenerator.nextInt();
            this.sendPacketToPlayer(new Packet0KeepAlive(this.keepAliveRandomID));
        }

        if (this.chatSpamThresholdCount > 0)
        {
            --this.chatSpamThresholdCount;
        }

        if (this.creativeItemCreationSpamThresholdTally > 0)
        {
            --this.creativeItemCreationSpamThresholdTally;
        }

        this.mcServer.theProfiler.endStartSection("playerTick");
        this.mcServer.theProfiler.endSection();
    }

    public void kickPlayerFromServer(String par1Str)
    {
        if (!this.connectionClosed)
        {
            this.playerEntity.mountEntityAndWakeUp();
            this.sendPacketToPlayer(new Packet255KickDisconnect(par1Str));
            this.netManager.serverShutdown();
            this.mcServer.getConfigurationManager().sendChatMsg(ChatMessageComponent.createFromTranslationWithSubstitutions("multiplayer.player.left", new Object[] {this.playerEntity.getTranslatedEntityName()}).setColor(EnumChatFormatting.YELLOW));
            this.mcServer.getConfigurationManager().playerLoggedOut(this.playerEntity);
            this.connectionClosed = true;
        }
    }

    public void func_110774_a(Packet27PlayerInput par1Packet27PlayerInput)
    {
        this.playerEntity.setEntityActionState(par1Packet27PlayerInput.func_111010_d(), par1Packet27PlayerInput.func_111012_f(), par1Packet27PlayerInput.func_111013_g(), par1Packet27PlayerInput.func_111011_h());
    }

    public void handleFlying(Packet10Flying par1Packet10Flying)
    {
        WorldServer worldserver = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
        this.field_72584_h = true;

        if (!this.playerEntity.playerConqueredTheEnd)
        {
            double d0;

            if (!this.hasMoved)
            {
                d0 = par1Packet10Flying.yPosition - this.lastPosY;

                if (par1Packet10Flying.xPosition == this.lastPosX && d0 * d0 < 0.01D && par1Packet10Flying.zPosition == this.lastPosZ)
                {
                    this.hasMoved = true;
                }
            }

            if (this.hasMoved)
            {
                double d1;
                double d2;
                double d3;

                if (this.playerEntity.ridingEntity != null)
                {
                    float f = this.playerEntity.rotationYaw;
                    float f1 = this.playerEntity.rotationPitch;
                    this.playerEntity.ridingEntity.updateRiderPosition();
                    d1 = this.playerEntity.posX;
                    d2 = this.playerEntity.posY;
                    d3 = this.playerEntity.posZ;

                    if (par1Packet10Flying.rotating)
                    {
                        f = par1Packet10Flying.yaw;
                        f1 = par1Packet10Flying.pitch;
                    }

                    this.playerEntity.onGround = par1Packet10Flying.onGround;
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.ySize = 0.0F;
                    this.playerEntity.setPositionAndRotation(d1, d2, d3, f, f1);

                    if (this.playerEntity.ridingEntity != null)
                    {
                        this.playerEntity.ridingEntity.updateRiderPosition();
                    }

                    if (!this.hasMoved) //Fixes teleportation kick while riding entities
                    {
                        return;
                    }

                    this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);

                    if (this.hasMoved)
                    {
                        this.lastPosX = this.playerEntity.posX;
                        this.lastPosY = this.playerEntity.posY;
                        this.lastPosZ = this.playerEntity.posZ;
                    }

                    worldserver.updateEntity(this.playerEntity);
                    return;
                }

                if (this.playerEntity.isPlayerSleeping())
                {
                    this.playerEntity.onUpdateEntity();
                    this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    worldserver.updateEntity(this.playerEntity);
                    return;
                }

                d0 = this.playerEntity.posY;
                this.lastPosX = this.playerEntity.posX;
                this.lastPosY = this.playerEntity.posY;
                this.lastPosZ = this.playerEntity.posZ;
                d1 = this.playerEntity.posX;
                d2 = this.playerEntity.posY;
                d3 = this.playerEntity.posZ;
                float f2 = this.playerEntity.rotationYaw;
                float f3 = this.playerEntity.rotationPitch;

                if (par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999.0D && par1Packet10Flying.stance == -999.0D)
                {
                    par1Packet10Flying.moving = false;
                }

                double d4;

                if (par1Packet10Flying.moving)
                {
                    d1 = par1Packet10Flying.xPosition;
                    d2 = par1Packet10Flying.yPosition;
                    d3 = par1Packet10Flying.zPosition;
                    d4 = par1Packet10Flying.stance - par1Packet10Flying.yPosition;

                    if (!this.playerEntity.isPlayerSleeping() && (d4 > 1.65D || d4 < 0.1D))
                    {
                        this.kickPlayerFromServer("Illegal stance");
                        this.mcServer.getLogAgent().logWarning(this.playerEntity.getCommandSenderName() + " had an illegal stance: " + d4);
                        return;
                    }

                    if (Math.abs(par1Packet10Flying.xPosition) > 3.2E7D || Math.abs(par1Packet10Flying.zPosition) > 3.2E7D)
                    {
                        this.kickPlayerFromServer("Illegal position");
                        return;
                    }
                }

                if (par1Packet10Flying.rotating)
                {
                    f2 = par1Packet10Flying.yaw;
                    f3 = par1Packet10Flying.pitch;
                }

                this.playerEntity.onUpdateEntity();
                this.playerEntity.ySize = 0.0F;
                this.playerEntity.setPositionAndRotation(this.lastPosX, this.lastPosY, this.lastPosZ, f2, f3);

                if (!this.hasMoved)
                {
                    return;
                }

                d4 = d1 - this.playerEntity.posX;
                double d5 = d2 - this.playerEntity.posY;
                double d6 = d3 - this.playerEntity.posZ;
                //BUGFIX: min -> max, grabs the highest distance
                double d7 = Math.max(Math.abs(d4), Math.abs(this.playerEntity.motionX));
                double d8 = Math.max(Math.abs(d5), Math.abs(this.playerEntity.motionY));
                double d9 = Math.max(Math.abs(d6), Math.abs(this.playerEntity.motionZ));
                double d10 = d7 * d7 + d8 * d8 + d9 * d9;

                if (d10 > 100.0D && (!this.mcServer.isSinglePlayer() || !this.mcServer.getServerOwner().equals(this.playerEntity.getCommandSenderName())))
                {
                    this.mcServer.getLogAgent().logWarning(this.playerEntity.getCommandSenderName() + " moved too quickly! " + d4 + "," + d5 + "," + d6 + " (" + d7 + ", " + d8 + ", " + d9 + ")");
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
                    return;
                }

                float f4 = 0.0625F;
                boolean flag = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract((double)f4, (double)f4, (double)f4)).isEmpty();

                if (this.playerEntity.onGround && !par1Packet10Flying.onGround && d5 > 0.0D)
                {
                    this.playerEntity.addExhaustion(0.2F);
                }

                if (!this.hasMoved) //Fixes "Moved Too Fast" kick when being teleported while moving
                {
                    return;
                }

                this.playerEntity.moveEntity(d4, d5, d6);
                this.playerEntity.onGround = par1Packet10Flying.onGround;
                this.playerEntity.addMovementStat(d4, d5, d6);
                double d11 = d5;
                d4 = d1 - this.playerEntity.posX;
                d5 = d2 - this.playerEntity.posY;

                if (d5 > -0.5D || d5 < 0.5D)
                {
                    d5 = 0.0D;
                }

                d6 = d3 - this.playerEntity.posZ;
                d10 = d4 * d4 + d5 * d5 + d6 * d6;
                boolean flag1 = false;

                if (d10 > 0.0625D && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.theItemInWorldManager.isCreative())
                {
                    flag1 = true;
                    this.mcServer.getLogAgent().logWarning(this.playerEntity.getCommandSenderName() + " moved wrongly!");
                }

                if (!this.hasMoved) //Fixes "Moved Too Fast" kick when being teleported while moving
                {
                    return;
                }

                this.playerEntity.setPositionAndRotation(d1, d2, d3, f2, f3);
                boolean flag2 = worldserver.getCollidingBoundingBoxes(this.playerEntity, this.playerEntity.boundingBox.copy().contract((double)f4, (double)f4, (double)f4)).isEmpty();

                if (flag && (flag1 || !flag2) && !this.playerEntity.isPlayerSleeping() && !this.playerEntity.noClip)
                {
                    this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, f2, f3);
                    return;
                }

                AxisAlignedBB axisalignedbb = this.playerEntity.boundingBox.copy().expand((double)f4, (double)f4, (double)f4).addCoord(0.0D, -0.55D, 0.0D);

                if (!this.mcServer.isFlightAllowed() && !this.playerEntity.theItemInWorldManager.isCreative() && !worldserver.checkBlockCollision(axisalignedbb)  && !this.playerEntity.capabilities.allowFlying)
                {
                    if (d11 >= -0.03125D)
                    {
                        ++this.ticksForFloatKick;

                        if (this.ticksForFloatKick > 80)
                        {
                            this.mcServer.getLogAgent().logWarning(this.playerEntity.getCommandSenderName() + " was kicked for floating too long!");
                            this.kickPlayerFromServer("Flying is not enabled on this server");
                            return;
                        }
                    }
                }
                else
                {
                    this.ticksForFloatKick = 0;
                }

                if (!this.hasMoved) //Fixes "Moved Too Fast" kick when being teleported while moving
                {
                    return;
                }

                this.playerEntity.onGround = par1Packet10Flying.onGround;
                this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this.playerEntity);
                this.playerEntity.updateFlyingState(this.playerEntity.posY - d0, par1Packet10Flying.onGround);
            }
            else if (this.currentTicks % 20 == 0)
            {
                this.setPlayerLocation(this.lastPosX, this.lastPosY, this.lastPosZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch);
            }
        }
    }

    /**
     * Moves the player to the specified destination and rotation
     */
    public void setPlayerLocation(double par1, double par3, double par5, float par7, float par8)
    {
        this.hasMoved = false;
        this.lastPosX = par1;
        this.lastPosY = par3;
        this.lastPosZ = par5;
        this.playerEntity.setPositionAndRotation(par1, par3, par5, par7, par8);
        this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet13PlayerLookMove(par1, par3 + 1.6200000047683716D, par3, par5, par7, par8, false));
    }

    public void handleBlockDig(Packet14BlockDig par1Packet14BlockDig)
    {
        WorldServer worldserver = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
        this.playerEntity.func_143004_u();

        if (par1Packet14BlockDig.status == 4)
        {
            this.playerEntity.dropOneItem(false);
        }
        else if (par1Packet14BlockDig.status == 3)
        {
            this.playerEntity.dropOneItem(true);
        }
        else if (par1Packet14BlockDig.status == 5)
        {
            this.playerEntity.stopUsingItem();
        }
        else
        {
            boolean flag = false;

            if (par1Packet14BlockDig.status == 0)
            {
                flag = true;
            }

            if (par1Packet14BlockDig.status == 1)
            {
                flag = true;
            }

            if (par1Packet14BlockDig.status == 2)
            {
                flag = true;
            }

            int i = par1Packet14BlockDig.xPosition;
            int j = par1Packet14BlockDig.yPosition;
            int k = par1Packet14BlockDig.zPosition;

            if (flag)
            {
                double d0 = this.playerEntity.posX - ((double)i + 0.5D);
                double d1 = this.playerEntity.posY - ((double)j + 0.5D) + 1.5D;
                double d2 = this.playerEntity.posZ - ((double)k + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                double dist = playerEntity.theItemInWorldManager.getBlockReachDistance() + 1;
                dist *= dist;

                if (d3 > dist)
                {
                    return;
                }

                if (j >= this.mcServer.getBuildLimit())
                {
                    return;
                }
            }

            if (par1Packet14BlockDig.status == 0)
            {
                if (!this.mcServer.isBlockProtected(worldserver, i, j, k, this.playerEntity))
                {
                    this.playerEntity.theItemInWorldManager.onBlockClicked(i, j, k, par1Packet14BlockDig.face);
                }
                else
                {
                    this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(i, j, k, worldserver));
                }
            }
            else if (par1Packet14BlockDig.status == 2)
            {
                this.playerEntity.theItemInWorldManager.uncheckedTryHarvestBlock(i, j, k);

                if (worldserver.getBlockId(i, j, k) != 0)
                {
                    this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(i, j, k, worldserver));
                }
            }
            else if (par1Packet14BlockDig.status == 1)
            {
                this.playerEntity.theItemInWorldManager.cancelDestroyingBlock(i, j, k);

                if (worldserver.getBlockId(i, j, k) != 0)
                {
                    this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(i, j, k, worldserver));
                }
            }
        }
    }

    public void handlePlace(Packet15Place par1Packet15Place)
    {
        WorldServer worldserver = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
        ItemStack itemstack = this.playerEntity.inventory.getCurrentItem();
        boolean flag = false;
        int i = par1Packet15Place.getXPosition();
        int j = par1Packet15Place.getYPosition();
        int k = par1Packet15Place.getZPosition();
        int l = par1Packet15Place.getDirection();
        this.playerEntity.func_143004_u();

        if (par1Packet15Place.getDirection() == 255)
        {
            if (itemstack == null)
            {
                return;
            }

            PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(playerEntity, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 0, 0, 0, -1);
            if (event.useItem != Event.Result.DENY)
            {
                this.playerEntity.theItemInWorldManager.tryUseItem(this.playerEntity, worldserver, itemstack);
            }
        }
        else if (par1Packet15Place.getYPosition() >= this.mcServer.getBuildLimit() - 1 && (par1Packet15Place.getDirection() == 1 || par1Packet15Place.getYPosition() >= this.mcServer.getBuildLimit()))
        {
            this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromTranslationWithSubstitutions("build.tooHigh", new Object[] {Integer.valueOf(this.mcServer.getBuildLimit())}).setColor(EnumChatFormatting.RED)));
            flag = true;
        }
        else
        {
            double dist = playerEntity.theItemInWorldManager.getBlockReachDistance() + 1;
            dist *= dist;
            if (this.hasMoved && this.playerEntity.getDistanceSq((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D) < dist && !this.mcServer.isBlockProtected(worldserver, i, j, k, this.playerEntity))
            {
                this.playerEntity.theItemInWorldManager.activateBlockOrUseItem(this.playerEntity, worldserver, itemstack, i, j, k, l, par1Packet15Place.getXOffset(), par1Packet15Place.getYOffset(), par1Packet15Place.getZOffset());
            }

            flag = true;
        }

        if (flag)
        {
            this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(i, j, k, worldserver));

            if (l == 0)
            {
                --j;
            }

            if (l == 1)
            {
                ++j;
            }

            if (l == 2)
            {
                --k;
            }

            if (l == 3)
            {
                ++k;
            }

            if (l == 4)
            {
                --i;
            }

            if (l == 5)
            {
                ++i;
            }

            this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(i, j, k, worldserver));
        }

        itemstack = this.playerEntity.inventory.getCurrentItem();

        if (itemstack != null && itemstack.stackSize == 0)
        {
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = null;
            itemstack = null;
        }

        if (itemstack == null || itemstack.getMaxItemUseDuration() == 0)
        {
            this.playerEntity.playerInventoryBeingManipulated = true;
            this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem] = ItemStack.copyItemStack(this.playerEntity.inventory.mainInventory[this.playerEntity.inventory.currentItem]);
            Slot slot = this.playerEntity.openContainer.getSlotFromInventory(this.playerEntity.inventory, this.playerEntity.inventory.currentItem);
            this.playerEntity.openContainer.detectAndSendChanges();
            this.playerEntity.playerInventoryBeingManipulated = false;

            if (!ItemStack.areItemStacksEqual(this.playerEntity.inventory.getCurrentItem(), par1Packet15Place.getItemStack()))
            {
                this.sendPacketToPlayer(new Packet103SetSlot(this.playerEntity.openContainer.windowId, slot.slotNumber, this.playerEntity.inventory.getCurrentItem()));
            }
        }
    }

    public void handleErrorMessage(String par1Str, Object[] par2ArrayOfObj)
    {
        this.mcServer.getLogAgent().logInfo(this.playerEntity.getCommandSenderName() + " lost connection: " + par1Str);
        this.mcServer.getConfigurationManager().sendChatMsg(ChatMessageComponent.createFromTranslationWithSubstitutions("multiplayer.player.left", new Object[] {this.playerEntity.getTranslatedEntityName()}).setColor(EnumChatFormatting.YELLOW));
        this.mcServer.getConfigurationManager().playerLoggedOut(this.playerEntity);
        this.connectionClosed = true;

        if (this.mcServer.isSinglePlayer() && this.playerEntity.getCommandSenderName().equals(this.mcServer.getServerOwner()))
        {
            this.mcServer.getLogAgent().logInfo("Stopping singleplayer server as player logged out");
            this.mcServer.initiateShutdown();
        }
    }

    /**
     * Default handler called for packets that don't have their own handlers in NetClientHandler; currentlly does
     * nothing.
     */
    public void unexpectedPacket(Packet par1Packet)
    {
        this.mcServer.getLogAgent().logWarning(this.getClass() + " wasn\'t prepared to deal with a " + par1Packet.getClass());
        this.kickPlayerFromServer("Protocol error, unexpected packet");
    }

    /**
     * addToSendQueue. if it is a chat packet, check before sending it
     */
    public void sendPacketToPlayer(Packet par1Packet)
    {
        if (par1Packet instanceof Packet3Chat)
        {
            Packet3Chat packet3chat = (Packet3Chat)par1Packet;
            int i = this.playerEntity.getChatVisibility();

            if (i == 2)
            {
                return;
            }

            if (i == 1 && !packet3chat.getIsServer())
            {
                return;
            }
        }

        try
        {
            this.netManager.addToSendQueue(par1Packet);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
            crashreportcategory.addCrashSectionCallable("Packet ID", new CallablePacketID(this, par1Packet));
            crashreportcategory.addCrashSectionCallable("Packet class", new CallablePacketClass(this, par1Packet));
            throw new ReportedException(crashreport);
        }
    }

    public void handleBlockItemSwitch(Packet16BlockItemSwitch par1Packet16BlockItemSwitch)
    {
        if (par1Packet16BlockItemSwitch.id >= 0 && par1Packet16BlockItemSwitch.id < InventoryPlayer.getHotbarSize())
        {
            this.playerEntity.inventory.currentItem = par1Packet16BlockItemSwitch.id;
            this.playerEntity.func_143004_u();
        }
        else
        {
            this.mcServer.getLogAgent().logWarning(this.playerEntity.getCommandSenderName() + " tried to set an invalid carried item");
        }
    }

    public void handleChat(Packet3Chat par1Packet3Chat)
    {
        par1Packet3Chat = FMLNetworkHandler.handleChatMessage(this, par1Packet3Chat);

        if (par1Packet3Chat == null || par1Packet3Chat.message == null)
        {
            return;
        }
        if (this.playerEntity.getChatVisibility() == 2)
        {
            this.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromTranslationKey("chat.cannotSend").setColor(EnumChatFormatting.RED)));
        }
        else
        {
            this.playerEntity.func_143004_u();
            String s = par1Packet3Chat.message;

            if (s.length() > 100)
            {
                this.kickPlayerFromServer("Chat message too long");
            }
            else
            {
                for (int i = 0; i < s.length(); ++i)
                {
                    if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
                    {
                        this.kickPlayerFromServer("Illegal characters in chat");
                        return;
                    }
                }

                if (s.startsWith("/"))
                {
                    this.handleSlashCommand(s);
                }
                else
                {
                    if (this.playerEntity.getChatVisibility() == 1)
                    {
                        this.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromTranslationKey("chat.cannotSend").setColor(EnumChatFormatting.RED)));
                        return;
                    }

                    ChatMessageComponent chatmessagecomponent = ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.text", new Object[] {this.playerEntity.getTranslatedEntityName(), s});
                    chatmessagecomponent = ForgeHooks.onServerChatEvent(this, s, chatmessagecomponent);
                    if (chatmessagecomponent == null) return;
                    this.mcServer.getConfigurationManager().func_110459_a(chatmessagecomponent, false);
                }

                this.chatSpamThresholdCount += 20;

                if (this.chatSpamThresholdCount > 200 && !this.mcServer.getConfigurationManager().isPlayerOpped(this.playerEntity.getCommandSenderName()))
                {
                    this.kickPlayerFromServer("disconnect.spam");
                }
            }
        }
    }

    /**
     * Processes a / command
     */
    private void handleSlashCommand(String par1Str)
    {
        this.mcServer.getCommandManager().executeCommand(this.playerEntity, par1Str);
    }

    public void handleAnimation(Packet18Animation par1Packet18Animation)
    {
        this.playerEntity.func_143004_u();

        if (par1Packet18Animation.animate == 1)
        {
            this.playerEntity.swingItem();
        }
    }

    /**
     * runs registerPacket on the given Packet19EntityAction
     */
    public void handleEntityAction(Packet19EntityAction par1Packet19EntityAction)
    {
        this.playerEntity.func_143004_u();

        if (par1Packet19EntityAction.action == 1)
        {
            this.playerEntity.setSneaking(true);
        }
        else if (par1Packet19EntityAction.action == 2)
        {
            this.playerEntity.setSneaking(false);
        }
        else if (par1Packet19EntityAction.action == 4)
        {
            this.playerEntity.setSprinting(true);
        }
        else if (par1Packet19EntityAction.action == 5)
        {
            this.playerEntity.setSprinting(false);
        }
        else if (par1Packet19EntityAction.action == 3)
        {
            this.playerEntity.wakeUpPlayer(false, true, true);
            this.hasMoved = false;
        }
        else if (par1Packet19EntityAction.action == 6)
        {
            if (this.playerEntity.ridingEntity != null && this.playerEntity.ridingEntity instanceof EntityHorse)
            {
                ((EntityHorse)this.playerEntity.ridingEntity).setJumpPower(par1Packet19EntityAction.auxData);
            }
        }
        else if (par1Packet19EntityAction.action == 7 && this.playerEntity.ridingEntity != null && this.playerEntity.ridingEntity instanceof EntityHorse)
        {
            ((EntityHorse)this.playerEntity.ridingEntity).openGUI(this.playerEntity);
        }
    }

    public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect)
    {
        this.netManager.networkShutdown("disconnect.quitting", new Object[0]);
    }

    /**
     * returns 0 for memoryMapped connections
     */
    public int packetSize()
    {
        return this.netManager.packetSize();
    }

    public void handleUseEntity(Packet7UseEntity par1Packet7UseEntity)
    {
        WorldServer worldserver = this.mcServer.worldServerForDimension(this.playerEntity.dimension);
        Entity entity = worldserver.getEntityByID(par1Packet7UseEntity.targetEntity);
        this.playerEntity.func_143004_u();

        if (entity != null)
        {
            boolean flag = this.playerEntity.canEntityBeSeen(entity);
            double d0 = 36.0D;

            if (!flag)
            {
                d0 = 9.0D;
            }

            if (this.playerEntity.getDistanceSqToEntity(entity) < d0)
            {
                if (par1Packet7UseEntity.isLeftClick == 0)
                {
                    this.playerEntity.interactWith(entity);
                }
                else if (par1Packet7UseEntity.isLeftClick == 1)
                {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == this.playerEntity)
                    {
                        this.kickPlayerFromServer("Attempting to attack an invalid entity");
                        this.mcServer.logWarning("Player " + this.playerEntity.getCommandSenderName() + " tried to attack an invalid entity");
                        return;
                    }

                    this.playerEntity.attackTargetEntityWithCurrentItem(entity);
                }
            }
        }
    }

    public void handleClientCommand(Packet205ClientCommand par1Packet205ClientCommand)
    {
        this.playerEntity.func_143004_u();

        if (par1Packet205ClientCommand.forceRespawn == 1)
        {
            if (this.playerEntity.playerConqueredTheEnd)
            {
                this.playerEntity = this.mcServer.getConfigurationManager().respawnPlayer(this.playerEntity, 0, true);
            }
            else if (this.playerEntity.getServerForPlayer().getWorldInfo().isHardcoreModeEnabled())
            {
                if (this.mcServer.isSinglePlayer() && this.playerEntity.getCommandSenderName().equals(this.mcServer.getServerOwner()))
                {
                    this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it\'s game over!");
                    this.mcServer.deleteWorldAndStopServer();
                }
                else
                {
                    BanEntry banentry = new BanEntry(this.playerEntity.getCommandSenderName());
                    banentry.setBanReason("Death in Hardcore");
                    this.mcServer.getConfigurationManager().getBannedPlayers().put(banentry);
                    this.playerEntity.playerNetServerHandler.kickPlayerFromServer("You have died. Game over, man, it\'s game over!");
                }
            }
            else
            {
                if (this.playerEntity.getHealth() > 0.0F)
                {
                    return;
                }

                this.playerEntity = this.mcServer.getConfigurationManager().respawnPlayer(this.playerEntity, playerEntity.dimension, false);
            }
        }
    }

    /**
     * If this returns false, all packets will be queued for the main thread to handle, even if they would otherwise be
     * processed asynchronously. Used to avoid processing packets on the client before the world has been downloaded
     * (which happens on the main thread)
     */
    public boolean canProcessPacketsAsync()
    {
        return true;
    }

    /**
     * respawns the player
     */
    public void handleRespawn(Packet9Respawn par1Packet9Respawn) {}

    public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow)
    {
        this.playerEntity.closeContainer();
    }

    public void handleWindowClick(Packet102WindowClick par1Packet102WindowClick)
    {
        this.playerEntity.func_143004_u();

        if (this.playerEntity.openContainer.windowId == par1Packet102WindowClick.window_Id && this.playerEntity.openContainer.isPlayerNotUsingContainer(this.playerEntity))
        {
            ItemStack itemstack = this.playerEntity.openContainer.slotClick(par1Packet102WindowClick.inventorySlot, par1Packet102WindowClick.mouseClick, par1Packet102WindowClick.holdingShift, this.playerEntity);

            if (ItemStack.areItemStacksEqual(par1Packet102WindowClick.itemStack, itemstack))
            {
                this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet106Transaction(par1Packet102WindowClick.window_Id, par1Packet102WindowClick.action, true));
                this.playerEntity.playerInventoryBeingManipulated = true;
                this.playerEntity.openContainer.detectAndSendChanges();
                this.playerEntity.updateHeldItem();
                this.playerEntity.playerInventoryBeingManipulated = false;
            }
            else
            {
                this.field_72586_s.addKey(this.playerEntity.openContainer.windowId, Short.valueOf(par1Packet102WindowClick.action));
                this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet106Transaction(par1Packet102WindowClick.window_Id, par1Packet102WindowClick.action, false));
                this.playerEntity.openContainer.setPlayerIsPresent(this.playerEntity, false);
                ArrayList arraylist = new ArrayList();

                for (int i = 0; i < this.playerEntity.openContainer.inventorySlots.size(); ++i)
                {
                    arraylist.add(((Slot)this.playerEntity.openContainer.inventorySlots.get(i)).getStack());
                }

                this.playerEntity.sendContainerAndContentsToPlayer(this.playerEntity.openContainer, arraylist);
            }
        }
    }

    public void handleEnchantItem(Packet108EnchantItem par1Packet108EnchantItem)
    {
        this.playerEntity.func_143004_u();

        if (this.playerEntity.openContainer.windowId == par1Packet108EnchantItem.windowId && this.playerEntity.openContainer.isPlayerNotUsingContainer(this.playerEntity))
        {
            this.playerEntity.openContainer.enchantItem(this.playerEntity, par1Packet108EnchantItem.enchantment);
            this.playerEntity.openContainer.detectAndSendChanges();
        }
    }

    /**
     * Handle a creative slot packet.
     */
    public void handleCreativeSetSlot(Packet107CreativeSetSlot par1Packet107CreativeSetSlot)
    {
        if (this.playerEntity.theItemInWorldManager.isCreative())
        {
            boolean flag = par1Packet107CreativeSetSlot.slot < 0;
            ItemStack itemstack = par1Packet107CreativeSetSlot.itemStack;
            boolean flag1 = par1Packet107CreativeSetSlot.slot >= 1 && par1Packet107CreativeSetSlot.slot < 36 + InventoryPlayer.getHotbarSize();
            boolean flag2 = itemstack == null || itemstack.itemID < Item.itemsList.length && itemstack.itemID >= 0 && Item.itemsList[itemstack.itemID] != null;
            boolean flag3 = itemstack == null || itemstack.getItemDamage() >= 0 && itemstack.getItemDamage() >= 0 && itemstack.stackSize <= 64 && itemstack.stackSize > 0;

            if (flag1 && flag2 && flag3)
            {
                if (itemstack == null)
                {
                    this.playerEntity.inventoryContainer.putStackInSlot(par1Packet107CreativeSetSlot.slot, (ItemStack)null);
                }
                else
                {
                    this.playerEntity.inventoryContainer.putStackInSlot(par1Packet107CreativeSetSlot.slot, itemstack);
                }

                this.playerEntity.inventoryContainer.setPlayerIsPresent(this.playerEntity, true);
            }
            else if (flag && flag2 && flag3 && this.creativeItemCreationSpamThresholdTally < 200)
            {
                this.creativeItemCreationSpamThresholdTally += 20;
                EntityItem entityitem = this.playerEntity.dropPlayerItem(itemstack);

                if (entityitem != null)
                {
                    entityitem.setAgeToCreativeDespawnTime();
                }
            }
        }
    }

    public void handleTransaction(Packet106Transaction par1Packet106Transaction)
    {
        Short oshort = (Short)this.field_72586_s.lookup(this.playerEntity.openContainer.windowId);

        if (oshort != null && par1Packet106Transaction.shortWindowId == oshort.shortValue() && this.playerEntity.openContainer.windowId == par1Packet106Transaction.windowId && !this.playerEntity.openContainer.isPlayerNotUsingContainer(this.playerEntity))
        {
            this.playerEntity.openContainer.setPlayerIsPresent(this.playerEntity, true);
        }
    }

    /**
     * Updates Client side signs
     */
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign)
    {
        this.playerEntity.func_143004_u();
        WorldServer worldserver = this.mcServer.worldServerForDimension(this.playerEntity.dimension);

        if (worldserver.blockExists(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition))
        {
            TileEntity tileentity = worldserver.getBlockTileEntity(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition);

            if (tileentity instanceof TileEntitySign)
            {
                TileEntitySign tileentitysign = (TileEntitySign)tileentity;

                if (!tileentitysign.isEditable() || tileentitysign.func_142009_b() != this.playerEntity)
                {
                    this.mcServer.logWarning("Player " + this.playerEntity.getCommandSenderName() + " just tried to change non-editable sign");
                    return;
                }
            }

            int i;
            int j;

            for (j = 0; j < 4; ++j)
            {
                boolean flag = true;

                if (par1Packet130UpdateSign.signLines[j].length() > 15)
                {
                    flag = false;
                }
                else
                {
                    for (i = 0; i < par1Packet130UpdateSign.signLines[j].length(); ++i)
                    {
                        if (ChatAllowedCharacters.allowedCharacters.indexOf(par1Packet130UpdateSign.signLines[j].charAt(i)) < 0)
                        {
                            flag = false;
                        }
                    }
                }

                if (!flag)
                {
                    par1Packet130UpdateSign.signLines[j] = "!?";
                }
            }

            if (tileentity instanceof TileEntitySign)
            {
                j = par1Packet130UpdateSign.xPosition;
                int k = par1Packet130UpdateSign.yPosition;
                i = par1Packet130UpdateSign.zPosition;
                TileEntitySign tileentitysign1 = (TileEntitySign)tileentity;
                System.arraycopy(par1Packet130UpdateSign.signLines, 0, tileentitysign1.signText, 0, 4);
                tileentitysign1.onInventoryChanged();
                worldserver.markBlockForUpdate(j, k, i);
            }
        }
    }

    /**
     * Handle a keep alive packet.
     */
    public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive)
    {
        if (par1Packet0KeepAlive.randomId == this.keepAliveRandomID)
        {
            int i = (int)(System.nanoTime() / 1000000L - this.keepAliveTimeSent);
            this.playerEntity.ping = (this.playerEntity.ping * 3 + i) / 4;
        }
    }

    /**
     * determine if it is a server handler
     */
    public boolean isServerHandler()
    {
        return true;
    }

    /**
     * Handle a player abilities packet.
     */
    public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities)
    {
        this.playerEntity.capabilities.isFlying = par1Packet202PlayerAbilities.getFlying() && this.playerEntity.capabilities.allowFlying;
    }

    public void handleAutoComplete(Packet203AutoComplete par1Packet203AutoComplete)
    {
        StringBuilder stringbuilder = new StringBuilder();
        String s;

        for (Iterator iterator = this.mcServer.getPossibleCompletions(this.playerEntity, par1Packet203AutoComplete.getText()).iterator(); iterator.hasNext(); stringbuilder.append(s))
        {
            s = (String)iterator.next();

            if (stringbuilder.length() > 0)
            {
                stringbuilder.append("\u0000");
            }
        }

        this.playerEntity.playerNetServerHandler.sendPacketToPlayer(new Packet203AutoComplete(stringbuilder.toString()));
    }

    public void handleClientInfo(Packet204ClientInfo par1Packet204ClientInfo)
    {
        this.playerEntity.updateClientInfo(par1Packet204ClientInfo);
    }

    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
        FMLNetworkHandler.handlePacket250Packet(par1Packet250CustomPayload, netManager, this);
    }

    public void handleVanilla250Packet(Packet250CustomPayload par1Packet250CustomPayload)
    {
        DataInputStream datainputstream;
        ItemStack itemstack;
        ItemStack itemstack1;

        if ("MC|BEdit".equals(par1Packet250CustomPayload.channel))
        {
            try
            {
                datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                itemstack = Packet.readItemStack(datainputstream);

                if (!ItemWritableBook.validBookTagPages(itemstack.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.playerEntity.inventory.getCurrentItem();

                if (itemstack != null && itemstack.itemID == Item.writableBook.itemID && itemstack.itemID == itemstack1.itemID)
                {
                    itemstack1.setTagInfo("pages", itemstack.getTagCompound().getTagList("pages"));
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else if ("MC|BSign".equals(par1Packet250CustomPayload.channel))
        {
            try
            {
                datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                itemstack = Packet.readItemStack(datainputstream);

                if (!ItemEditableBook.validBookTagContents(itemstack.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                itemstack1 = this.playerEntity.inventory.getCurrentItem();

                if (itemstack != null && itemstack.itemID == Item.writtenBook.itemID && itemstack1.itemID == Item.writableBook.itemID)
                {
                    itemstack1.setTagInfo("author", new NBTTagString("author", this.playerEntity.getCommandSenderName()));
                    itemstack1.setTagInfo("title", new NBTTagString("title", itemstack.getTagCompound().getString("title")));
                    itemstack1.setTagInfo("pages", itemstack.getTagCompound().getTagList("pages"));
                    itemstack1.itemID = Item.writtenBook.itemID;
                }
            }
            catch (Exception exception1)
            {
                exception1.printStackTrace();
            }
        }
        else
        {
            int i;

            if ("MC|TrSel".equals(par1Packet250CustomPayload.channel))
            {
                try
                {
                    datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                    i = datainputstream.readInt();
                    Container container = this.playerEntity.openContainer;

                    if (container instanceof ContainerMerchant)
                    {
                        ((ContainerMerchant)container).setCurrentRecipeIndex(i);
                    }
                }
                catch (Exception exception2)
                {
                    exception2.printStackTrace();
                }
            }
            else
            {
                int j;

                if ("MC|AdvCdm".equals(par1Packet250CustomPayload.channel))
                {
                    if (!this.mcServer.isCommandBlockEnabled())
                    {
                        this.playerEntity.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("advMode.notEnabled"));
                    }
                    else if (this.playerEntity.canCommandSenderUseCommand(2, "") && this.playerEntity.capabilities.isCreativeMode)
                    {
                        try
                        {
                            datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                            i = datainputstream.readInt();
                            j = datainputstream.readInt();
                            int k = datainputstream.readInt();
                            String s = Packet.readString(datainputstream, 256);
                            TileEntity tileentity = this.playerEntity.worldObj.getBlockTileEntity(i, j, k);

                            if (tileentity != null && tileentity instanceof TileEntityCommandBlock)
                            {
                                ((TileEntityCommandBlock)tileentity).setCommand(s);
                                this.playerEntity.worldObj.markBlockForUpdate(i, j, k);
                                this.playerEntity.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("advMode.setCommand.success", new Object[] {s}));
                            }
                        }
                        catch (Exception exception3)
                        {
                            exception3.printStackTrace();
                        }
                    }
                    else
                    {
                        this.playerEntity.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("advMode.notAllowed"));
                    }
                }
                else if ("MC|Beacon".equals(par1Packet250CustomPayload.channel))
                {
                    if (this.playerEntity.openContainer instanceof ContainerBeacon)
                    {
                        try
                        {
                            datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                            i = datainputstream.readInt();
                            j = datainputstream.readInt();
                            ContainerBeacon containerbeacon = (ContainerBeacon)this.playerEntity.openContainer;
                            Slot slot = containerbeacon.getSlot(0);

                            if (slot.getHasStack())
                            {
                                slot.decrStackSize(1);
                                TileEntityBeacon tileentitybeacon = containerbeacon.getBeacon();
                                tileentitybeacon.setPrimaryEffect(i);
                                tileentitybeacon.setSecondaryEffect(j);
                                tileentitybeacon.onInventoryChanged();
                            }
                        }
                        catch (Exception exception4)
                        {
                            exception4.printStackTrace();
                        }
                    }
                }
                else if ("MC|ItemName".equals(par1Packet250CustomPayload.channel) && this.playerEntity.openContainer instanceof ContainerRepair)
                {
                    ContainerRepair containerrepair = (ContainerRepair)this.playerEntity.openContainer;

                    if (par1Packet250CustomPayload.data != null && par1Packet250CustomPayload.data.length >= 1)
                    {
                        String s1 = ChatAllowedCharacters.filerAllowedCharacters(new String(par1Packet250CustomPayload.data));

                        if (s1.length() <= 30)
                        {
                            containerrepair.updateItemName(s1);
                        }
                    }
                    else
                    {
                        containerrepair.updateItemName("");
                    }
                }
            }
        }
    }

    public boolean isConnectionClosed()
    {
        return this.connectionClosed;
    }


    @Override

    /**
     * Contains logic for handling packets containing arbitrary unique item data. Currently this is only for maps.
     */
    public void handleMapData(Packet131MapData par1Packet131MapData)
    {
        FMLNetworkHandler.handlePacket131Packet(this, par1Packet131MapData);
    }

    // modloader compat -- yuk!
    @Override
    public EntityPlayerMP getPlayer()
    {
        return playerEntity;
    }
}
