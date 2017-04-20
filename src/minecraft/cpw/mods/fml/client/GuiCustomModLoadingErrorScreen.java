/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package cpw.mods.fml.client;

import net.minecraft.client.gui.GuiErrorScreen;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.WrongMinecraftVersionException;

public class GuiCustomModLoadingErrorScreen extends GuiErrorScreen
{
    private CustomModLoadingErrorDisplayException customException;
    public GuiCustomModLoadingErrorScreen(CustomModLoadingErrorDisplayException customException)
    {
        super(null,null);
        this.customException = customException;
    }
    @Override

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.customException.initGui(this, fontRenderer);
    }
    @Override

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.customException.drawScreen(this, fontRenderer, par1, par2, par3);
    }
}
