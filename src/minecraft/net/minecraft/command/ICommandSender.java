package net.minecraft.command;

import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public interface ICommandSender
{
    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    String getCommandSenderName();

    void sendChatToPlayer(ChatMessageComponent chatmessagecomponent);

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    boolean canCommandSenderUseCommand(int i, String s);

    /**
     * Return the position for this command sender.
     */
    ChunkCoordinates getPlayerCoordinates();

    World getEntityWorld();
}
