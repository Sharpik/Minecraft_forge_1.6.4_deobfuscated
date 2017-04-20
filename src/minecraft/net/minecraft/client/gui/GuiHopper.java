package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiHopper extends GuiContainer
{
    private static final ResourceLocation hopperGuiTextures = new ResourceLocation("textures/gui/container/hopper.png");
    private IInventory field_94081_r;
    private IInventory field_94080_s;

    public GuiHopper(InventoryPlayer par1InventoryPlayer, IInventory par2IInventory)
    {
        super(new ContainerHopper(par1InventoryPlayer, par2IInventory));
        this.field_94081_r = par1InventoryPlayer;
        this.field_94080_s = par2IInventory;
        this.allowUserInput = false;
        this.ySize = 133;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.field_94080_s.isInvNameLocalized() ? this.field_94080_s.getInvName() : I18n.getString(this.field_94080_s.getInvName()), 8, 6, 4210752);
        this.fontRenderer.drawString(this.field_94081_r.isInvNameLocalized() ? this.field_94081_r.getInvName() : I18n.getString(this.field_94081_r.getInvName()), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(hopperGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
