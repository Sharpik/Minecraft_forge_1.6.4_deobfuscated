package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
class GuiCreateFlatWorldListSlot extends GuiSlot
{
    public int field_82454_a;

    final GuiCreateFlatWorld createFlatWorldGui;

    public GuiCreateFlatWorldListSlot(GuiCreateFlatWorld par1GuiCreateFlatWorld)
    {
        super(par1GuiCreateFlatWorld.mc, par1GuiCreateFlatWorld.width, par1GuiCreateFlatWorld.height, 43, par1GuiCreateFlatWorld.height - 60, 24);
        this.createFlatWorldGui = par1GuiCreateFlatWorld;
        this.field_82454_a = -1;
    }

    private void func_82452_a(int par1, int par2, ItemStack par3ItemStack)
    {
        this.func_82451_d(par1 + 1, par2 + 1);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if (par3ItemStack != null)
        {
            RenderHelper.enableGUIStandardItemLighting();
            GuiCreateFlatWorld.getRenderItem().renderItemIntoGUI(this.createFlatWorldGui.fontRenderer, this.createFlatWorldGui.mc.getTextureManager(), par3ItemStack, par1 + 2, par2 + 2);
            RenderHelper.disableStandardItemLighting();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }

    private void func_82451_d(int par1, int par2)
    {
        this.func_82450_b(par1, par2, 0, 0);
    }

    private void func_82450_b(int par1, int par2, int par3, int par4)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.createFlatWorldGui.mc.getTextureManager().bindTexture(Gui.statIcons);
        float f = 0.0078125F;
        float f1 = 0.0078125F;
        boolean flag = true;
        boolean flag1 = true;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 18), (double)this.createFlatWorldGui.zLevel, (double)((float)(par3 + 0) * 0.0078125F), (double)((float)(par4 + 18) * 0.0078125F));
        tessellator.addVertexWithUV((double)(par1 + 18), (double)(par2 + 18), (double)this.createFlatWorldGui.zLevel, (double)((float)(par3 + 18) * 0.0078125F), (double)((float)(par4 + 18) * 0.0078125F));
        tessellator.addVertexWithUV((double)(par1 + 18), (double)(par2 + 0), (double)this.createFlatWorldGui.zLevel, (double)((float)(par3 + 18) * 0.0078125F), (double)((float)(par4 + 0) * 0.0078125F));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.createFlatWorldGui.zLevel, (double)((float)(par3 + 0) * 0.0078125F), (double)((float)(par4 + 0) * 0.0078125F));
        tessellator.draw();
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers().size();
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
        this.field_82454_a = par1;
        this.createFlatWorldGui.func_82270_g();
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return par1 == this.field_82454_a;
    }

    protected void drawBackground() {}

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        FlatLayerInfo flatlayerinfo = (FlatLayerInfo)GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers().get(GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers().size() - par1 - 1);
        ItemStack itemstack = flatlayerinfo.getFillBlock() == 0 ? null : new ItemStack(flatlayerinfo.getFillBlock(), 1, flatlayerinfo.getFillBlockMeta());
        String s = itemstack == null ? "Air" : Item.itemsList[flatlayerinfo.getFillBlock()].getItemStackDisplayName(itemstack);
        this.func_82452_a(par2, par3, itemstack);
        this.createFlatWorldGui.fontRenderer.drawString(s, par2 + 18 + 5, par3 + 3, 16777215);
        String s1;

        if (par1 == 0)
        {
            s1 = I18n.getStringParams("createWorld.customize.flat.layer.top", new Object[] {Integer.valueOf(flatlayerinfo.getLayerCount())});
        }
        else if (par1 == GuiCreateFlatWorld.func_82271_a(this.createFlatWorldGui).getFlatLayers().size() - 1)
        {
            s1 = I18n.getStringParams("createWorld.customize.flat.layer.bottom", new Object[] {Integer.valueOf(flatlayerinfo.getLayerCount())});
        }
        else
        {
            s1 = I18n.getStringParams("createWorld.customize.flat.layer", new Object[] {Integer.valueOf(flatlayerinfo.getLayerCount())});
        }

        this.createFlatWorldGui.fontRenderer.drawString(s1, par2 + 2 + 213 - this.createFlatWorldGui.fontRenderer.getStringWidth(s1), par3 + 3, 16777215);
    }

    protected int getScrollBarX()
    {
        return this.createFlatWorldGui.width - 70;
    }
}
