package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.packet.Packet203AutoComplete;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import com.google.common.collect.ObjectArrays;

@SideOnly(Side.CLIENT)
public class GuiChat extends GuiScreen
{
    private String field_73898_b = "";

    /**
     * keeps position of which chat message you will select when you press up, (does not increase for duplicated
     * messages sent immediately after each other)
     */
    private int sentHistoryCursor = -1;
    private boolean field_73897_d;
    private boolean field_73905_m;
    private int field_73903_n;
    private List field_73904_o = new ArrayList();

    /** used to pass around the URI to various dialogues and to the host os */
    private URI clickedURI;

    /** Chat entry field */
    protected GuiTextField inputField;

    /**
     * is the text that appears when you press the chat key and the input box appears pre-filled
     */
    private String defaultInputFieldText = "";

    public GuiChat() {}

    public GuiChat(String par1Str)
    {
        this.defaultInputFieldText = par1Str;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(this.fontRenderer, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.inputField.updateCursorCounter();
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        this.field_73905_m = false;

        if (par2 == 15)
        {
            this.completePlayerName();
        }
        else
        {
            this.field_73897_d = false;
        }

        if (par2 == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (par2 != 28 && par2 != 156)
        {
            if (par2 == 200)
            {
                this.getSentHistory(-1);
            }
            else if (par2 == 208)
            {
                this.getSentHistory(1);
            }
            else if (par2 == 201)
            {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().func_96127_i() - 1);
            }
            else if (par2 == 209)
            {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().func_96127_i() + 1);
            }
            else
            {
                this.inputField.textboxKeyTyped(par1, par2);
            }
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (s.length() > 0)
            {
                this.mc.ingameGUI.getChatGUI().addToSentMessages(s);

                if (!this.mc.handleClientCommand(s))
                {
                    this.mc.thePlayer.sendChatMessage(s);
                }
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 1)
            {
                i = 1;
            }

            if (i < -1)
            {
                i = -1;
            }

            if (!isShiftKeyDown())
            {
                i *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0 && this.mc.gameSettings.chatLinks)
        {
            ChatClickData chatclickdata = this.mc.ingameGUI.getChatGUI().func_73766_a(Mouse.getX(), Mouse.getY());

            if (chatclickdata != null)
            {
                URI uri = chatclickdata.getURI();

                if (uri != null)
                {
                    if (this.mc.gameSettings.chatLinksPrompt)
                    {
                        this.clickedURI = uri;
                        this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, chatclickdata.getClickedUrl(), 0, false));
                    }
                    else
                    {
                        this.func_73896_a(uri);
                    }

                    return;
                }
            }
        }

        this.inputField.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (par2 == 0)
        {
            if (par1)
            {
                this.func_73896_a(this.clickedURI);
            }

            this.clickedURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void func_73896_a(URI par1URI)
    {
        try
        {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {par1URI});
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    /**
     * Autocompletes player name
     */
    public void completePlayerName()
    {
        String s;

        if (this.field_73897_d)
        {
            this.inputField.deleteFromCursor(this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

            if (this.field_73903_n >= this.field_73904_o.size())
            {
                this.field_73903_n = 0;
            }
        }
        else
        {
            int i = this.inputField.func_73798_a(-1, this.inputField.getCursorPosition(), false);
            this.field_73904_o.clear();
            this.field_73903_n = 0;
            String s1 = this.inputField.getText().substring(i).toLowerCase();
            s = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.func_73893_a(s, s1);

            if (this.field_73904_o.isEmpty())
            {
                return;
            }

            this.field_73897_d = true;
            this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
        }

        if (this.field_73904_o.size() > 1)
        {
            StringBuilder stringbuilder = new StringBuilder();

            for (Iterator iterator = this.field_73904_o.iterator(); iterator.hasNext(); stringbuilder.append(s))
            {
                s = (String)iterator.next();

                if (stringbuilder.length() > 0)
                {
                    stringbuilder.append(", ");
                }
            }

            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(stringbuilder.toString(), 1);
        }

        this.inputField.writeText(EnumChatFormatting.func_110646_a((String)this.field_73904_o.get(this.field_73903_n++)));
    }

    private void func_73893_a(String par1Str, String par2Str)
    {
        if (par1Str.length() >= 1)
        {
            ClientCommandHandler.instance.autoComplete(par1Str, par2Str);
            this.mc.thePlayer.sendQueue.addToSendQueue(new Packet203AutoComplete(par1Str));
            this.field_73905_m = true;
        }
    }

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int par1)
    {
        int j = this.sentHistoryCursor + par1;
        int k = this.mc.ingameGUI.getChatGUI().getSentMessages().size();

        if (j < 0)
        {
            j = 0;
        }

        if (j > k)
        {
            j = k;
        }

        if (j != this.sentHistoryCursor)
        {
            if (j == k)
            {
                this.sentHistoryCursor = k;
                this.inputField.setText(this.field_73898_b);
            }
            else
            {
                if (this.sentHistoryCursor == k)
                {
                    this.field_73898_b = this.inputField.getText();
                }

                this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(j));
                this.sentHistoryCursor = j;
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    public void func_73894_a(String[] par1ArrayOfStr)
    {
        if (this.field_73905_m)
        {
            this.field_73904_o.clear();
            String[] astring1 = par1ArrayOfStr;
            int i = par1ArrayOfStr.length;

            String[] complete = ClientCommandHandler.instance.latestAutoComplete;
            if (complete != null)
            {
                astring1 = ObjectArrays.concat(complete, astring1, String.class);
                i = astring1.length;
            }

            for (int j = 0; j < i; ++j)
            {
                String s = astring1[j];

                if (s.length() > 0)
                {
                    this.field_73904_o.add(s);
                }
            }

            if (this.field_73904_o.size() > 0)
            {
                this.field_73897_d = true;
                this.completePlayerName();
            }
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
