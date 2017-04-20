package net.minecraft.client.gui.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBeacon extends GuiContainer
{
    private static final ResourceLocation beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
    private TileEntityBeacon beacon;
    private GuiBeaconButtonConfirm beaconConfirmButton;
    private boolean buttonsNotDrawn;

    public GuiBeacon(InventoryPlayer par1InventoryPlayer, TileEntityBeacon par2TileEntityBeacon)
    {
        super(new ContainerBeacon(par1InventoryPlayer, par2TileEntityBeacon));
        this.beacon = par2TileEntityBeacon;
        this.xSize = 230;
        this.ySize = 219;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(this.beaconConfirmButton = new GuiBeaconButtonConfirm(this, -1, this.guiLeft + 164, this.guiTop + 107));
        this.buttonList.add(new GuiBeaconButtonCancel(this, -2, this.guiLeft + 190, this.guiTop + 107));
        this.buttonsNotDrawn = true;
        this.beaconConfirmButton.enabled = false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (this.buttonsNotDrawn && this.beacon.getLevels() >= 0)
        {
            this.buttonsNotDrawn = false;
            int i;
            int j;
            int k;
            int l;
            GuiBeaconButtonPower guibeaconbuttonpower;

            for (int i1 = 0; i1 <= 2; ++i1)
            {
                i = TileEntityBeacon.effectsList[i1].length;
                j = i * 22 + (i - 1) * 2;

                for (k = 0; k < i; ++k)
                {
                    l = TileEntityBeacon.effectsList[i1][k].id;
                    guibeaconbuttonpower = new GuiBeaconButtonPower(this, i1 << 8 | l, this.guiLeft + 76 + k * 24 - j / 2, this.guiTop + 22 + i1 * 25, l, i1);
                    this.buttonList.add(guibeaconbuttonpower);

                    if (i1 >= this.beacon.getLevels())
                    {
                        guibeaconbuttonpower.enabled = false;
                    }
                    else if (l == this.beacon.getPrimaryEffect())
                    {
                        guibeaconbuttonpower.func_82254_b(true);
                    }
                }
            }

            byte b0 = 3;
            i = TileEntityBeacon.effectsList[b0].length + 1;
            j = i * 22 + (i - 1) * 2;

            for (k = 0; k < i - 1; ++k)
            {
                l = TileEntityBeacon.effectsList[b0][k].id;
                guibeaconbuttonpower = new GuiBeaconButtonPower(this, b0 << 8 | l, this.guiLeft + 167 + k * 24 - j / 2, this.guiTop + 47, l, b0);
                this.buttonList.add(guibeaconbuttonpower);

                if (b0 >= this.beacon.getLevels())
                {
                    guibeaconbuttonpower.enabled = false;
                }
                else if (l == this.beacon.getSecondaryEffect())
                {
                    guibeaconbuttonpower.func_82254_b(true);
                }
            }

            if (this.beacon.getPrimaryEffect() > 0)
            {
                GuiBeaconButtonPower guibeaconbuttonpower1 = new GuiBeaconButtonPower(this, b0 << 8 | this.beacon.getPrimaryEffect(), this.guiLeft + 167 + (i - 1) * 24 - j / 2, this.guiTop + 47, this.beacon.getPrimaryEffect(), b0);
                this.buttonList.add(guibeaconbuttonpower1);

                if (b0 >= this.beacon.getLevels())
                {
                    guibeaconbuttonpower1.enabled = false;
                }
                else if (this.beacon.getPrimaryEffect() == this.beacon.getSecondaryEffect())
                {
                    guibeaconbuttonpower1.func_82254_b(true);
                }
            }
        }

        this.beaconConfirmButton.enabled = this.beacon.getStackInSlot(0) != null && this.beacon.getPrimaryEffect() > 0;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == -2)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (par1GuiButton.id == -1)
        {
            String s = "MC|Beacon";
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try
            {
                dataoutputstream.writeInt(this.beacon.getPrimaryEffect());
                dataoutputstream.writeInt(this.beacon.getSecondaryEffect());
                this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(s, bytearrayoutputstream.toByteArray()));
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (par1GuiButton instanceof GuiBeaconButtonPower)
        {
            if (((GuiBeaconButtonPower)par1GuiButton).func_82255_b())
            {
                return;
            }

            int i = par1GuiButton.id;
            int j = i & 255;
            int k = i >> 8;

            if (k < 3)
            {
                this.beacon.setPrimaryEffect(j);
            }
            else
            {
                this.beacon.setSecondaryEffect(j);
            }

            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRenderer, I18n.getString("tile.beacon.primary"), 62, 10, 14737632);
        this.drawCenteredString(this.fontRenderer, I18n.getString("tile.beacon.secondary"), 169, 10, 14737632);
        Iterator iterator = this.buttonList.iterator();

        while (iterator.hasNext())
        {
            GuiButton guibutton = (GuiButton)iterator.next();

            if (guibutton.func_82252_a())
            {
                guibutton.func_82251_b(par1 - this.guiLeft, par2 - this.guiTop);
                break;
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(beaconGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        itemRenderer.zLevel = 100.0F;
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.getTextureManager(), new ItemStack(Item.emerald), k + 42, l + 109);
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.getTextureManager(), new ItemStack(Item.diamond), k + 42 + 22, l + 109);
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.getTextureManager(), new ItemStack(Item.ingotGold), k + 42 + 44, l + 109);
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.getTextureManager(), new ItemStack(Item.ingotIron), k + 42 + 66, l + 109);
        itemRenderer.zLevel = 0.0F;
    }

    static ResourceLocation getBeaconGuiTextures()
    {
        return beaconGuiTextures;
    }
}
