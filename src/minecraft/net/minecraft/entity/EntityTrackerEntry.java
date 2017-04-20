package net.minecraft.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet17Sleep;
import net.minecraft.network.packet.Packet20NamedEntitySpawn;
import net.minecraft.network.packet.Packet23VehicleSpawn;
import net.minecraft.network.packet.Packet24MobSpawn;
import net.minecraft.network.packet.Packet25EntityPainting;
import net.minecraft.network.packet.Packet26EntityExpOrb;
import net.minecraft.network.packet.Packet28EntityVelocity;
import net.minecraft.network.packet.Packet31RelEntityMove;
import net.minecraft.network.packet.Packet32EntityLook;
import net.minecraft.network.packet.Packet33RelEntityMoveLook;
import net.minecraft.network.packet.Packet34EntityTeleport;
import net.minecraft.network.packet.Packet35EntityHeadRotation;
import net.minecraft.network.packet.Packet39AttachEntity;
import net.minecraft.network.packet.Packet40EntityMetadata;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet44UpdateAttributes;
import net.minecraft.network.packet.Packet5PlayerInventory;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;

public class EntityTrackerEntry
{
    public Entity myEntity;
    public int blocksDistanceThreshold;

    /** check for sync when ticks % updateFrequency==0 */
    public int updateFrequency;
    public int lastScaledXPosition;
    public int lastScaledYPosition;
    public int lastScaledZPosition;
    public int lastYaw;
    public int lastPitch;
    public int lastHeadMotion;
    public double motionX;
    public double motionY;
    public double motionZ;
    public int ticks;
    private double posX;
    private double posY;
    private double posZ;

    /** set to true on first sendLocationToClients */
    private boolean isDataInitialized;
    private boolean sendVelocityUpdates;

    /**
     * every 400 ticks a  full teleport packet is sent, rather than just a "move me +x" command, so that position
     * remains fully synced.
     */
    private int ticksSinceLastForcedTeleport;
    private Entity field_85178_v;
    private boolean ridingEntity;
    public boolean playerEntitiesUpdated;

    /**
     * Holds references to all the players that are currently receiving position updates for this entity.
     */
    public Set trackingPlayers = new HashSet();

    public EntityTrackerEntry(Entity par1Entity, int par2, int par3, boolean par4)
    {
        this.myEntity = par1Entity;
        this.blocksDistanceThreshold = par2;
        this.updateFrequency = par3;
        this.sendVelocityUpdates = par4;
        this.lastScaledXPosition = MathHelper.floor_double(par1Entity.posX * 32.0D);
        this.lastScaledYPosition = MathHelper.floor_double(par1Entity.posY * 32.0D);
        this.lastScaledZPosition = MathHelper.floor_double(par1Entity.posZ * 32.0D);
        this.lastYaw = MathHelper.floor_float(par1Entity.rotationYaw * 256.0F / 360.0F);
        this.lastPitch = MathHelper.floor_float(par1Entity.rotationPitch * 256.0F / 360.0F);
        this.lastHeadMotion = MathHelper.floor_float(par1Entity.getRotationYawHead() * 256.0F / 360.0F);
    }

    public boolean equals(Object par1Obj)
    {
        return par1Obj instanceof EntityTrackerEntry ? ((EntityTrackerEntry)par1Obj).myEntity.entityId == this.myEntity.entityId : false;
    }

    public int hashCode()
    {
        return this.myEntity.entityId;
    }

