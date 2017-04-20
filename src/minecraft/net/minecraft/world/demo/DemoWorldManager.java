package net.minecraft.world.demo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;

public class DemoWorldManager extends ItemInWorldManager
{
    private boolean field_73105_c;
    private boolean demoTimeExpired;
    private int field_73104_e;
    private int field_73102_f;

    public DemoWorldManager(World par1World)
    {
        super(par1World);
    }

    public void updateBlockRemoving()
    {
        super.updateBlockRemoving();
        ++this.field_73102_f;
        long i = this.theWorld.getTotalWorldTime();
        long j = i / 24000L + 1L;

        if (!this.field_73105_c && this.field_73102_f > 20)
        {
            this.field_73105_c = true;
            this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 0));
        }

        this.demoTimeExpired = i > 120500L;

        if (this.demoTimeExpired)
        {
            ++this.field_73104_e;
        }

        if (i % 24000L == 500L)
        {
            if (j <= 6L)
            {
                this.thisPlayerMP.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("demo.day." + j));
            }
        }
        else if (j == 1L)
        {
            if (i == 100L)
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 101));
            }
            else if (i == 175L)
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 102));
            }
            else if (i == 250L)
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(5, 103));
            }
        }
        else if (j == 5L && i % 24000L == 22000L)
        {
            this.thisPlayerMP.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("demo.day.warning"));
        }
    }

    /**
     * Sends a message to the player reminding them that this is the demo version
     */
    private void sendDemoReminder()
    {
        if (this.field_73104_e > 100)
        {
            this.thisPlayerMP.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("demo.reminder"));
            this.field_73104_e = 0;
        }
    }

    /**
     * if not creative, it calls destroyBlockInWorldPartially untill the block is broken first. par4 is the specific
     * side. tryHarvestBlock can also be the result of this call
     */
    public void onBlockClicked(int par1, int par2, int par3, int par4)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
        }
        else
        {
            super.onBlockClicked(par1, par2, par3, par4);
        }
    }

    public void uncheckedTryHarvestBlock(int par1, int par2, int par3)
    {
        if (!this.demoTimeExpired)
        {
            super.uncheckedTryHarvestBlock(par1, par2, par3);
        }
    }

    /**
     * Attempts to harvest a block at the given coordinate
     */
    public boolean tryHarvestBlock(int par1, int par2, int par3)
    {
        return this.demoTimeExpired ? false : super.tryHarvestBlock(par1, par2, par3);
    }

    /**
     * Attempts to right-click use an item by the given EntityPlayer in the given World
     */
    public boolean tryUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
            return false;
        }
        else
        {
            return super.tryUseItem(par1EntityPlayer, par2World, par3ItemStack);
        }
    }

    /**
     * Activate the clicked on block, otherwise use the held item. Args: player, world, itemStack, x, y, z, side,
     * xOffset, yOffset, zOffset
     */
    public boolean activateBlockOrUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (this.demoTimeExpired)
        {
            this.sendDemoReminder();
            return false;
        }
        else
        {
            return super.activateBlockOrUseItem(par1EntityPlayer, par2World, par3ItemStack, par4, par5, par6, par7, par8, par9, par10);
        }
    }
}
