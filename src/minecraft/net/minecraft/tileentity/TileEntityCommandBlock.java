package net.minecraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity implements ICommandSender
{
    private int succesCount;

    /** The command this block will execute when powered. */
    private String command = "";

    /** The name of command sender (usually username, but possibly "Rcon") */
    private String commandSenderName = "@";

    /**
     * Sets the command this block will execute when powered.
     */
    public void setCommand(String par1Str)
    {
        this.command = par1Str;
        this.onInventoryChanged();
    }

    /**
     * Execute the command, called when the command block is powered.
     */
    public int executeCommandOnPowered(World par1World)
    {
        if (par1World.isRemote)
        {
            return 0;
        }
        else
        {
            MinecraftServer minecraftserver = MinecraftServer.getServer();

            if (minecraftserver != null && minecraftserver.isCommandBlockEnabled())
            {
                ICommandManager icommandmanager = minecraftserver.getCommandManager();
                return icommandmanager.executeCommand(this, this.command);
            }
            else
            {
                return 0;
            }
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return the command this command block is set to execute.
     */
    public String getCommand()
    {
        return this.command;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return this.commandSenderName;
    }

    /**
     * Sets the name of the command sender
     */
    public void setCommandSenderName(String par1Str)
    {
        this.commandSenderName = par1Str;
    }

    public void sendChatToPlayer(ChatMessageComponent par1ChatMessageComponent) {}

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return par1 <= 2;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("Command", this.command);
        par1NBTTagCompound.setInteger("SuccessCount", this.succesCount);
        par1NBTTagCompound.setString("CustomName", this.commandSenderName);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.command = par1NBTTagCompound.getString("Command");
        this.succesCount = par1NBTTagCompound.getInteger("SuccessCount");

        if (par1NBTTagCompound.hasKey("CustomName"))
        {
            this.commandSenderName = par1NBTTagCompound.getString("CustomName");
        }
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
    }

    public World getEntityWorld()
    {
        return this.getWorldObj();
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 2, nbttagcompound);
    }

    public int getSignalStrength()
    {
        return this.succesCount;
    }

    public void setSignalStrength(int par1)
    {
        this.succesCount = par1;
    }
}
