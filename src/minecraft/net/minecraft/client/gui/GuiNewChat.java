package net.minecraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiNewChat extends Gui
{
    /** The Minecraft instance. */
    private final Minecraft mc;

    /** A list of messages previously sent through the chat GUI */
    private final List sentMessages = new ArrayList();

    /** Chat lines to be displayed in the chat box */
    private final List chatLines = new ArrayList();
    private final List field_96134_d = new ArrayList();
    private int field_73768_d;
    private boolean field_73769_e;

    public GuiNewChat(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    public void drawChat(int par1)
    {
        if (this.mc.gameSettings.chatVisibility != 2)
        {
            int j = this.func_96127_i();
            boolean flag = false;
            int k = 0;
            int l = this.field_96134_d.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (l > 0)
            {
                if (this.getChatOpen())
                {
                    flag = true;
                }

                float f1 = this.func_96131_h();
                int i1 = MathHelper.ceiling_float_int((float)this.func_96126_f() / f1);
                GL11.glPushMatrix();
                GL11.glTranslatef(2.0F, 20.0F, 0.0F);
                GL11.glScalef(f1, f1, 1.0F);
                int j1;
                int k1;
                int l1;

                for (j1 = 0; j1 + this.field_73768_d < this.field_96134_d.size() && j1 < j; ++j1)
                {
                    ChatLine chatline = (ChatLine)this.field_96134_d.get(j1 + this.field_73768_d);

                    if (chatline != null)
                    {
                        k1 = par1 - chatline.getUpdatedCounter();

                        if (k1 < 200 || flag)
                        {
                            double d0 = (double)k1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 *= 10.0D;

                            if (d0 < 0.0D)
                            {
                                d0 = 0.0D;
                            }

                            if (d0 > 1.0D)
                            {
                                d0 = 1.0D;
                            }

                            d0 *= d0;
                            l1 = (int)(255.0D * d0);

                            if (flag)
                            {
                                l1 = 255;
                            }

                            l1 = (int)((float)l1 * f);
                            ++k;

                            if (l1 > 3)
                            {
                                byte b0 = 0;
                                int i2 = -j1 * 9;
                                drawRect(b0, i2 - 9, b0 + i1 + 4, i2, l1 / 2 << 24);
                                GL11.glEnable(GL11.GL_BLEND);
                                String s = chatline.getChatLineString();

                                if (!this.mc.gameSettings.chatColours)
                                {
                                    s = StringUtils.stripControlCodes(s);
                                }

                                this.mc.fontRenderer.drawStringWithShadow(s, b0, i2 - 8, 16777215 + (l1 << 24));
                            }
                        }
                    }
                }

                if (flag)
                {
                    j1 = this.mc.fontRenderer.FONT_HEIGHT;
                    GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
                    int j2 = l * j1 + l;
                    k1 = k * j1 + k;
                    int k2 = this.field_73768_d * k1 / l;
                    int l2 = k1 * k1 / j2;

                    if (j2 != k1)
                    {
                        l1 = k2 > 0 ? 170 : 96;
                        int i3 = this.field_73769_e ? 13382451 : 3355562;
                        drawRect(0, -k2, 2, -k2 - l2, i3 + (l1 << 24));
                        drawRect(2, -k2, 1, -k2 - l2, 13421772 + (l1 << 24));
                    }
                }

                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Clears the chat.
     */
    public void clearChatMessages()
    {
        this.field_96134_d.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }

    /**
     * takes a String and prints it to chat
     */
    public void printChatMessage(String par1Str)
    {
        this.printChatMessageWithOptionalDeletion(par1Str, 0);
    }

    /**
     * prints the String to Chat. If the ID is not 0, deletes an existing Chat Line of that ID from the GUI
     */
    public void printChatMessageWithOptionalDeletion(String par1Str, int par2)
    {
        this.func_96129_a(par1Str, par2, this.mc.ingameGUI.getUpdateCounter(), false);
        this.mc.getLogAgent().logInfo("[CHAT] " + EnumChatFormatting.func_110646_a(par1Str));
    }

    private void func_96129_a(String par1Str, int par2, int par3, boolean par4)
    {
        boolean flag1 = this.getChatOpen();
        boolean flag2 = true;

        if (par2 != 0)
        {
            this.deleteChatLine(par2);
        }

        Iterator iterator = this.mc.fontRenderer.listFormattedStringToWidth(par1Str, MathHelper.floor_float((float)this.func_96126_f() / this.func_96131_h())).iterator();

        while (iterator.hasNext())
        {
            String s1 = (String)iterator.next();

            if (flag1 && this.field_73768_d > 0)
            {
                this.field_73769_e = true;
                this.scroll(1);
            }

            if (!flag2)
            {
                s1 = " " + s1;
            }

            flag2 = false;
            this.field_96134_d.add(0, new ChatLine(par3, s1, par2));
        }

        while (this.field_96134_d.size() > 100)
        {
            this.field_96134_d.remove(this.field_96134_d.size() - 1);
        }

        if (!par4)
        {
            this.chatLines.add(0, new ChatLine(par3, par1Str.trim(), par2));

            while (this.chatLines.size() > 100)
            {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }

    public void func_96132_b()
    {
        this.field_96134_d.clear();
        this.resetScroll();

        for (int i = this.chatLines.size() - 1; i >= 0; --i)
        {
            ChatLine chatline = (ChatLine)this.chatLines.get(i);
            this.func_96129_a(chatline.getChatLineString(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    /**
     * Gets the list of messages previously sent through the chat GUI
     */
    public List getSentMessages()
    {
        return this.sentMessages;
    }

    /**
     * Adds this string to the list of sent messages, for recall using the up/down arrow keys
     */
    public void addToSentMessages(String par1Str)
    {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(par1Str))
        {
            this.sentMessages.add(par1Str);
        }
    }

    /**
     * Resets the chat scroll (executed when the GUI is closed)
     */
    public void resetScroll()
    {
        this.field_73768_d = 0;
        this.field_73769_e = false;
    }

    /**
     * Scrolls the chat by the given number of lines.
     */
    public void scroll(int par1)
    {
        this.field_73768_d += par1;
        int j = this.field_96134_d.size();

        if (this.field_73768_d > j - this.func_96127_i())
        {
            this.field_73768_d = j - this.func_96127_i();
        }

        if (this.field_73768_d <= 0)
        {
            this.field_73768_d = 0;
            this.field_73769_e = false;
        }
    }

    public ChatClickData func_73766_a(int par1, int par2)
    {
        if (!this.getChatOpen())
        {
            return null;
        }
        else
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int k = scaledresolution.getScaleFactor();
            float f = this.func_96131_h();
            int l = par1 / k - 3;
            int i1 = par2 / k - 25;
            l = MathHelper.floor_float((float)l / f);
            i1 = MathHelper.floor_float((float)i1 / f);

            if (l >= 0 && i1 >= 0)
            {
                int j1 = Math.min(this.func_96127_i(), this.field_96134_d.size());

                if (l <= MathHelper.floor_float((float)this.func_96126_f() / this.func_96131_h()) && i1 < this.mc.fontRenderer.FONT_HEIGHT * j1 + j1)
                {
                    int k1 = i1 / (this.mc.fontRenderer.FONT_HEIGHT + 1) + this.field_73768_d;
                    return new ChatClickData(this.mc.fontRenderer, (ChatLine)this.field_96134_d.get(k1), l, i1 - (k1 - this.field_73768_d) * this.mc.fontRenderer.FONT_HEIGHT + k1);
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Adds a message to the chat after translating to the client's locale.
     */
    public void addTranslatedMessage(String par1Str, Object ... par2ArrayOfObj)
    {
        this.printChatMessage(I18n.getStringParams(par1Str, par2ArrayOfObj));
    }

    /**
     * @return {@code true} if the chat GUI is open
     */
    public boolean getChatOpen()
    {
        return this.mc.currentScreen instanceof GuiChat;
    }

    /**
     * finds and deletes a Chat line by ID
     */
    public void deleteChatLine(int par1)
    {
        Iterator iterator = this.field_96134_d.iterator();
        ChatLine chatline;

        do
        {
            if (!iterator.hasNext())
            {
                iterator = this.chatLines.iterator();

                do
                {
                    if (!iterator.hasNext())
                    {
                        return;
                    }

                    chatline = (ChatLine)iterator.next();
                }
                while (chatline.getChatLineID() != par1);

                iterator.remove();
                return;
            }

            chatline = (ChatLine)iterator.next();
        }
        while (chatline.getChatLineID() != par1);

        iterator.remove();
    }

    public int func_96126_f()
    {
        return func_96128_a(this.mc.gameSettings.chatWidth);
    }

    public int func_96133_g()
    {
        return func_96130_b(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float func_96131_h()
    {
        return this.mc.gameSettings.chatScale;
    }

    public static final int func_96128_a(float par0)
    {
        short short1 = 320;
        byte b0 = 40;
        return MathHelper.floor_float(par0 * (float)(short1 - b0) + (float)b0);
    }

    public static final int func_96130_b(float par0)
    {
        short short1 = 180;
        byte b0 = 20;
        return MathHelper.floor_float(par0 * (float)(short1 - b0) + (float)b0);
    }

    public int func_96127_i()
    {
        return this.func_96133_g() / 9;
    }
}