    /**
     * also sends velocity, rotation, and riding info.
     */
    public void sendLocationToAllClients(List par1List)
    {
        this.playerEntitiesUpdated = false;

        if (!this.isDataInitialized || this.myEntity.getDistanceSq(this.posX, this.posY, this.posZ) > 16.0D)
        {
            this.posX = this.myEntity.posX;
            this.posY = this.myEntity.posY;
            this.posZ = this.myEntity.posZ;
            this.isDataInitialized = true;
            this.playerEntitiesUpdated = true;
            this.sendEventsToPlayers(par1List);
        }

        if (this.field_85178_v != this.myEntity.ridingEntity || this.myEntity.ridingEntity != null && this.ticks % 60 == 0)
        {
            this.field_85178_v = this.myEntity.ridingEntity;
            this.sendPacketToAllTrackingPlayers(new Packet39AttachEntity(0, this.myEntity, this.myEntity.ridingEntity));
        }

        if (this.myEntity instanceof EntityItemFrame && this.ticks % 10 == 0)
        {
            EntityItemFrame entityitemframe = (EntityItemFrame)this.myEntity;
            ItemStack itemstack = entityitemframe.getDisplayedItem();

            if (itemstack != null && itemstack.getItem() instanceof ItemMap)
            {
                MapData mapdata = Item.map.getMapData(itemstack, this.myEntity.worldObj);
                Iterator iterator = par1List.iterator();

                while (iterator.hasNext())
                {
                    EntityPlayer entityplayer = (EntityPlayer)iterator.next();
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
                    mapdata.updateVisiblePlayers(entityplayermp, itemstack);

                    if (entityplayermp.playerNetServerHandler.packetSize() <= 5)
                    {
                        Packet packet = Item.map.createMapDataPacket(itemstack, this.myEntity.worldObj, entityplayermp);

                        if (packet != null)
                        {
                            entityplayermp.playerNetServerHandler.sendPacketToPlayer(packet);
                        }
                    }
                }
            }

            this.func_111190_b();
        }
        else if (this.ticks % this.updateFrequency == 0 || this.myEntity.isAirBorne || this.myEntity.getDataWatcher().hasChanges())
        {
            int i;
            int j;

            if (this.myEntity.ridingEntity == null)
            {
                ++this.ticksSinceLastForcedTeleport;
                i = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posX);
                j = MathHelper.floor_double(this.myEntity.posY * 32.0D);
                int k = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posZ);
                int l = MathHelper.floor_float(this.myEntity.rotationYaw * 256.0F / 360.0F);
                int i1 = MathHelper.floor_float(this.myEntity.rotationPitch * 256.0F / 360.0F);
                int j1 = i - this.lastScaledXPosition;
                int k1 = j - this.lastScaledYPosition;
                int l1 = k - this.lastScaledZPosition;
                Object object = null;
                boolean flag = Math.abs(j1) >= 4 || Math.abs(k1) >= 4 || Math.abs(l1) >= 4 || this.ticks % 60 == 0;
                boolean flag1 = Math.abs(l - this.lastYaw) >= 4 || Math.abs(i1 - this.lastPitch) >= 4;

                if (this.ticks > 0 || this.myEntity instanceof EntityArrow)
                {
                    if (j1 >= -128 && j1 < 128 && k1 >= -128 && k1 < 128 && l1 >= -128 && l1 < 128 && this.ticksSinceLastForcedTeleport <= 400 && !this.ridingEntity)
                    {
                        if (flag && flag1)
                        {
                            object = new Packet33RelEntityMoveLook(this.myEntity.entityId, (byte)j1, (byte)k1, (byte)l1, (byte)l, (byte)i1);
                        }
                        else if (flag)
                        {
                            object = new Packet31RelEntityMove(this.myEntity.entityId, (byte)j1, (byte)k1, (byte)l1);
                        }
                        else if (flag1)
                        {
                            object = new Packet32EntityLook(this.myEntity.entityId, (byte)l, (byte)i1);
                        }
                    }
                    else
                    {
                        this.ticksSinceLastForcedTeleport = 0;
                        object = new Packet34EntityTeleport(this.myEntity.entityId, i, j, k, (byte)l, (byte)i1);
                    }
                }

                if (this.sendVelocityUpdates)
                {
                    double d0 = this.myEntity.motionX - this.motionX;
                    double d1 = this.myEntity.motionY - this.motionY;
                    double d2 = this.myEntity.motionZ - this.motionZ;
                    double d3 = 0.02D;
                    double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d4 > d3 * d3 || d4 > 0.0D && this.myEntity.motionX == 0.0D && this.myEntity.motionY == 0.0D && this.myEntity.motionZ == 0.0D)
                    {
                        this.motionX = this.myEntity.motionX;
                        this.motionY = this.myEntity.motionY;
                        this.motionZ = this.myEntity.motionZ;
                        this.sendPacketToAllTrackingPlayers(new Packet28EntityVelocity(this.myEntity.entityId, this.motionX, this.motionY, this.motionZ));
                    }
                }

