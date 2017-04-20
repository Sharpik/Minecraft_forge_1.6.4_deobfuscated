package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenBook extends GuiScreen
{
    private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");

    /** The player editing the book */
    private final EntityPlayer editingPlayer;
    private final ItemStack itemstackBook;

    /** Whether the book is signed or can still be edited */
    private final boolean bookIsUnsigned;
    private boolean bookModified;
    private boolean editingTitle;

    /** Update ticks since the gui was opened */
    private int updateCount;
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private String bookTitle = "";
    private GuiButtonNextPage buttonNextPage;
    private GuiButtonNextPage buttonPreviousPage;
    private GuiButton buttonDone;

    /** The GuiButton to sign this book. */
    private GuiButton buttonSign;
    private GuiButton buttonFinalize;
    private GuiButton buttonCancel;

    public GuiScreenBook(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, boolean par3)
    {
        this.editingPlayer = par1EntityPlayer;
        this.itemstackBook = par2ItemStack;
        this.bookIsUnsigned = par3;

        if (par2ItemStack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = par2ItemStack.getTagCompound();
            this.bookPages = nbttagcompound.getTagList("pages");

            if (this.bookPages != null)
            {
                this.bookPages = (NBTTagList)this.bookPages.copy();
                this.bookTotalPages = this.bookPages.tagCount();

                if (this.bookTotalPages < 1)
                {
                    this.bookTotalPages = 1;
                }
            }
        }

        if (this.bookPages == null && par3)
        {
            this.bookPages = new NBTTagList("pages");
            this.bookPages.appendTag(new NBTTagString("1", ""));
            this.bookTotalPages = 1;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.updateCount;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        if (this.bookIsUnsigned)
        {
            this.buttonList.add(this.buttonSign = new GuiButton(3, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.getString("book.signButton")));
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.getString("gui.done")));
            this.buttonList.add(this.buttonFinalize = new GuiButton(5, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.getString("book.finalizeButton")));
            this.buttonList.add(this.buttonCancel = new GuiButton(4, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.getString("gui.cancel")));
        }
        else
        {
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.getString("gui.done")));
        }

        int i = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, i + 120, b0 + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, i + 38, b0 + 154, false));
        this.updateButtons();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons()
    {
        this.buttonNextPage.drawButton = !this.editingTitle && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
        this.buttonPreviousPage.drawButton = !this.editingTitle && this.currPage > 0;
        this.buttonDone.drawButton = !this.bookIsUnsigned || !this.editingTitle;

        if (this.bookIsUnsigned)
        {
            this.buttonSign.drawButton = !this.editingTitle;
            this.buttonCancel.drawButton = this.editingTitle;
            this.buttonFinalize.drawButton = this.editingTitle;
            this.buttonFinalize.enabled = this.bookTitle.trim().length() > 0;
        }
    }

    private void sendBookToServer(boolean par1)
    {
        if (this.bookIsUnsigned && this.bookModified)
        {
            if (this.bookPages != null)
            {
                while (this.bookPages.tagCount() > 1)
                {
                    NBTTagString nbttagstring = (NBTTagString)this.bookPages.tagAt(this.bookPages.tagCount() - 1);

                    if (nbttagstring.data != null && nbttagstring.data.length() != 0)
                    {
                        break;
                    }

                    this.bookPages.removeTag(this.bookPages.tagCount() - 1);
                }

                if (this.itemstackBook.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = this.itemstackBook.getTagCompound();
                    nbttagcompound.setTag("pages", this.bookPages);
                }
                else
                {
                    this.itemstackBook.setTagInfo("pages", this.bookPages);
                }

                String s = "MC|BEdit";

                if (par1)
                {
                    s = "MC|BSign";
                    this.itemstackBook.setTagInfo("author", new NBTTagString("author", this.editingPlayer.getCommandSenderName()));
                    this.itemstackBook.setTagInfo("title", new NBTTagString("title", this.bookTitle.trim()));
                    this.itemstackBook.itemID = Item.writtenBook.itemID;
                }

                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

                try
                {
                    Packet.writeItemStack(this.itemstackBook, dataoutputstream);
                    this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload(s, bytearrayoutputstream.toByteArray()));
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.sendBookToServer(false);
            }
            else if (par1GuiButton.id == 3 && this.bookIsUnsigned)
            {
                this.editingTitle = true;
            }
            else if (par1GuiButton.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
                else if (this.bookIsUnsigned)
                {
                    this.addNewPage();

                    if (this.currPage < this.bookTotalPages - 1)
                    {
                        ++this.currPage;
                    }
                }
            }
            else if (par1GuiButton.id == 2)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }
            else if (par1GuiButton.id == 5 && this.editingTitle)
            {
                this.sendBookToServer(true);
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (par1GuiButton.id == 4 && this.editingTitle)
            {
                this.editingTitle = false;
            }

            this.updateButtons();
        }
    }

    private void addNewPage()
    {
        if (this.bookPages != null && this.bookPages.tagCount() < 50)
        {
            this.bookPages.appendTag(new NBTTagString("" + (this.bookTotalPages + 1), ""));
            ++this.bookTotalPages;
            this.bookModified = true;
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);

        if (this.bookIsUnsigned)
        {
            if (this.editingTitle)
            {
                this.func_74162_c(par1, par2);
            }
            else
            {
                this.keyTypedInBook(par1, par2);
            }
        }
    }

    /**
     * Processes keystrokes when editing the text of a book
     */
    private void keyTypedInBook(char par1, int par2)
    {
        switch (par1)
        {
            case 22:
                this.func_74160_b(GuiScreen.getClipboardString());
                return;
            default:
                switch (par2)
                {
                    case 14:
                        String s = this.func_74158_i();

                        if (s.length() > 0)
                        {
                            this.func_74159_a(s.substring(0, s.length() - 1));
                        }

                        return;
                    case 28:
                    case 156:
                        this.func_74160_b("\n");
                        return;
                    default:
                        if (ChatAllowedCharacters.isAllowedCharacter(par1))
                        {
                            this.func_74160_b(Character.toString(par1));
                        }
                }
        }
    }

    private void func_74162_c(char par1, int par2)
    {
        switch (par2)
        {
            case 14:
                if (!this.bookTitle.isEmpty())
                {
                    this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
                    this.updateButtons();
                }

                return;
            case 28:
            case 156:
                if (!this.bookTitle.isEmpty())
                {
                    this.sendBookToServer(true);
                    this.mc.displayGuiScreen((GuiScreen)null);
                }

                return;
            default:
                if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(par1))
                {
                    this.bookTitle = this.bookTitle + Character.toString(par1);
                    this.updateButtons();
                    this.bookModified = true;
                }
        }
    }

    private String func_74158_i()
    {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        {
            NBTTagString nbttagstring = (NBTTagString)this.bookPages.tagAt(this.currPage);
            return nbttagstring.toString();
        }
        else
        {
            return "";
        }
    }

    private void func_74159_a(String par1Str)
    {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        {
            NBTTagString nbttagstring = (NBTTagString)this.bookPages.tagAt(this.currPage);
            nbttagstring.data = par1Str;
            this.bookModified = true;
        }
    }

    private void func_74160_b(String par1Str)
    {
        String s1 = this.func_74158_i();
        String s2 = s1 + par1Str;
        int i = this.fontRenderer.splitStringWidth(s2 + "" + EnumChatFormatting.BLACK + "_", 118);

        if (i <= 118 && s2.length() < 256)
        {
            this.func_74159_a(s2);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookGuiTextures);
        int k = (this.width - this.bookImageWidth) / 2;
        byte b0 = 2;
        this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
        String s;
        String s1;
        int l;

        if (this.editingTitle)
        {
            s = this.bookTitle;

            if (this.bookIsUnsigned)
            {
                if (this.updateCount / 6 % 2 == 0)
                {
                    s = s + "" + EnumChatFormatting.BLACK + "_";
                }
                else
                {
                    s = s + "" + EnumChatFormatting.GRAY + "_";
                }
            }

            s1 = I18n.getString("book.editTitle");
            l = this.fontRenderer.getStringWidth(s1);
            this.fontRenderer.drawString(s1, k + 36 + (116 - l) / 2, b0 + 16 + 16, 0);
            int i1 = this.fontRenderer.getStringWidth(s);
            this.fontRenderer.drawString(s, k + 36 + (116 - i1) / 2, b0 + 48, 0);
            String s2 = String.format(I18n.getString("book.byAuthor"), new Object[] {this.editingPlayer.getCommandSenderName()});
            int j1 = this.fontRenderer.getStringWidth(s2);
            this.fontRenderer.drawString(EnumChatFormatting.DARK_GRAY + s2, k + 36 + (116 - j1) / 2, b0 + 48 + 10, 0);
            String s3 = I18n.getString("book.finalizeWarning");
            this.fontRenderer.drawSplitString(s3, k + 36, b0 + 80, 116, 0);
        }
        else
        {
            s = String.format(I18n.getString("book.pageIndicator"), new Object[] {Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
            s1 = "";

            if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
            {
                NBTTagString nbttagstring = (NBTTagString)this.bookPages.tagAt(this.currPage);
                s1 = nbttagstring.toString();
            }

            if (this.bookIsUnsigned)
            {
                if (this.fontRenderer.getBidiFlag())
                {
                    s1 = s1 + "_";
                }
                else if (this.updateCount / 6 % 2 == 0)
                {
                    s1 = s1 + "" + EnumChatFormatting.BLACK + "_";
                }
                else
                {
                    s1 = s1 + "" + EnumChatFormatting.GRAY + "_";
                }
            }

            l = this.fontRenderer.getStringWidth(s);
            this.fontRenderer.drawString(s, k - l + this.bookImageWidth - 44, b0 + 16, 0);
            this.fontRenderer.drawSplitString(s1, k + 36, b0 + 16 + 16, 116, 0);
        }

        super.drawScreen(par1, par2, par3);
    }

    static ResourceLocation func_110404_g()
    {
        return bookGuiTextures;
    }
}