                if (object != null)
                {
                    this.sendPacketToAllTrackingPlayers((Packet)object);
                }

                this.func_111190_b();

                if (flag)
                {
                    this.lastScaledXPosition = i;
                    this.lastScaledYPosition = j;
                    this.lastScaledZPosition = k;
                }

                if (flag1)
                {
                    this.lastYaw = l;
                    this.lastPitch = i1;
                }

                this.ridingEntity = false;
            }
            else
            {
                i = MathHelper.floor_float(this.myEntity.rotationYaw * 256.0F / 360.0F);
                j = MathHelper.floor_float(this.myEntity.rotationPitch * 256.0F / 360.0F);
                boolean flag2 = Math.abs(i - this.lastYaw) >= 4 || Math.abs(j - this.lastPitch) >= 4;

                if (flag2)
                {
                    this.sendPacketToAllTrackingPlayers(new Packet32EntityLook(this.myEntity.entityId, (byte)i, (byte)j));
                    this.lastYaw = i;
                    this.lastPitch = j;
                }

                this.lastScaledXPosition = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posX);
                this.lastScaledYPosition = MathHelper.floor_double(this.myEntity.posY * 32.0D);
                this.lastScaledZPosition = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posZ);
                this.func_111190_b();
                this.ridingEntity = true;
            }

            i = MathHelper.floor_float(this.myEntity.getRotationYawHead() * 256.0F / 360.0F);

            if (Math.abs(i - this.lastHeadMotion) >= 4)
            {
                this.sendPacketToAllTrackingPlayers(new Packet35EntityHeadRotation(this.myEntity.entityId, (byte)i));
                this.lastHeadMotion = i;
            }

            this.myEntity.isAirBorne = false;
        }

        ++this.ticks;

        if (this.myEntity.velocityChanged)
        {
            this.sendPacketToAllAssociatedPlayers(new Packet28EntityVelocity(this.myEntity));
            this.myEntity.velocityChanged = false;
        }
    }

    private void func_111190_b()
    {
        DataWatcher datawatcher = this.myEntity.getDataWatcher();

        if (datawatcher.hasChanges())
        {
            this.sendPacketToAllAssociatedPlayers(new Packet40EntityMetadata(this.myEntity.entityId, datawatcher, false));
        }

        if (this.myEntity instanceof EntityLivingBase)
        {
            ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.myEntity).getAttributeMap();
            Set set = serversideattributemap.func_111161_b();

            if (!set.isEmpty())
            {
                this.sendPacketToAllAssociatedPlayers(new Packet44UpdateAttributes(this.myEntity.entityId, set));
            }

            set.clear();
        }
    }

    /**
     * if this is a player, then it is not informed
     */
    public void sendPacketToAllTrackingPlayers(Packet par1Packet)
    {
        Iterator iterator = this.trackingPlayers.iterator();

        while (iterator.hasNext())
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
            entityplayermp.playerNetServerHandler.sendPacketToPlayer(par1Packet);
        }
    }

    /**
     * if this is a player, then it recieves the message also
     */
    public void sendPacketToAllAssociatedPlayers(Packet par1Packet)
    {
        this.sendPacketToAllTrackingPlayers(par1Packet);

        if (this.myEntity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)this.myEntity).playerNetServerHandler.sendPacketToPlayer(par1Packet);
        }
    }

    public void informAllAssociatedPlayersOfItemDestruction()
    {
        Iterator iterator = this.trackingPlayers.iterator();

        while (iterator.hasNext())
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
            entityplayermp.destroyedItemsNetCache.add(Integer.valueOf(this.myEntity.entityId));
        }
    }

    public void removeFromWatchingList(EntityPlayerMP par1EntityPlayerMP)
    {
        if (this.trackingPlayers.contains(par1EntityPlayerMP))
        {
            par1EntityPlayerMP.destroyedItemsNetCache.add(Integer.valueOf(this.myEntity.entityId));
            this.trackingPlayers.remove(par1EntityPlayerMP);
        }
    }

    /**
     * if the player is more than the distance threshold (typically 64) then the player is removed instead
     */
    public void tryStartWachingThis(EntityPlayerMP par1EntityPlayerMP)
    {
        if (par1EntityPlayerMP != this.myEntity)
        {
            double d0 = par1EntityPlayerMP.posX - (double)(this.lastScaledXPosition / 32);
            double d1 = par1EntityPlayerMP.posZ - (double)(this.lastScaledZPosition / 32);

            if (d0 >= (double)(-this.blocksDistanceThreshold) && d0 <= (double)this.blocksDistanceThreshold && d1 >= (double)(-this.blocksDistanceThreshold) && d1 <= (double)this.blocksDistanceThreshold)
            {
                if (!this.trackingPlayers.contains(par1EntityPlayerMP) && (this.isPlayerWatchingThisChunk(par1EntityPlayerMP) || this.myEntity.forceSpawn))
                {
                    this.trackingPlayers.add(par1EntityPlayerMP);
                    Packet packet = this.getPacketForThisEntity();
                    par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(packet);

                    if (!this.myEntity.getDataWatcher().getIsBlank())
                    {
                        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet40EntityMetadata(this.myEntity.entityId, this.myEntity.getDataWatcher(), true));
                    }

                    if (this.myEntity instanceof EntityLivingBase)
                    {
                        ServersideAttributeMap serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.myEntity).getAttributeMap();
                        Collection collection = serversideattributemap.func_111160_c();

                        if (!collection.isEmpty())
                        {
                            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet44UpdateAttributes(this.myEntity.entityId, collection));
                        }
                    }

                    this.motionX = this.myEntity.motionX;
                    this.motionY = this.myEntity.motionY;
                    this.motionZ = this.myEntity.motionZ;

                    int posX = MathHelper.floor_double(this.myEntity.posX * 32.0D);
                    int posY = MathHelper.floor_double(this.myEntity.posY * 32.0D);
                    int posZ = MathHelper.floor_double(this.myEntity.posZ * 32.0D);
                    if (posX != this.lastScaledXPosition || posY != this.lastScaledYPosition || posZ != this.lastScaledZPosition)
                    {
                        FMLNetworkHandler.makeEntitySpawnAdjustment(this.myEntity.entityId, par1EntityPlayerMP, this.lastScaledXPosition, this.lastScaledYPosition, this.lastScaledZPosition);
                    }

                    if (this.sendVelocityUpdates && !(packet instanceof Packet24MobSpawn))
                    {
                        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet28EntityVelocity(this.myEntity.entityId, this.myEntity.motionX, this.myEntity.motionY, this.myEntity.motionZ));
                    }

                    if (this.myEntity.ridingEntity != null)
                    {
                        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet39AttachEntity(0, this.myEntity, this.myEntity.ridingEntity));
                    }

                    if (this.myEntity instanceof EntityLiving && ((EntityLiving)this.myEntity).getLeashedToEntity() != null)
                    {
                        par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet39AttachEntity(1, this.myEntity, ((EntityLiving)this.myEntity).getLeashedToEntity()));
                    }

                    if (this.myEntity instanceof EntityLivingBase)
                    {
                        for (int i = 0; i < 5; ++i)
                        {
                            ItemStack itemstack = ((EntityLivingBase)this.myEntity).getCurrentItemOrArmor(i);

                            if (itemstack != null)
                            {
                                par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet5PlayerInventory(this.myEntity.entityId, i, itemstack));
                            }
                        }
                    }

                    if (this.myEntity instanceof EntityPlayer)
                    {
                        EntityPlayer entityplayer = (EntityPlayer)this.myEntity;

                        if (entityplayer.isPlayerSleeping())
                        {
                            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet17Sleep(this.myEntity, 0, MathHelper.floor_double(this.myEntity.posX), MathHelper.floor_double(this.myEntity.posY), MathHelper.floor_double(this.myEntity.posZ)));
                        }
                    }

                    if (this.myEntity instanceof EntityLivingBase)
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)this.myEntity;
                        Iterator iterator = entitylivingbase.getActivePotionEffects().iterator();

                        while (iterator.hasNext())
                        {
                            PotionEffect potioneffect = (PotionEffect)iterator.next();
                            par1EntityPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.myEntity.entityId, potioneffect));
                        }
                    }
                }
            }
            else if (this.trackingPlayers.contains(par1EntityPlayerMP))
            {
                this.trackingPlayers.remove(par1EntityPlayerMP);
                par1EntityPlayerMP.destroyedItemsNetCache.add(Integer.valueOf(this.myEntity.entityId));
            }
        }
    }

    private boolean isPlayerWatchingThisChunk(EntityPlayerMP par1EntityPlayerMP)
    {
        return par1EntityPlayerMP.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(par1EntityPlayerMP, this.myEntity.chunkCoordX, this.myEntity.chunkCoordZ);
    }

    public void sendEventsToPlayers(List par1List)
    {
        for (int i = 0; i < par1List.size(); ++i)
        {
            this.tryStartWachingThis((EntityPlayerMP)par1List.get(i));
        }
    }

    private Packet getPacketForThisEntity()
    {
        if (this.myEntity.isDead)
        {
            this.myEntity.worldObj.getWorldLogAgent().logWarning("Fetching addPacket for removed entity");
        }

        Packet pkt = FMLNetworkHandler.getEntitySpawningPacket(this.myEntity);

        if (pkt != null)
        {
            return pkt;
        }

        if (this.myEntity instanceof EntityItem)
        {
            return new Packet23VehicleSpawn(this.myEntity, 2, 1);
        }
        else if (this.myEntity instanceof EntityPlayerMP)
        {
            return new Packet20NamedEntitySpawn((EntityPlayer)this.myEntity);
        }
        else if (this.myEntity instanceof EntityMinecart)
        {
            EntityMinecart entityminecart = (EntityMinecart)this.myEntity;
            return new Packet23VehicleSpawn(this.myEntity, 10, entityminecart.getMinecartType());
        }
        else if (this.myEntity instanceof EntityBoat)
        {
            return new Packet23VehicleSpawn(this.myEntity, 1);
        }
        else if (!(this.myEntity instanceof IAnimals) && !(this.myEntity instanceof EntityDragon))
        {
            if (this.myEntity instanceof EntityFishHook)
            {
                EntityPlayer entityplayer = ((EntityFishHook)this.myEntity).angler;
                return new Packet23VehicleSpawn(this.myEntity, 90, entityplayer != null ? entityplayer.entityId : this.myEntity.entityId);
            }
            else if (this.myEntity instanceof EntityArrow)
            {
                Entity entity = ((EntityArrow)this.myEntity).shootingEntity;
                return new Packet23VehicleSpawn(this.myEntity, 60, entity != null ? entity.entityId : this.myEntity.entityId);
            }
            else if (this.myEntity instanceof EntitySnowball)
            {
                return new Packet23VehicleSpawn(this.myEntity, 61);
            }
            else if (this.myEntity instanceof EntityPotion)
            {
                return new Packet23VehicleSpawn(this.myEntity, 73, ((EntityPotion)this.myEntity).getPotionDamage());
            }
            else if (this.myEntity instanceof EntityExpBottle)
            {
                return new Packet23VehicleSpawn(this.myEntity, 75);
            }
            else if (this.myEntity instanceof EntityEnderPearl)
            {
                return new Packet23VehicleSpawn(this.myEntity, 65);
            }
            else if (this.myEntity instanceof EntityEnderEye)
            {
                return new Packet23VehicleSpawn(this.myEntity, 72);
            }
            else if (this.myEntity instanceof EntityFireworkRocket)
            {
                return new Packet23VehicleSpawn(this.myEntity, 76);
            }
            else
            {
                Packet23VehicleSpawn packet23vehiclespawn;

                if (this.myEntity instanceof EntityFireball)
                {
                    EntityFireball entityfireball = (EntityFireball)this.myEntity;
                    packet23vehiclespawn = null;
                    byte b0 = 63;

                    if (this.myEntity instanceof EntitySmallFireball)
                    {
                        b0 = 64;
                    }
                    else if (this.myEntity instanceof EntityWitherSkull)
                    {
                        b0 = 66;
                    }

                    if (entityfireball.shootingEntity != null)
                    {
                        packet23vehiclespawn = new Packet23VehicleSpawn(this.myEntity, b0, ((EntityFireball)this.myEntity).shootingEntity.entityId);
                    }
                    else
                    {
                        packet23vehiclespawn = new Packet23VehicleSpawn(this.myEntity, b0, 0);
                    }

                    packet23vehiclespawn.speedX = (int)(entityfireball.accelerationX * 8000.0D);
                    packet23vehiclespawn.speedY = (int)(entityfireball.accelerationY * 8000.0D);
                    packet23vehiclespawn.speedZ = (int)(entityfireball.accelerationZ * 8000.0D);
                    return packet23vehiclespawn;
                }
                else if (this.myEntity instanceof EntityEgg)
                {
                    return new Packet23VehicleSpawn(this.myEntity, 62);
                }
                else if (this.myEntity instanceof EntityTNTPrimed)
                {
                    return new Packet23VehicleSpawn(this.myEntity, 50);
                }
                else if (this.myEntity instanceof EntityEnderCrystal)
                {
                    return new Packet23VehicleSpawn(this.myEntity, 51);
                }
                else if (this.myEntity instanceof EntityFallingSand)
                {
                    EntityFallingSand entityfallingsand = (EntityFallingSand)this.myEntity;
                    return new Packet23VehicleSpawn(this.myEntity, 70, entityfallingsand.blockID | entityfallingsand.metadata << 16);
                }
                else if (this.myEntity instanceof EntityPainting)
                {
                    return new Packet25EntityPainting((EntityPainting)this.myEntity);
                }
                else if (this.myEntity instanceof EntityItemFrame)
                {
                    EntityItemFrame entityitemframe = (EntityItemFrame)this.myEntity;
                    packet23vehiclespawn = new Packet23VehicleSpawn(this.myEntity, 71, entityitemframe.hangingDirection);
                    packet23vehiclespawn.xPosition = MathHelper.floor_float((float)(entityitemframe.xPosition * 32));
                    packet23vehiclespawn.yPosition = MathHelper.floor_float((float)(entityitemframe.yPosition * 32));
                    packet23vehiclespawn.zPosition = MathHelper.floor_float((float)(entityitemframe.zPosition * 32));
                    return packet23vehiclespawn;
                }
                else if (this.myEntity instanceof EntityLeashKnot)
                {
                    EntityLeashKnot entityleashknot = (EntityLeashKnot)this.myEntity;
                    packet23vehiclespawn = new Packet23VehicleSpawn(this.myEntity, 77);
                    packet23vehiclespawn.xPosition = MathHelper.floor_float((float)(entityleashknot.xPosition * 32));
                    packet23vehiclespawn.yPosition = MathHelper.floor_float((float)(entityleashknot.yPosition * 32));
                    packet23vehiclespawn.zPosition = MathHelper.floor_float((float)(entityleashknot.zPosition * 32));
                    return packet23vehiclespawn;
                }
                else if (this.myEntity instanceof EntityXPOrb)
                {
                    return new Packet26EntityExpOrb((EntityXPOrb)this.myEntity);
                }
                else
                {
                    throw new IllegalArgumentException("Don\'t know how to add " + this.myEntity.getClass() + "!");
                }
            }
        }
        else
        {
            this.lastHeadMotion = MathHelper.floor_float(this.myEntity.getRotationYawHead() * 256.0F / 360.0F);
            return new Packet24MobSpawn((EntityLivingBase)this.myEntity);
        }
    }

    public void removePlayerFromTracker(EntityPlayerMP par1EntityPlayerMP)
    {
        if (this.trackingPlayers.contains(par1EntityPlayerMP))
        {
            this.trackingPlayers.remove(par1EntityPlayerMP);
            par1EntityPlayerMP.destroyedItemsNetCache.add(Integer.valueOf(this.myEntity.entityId));
        }
    }
}
