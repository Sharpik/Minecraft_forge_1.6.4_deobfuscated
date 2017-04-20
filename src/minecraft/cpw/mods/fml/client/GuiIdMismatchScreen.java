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

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.StringTranslate;

import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;

import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.common.versioning.ArtifactVersion;

public class GuiIdMismatchScreen extends GuiYesNo {
    private List<String> missingIds = Lists.newArrayList();
    private List<String> mismatchedIds = Lists.newArrayList();
    private boolean allowContinue;

    public GuiIdMismatchScreen(MapDifference<Integer, ItemData> idDifferences, boolean allowContinue)
    {
        super(null,"ID mismatch", "Should I continue?", 1);
        parentScreen = this;
        for (Entry<Integer, ItemData> entry : idDifferences.entriesOnlyOnLeft().entrySet())
        {
            missingIds.add(String.format("ID %d from Mod %s is missing", entry.getValue().getItemId(), entry.getValue().getModId(), entry.getValue().getItemType()));
        }
        for (Entry<Integer, ValueDifference<ItemData>> entry : idDifferences.entriesDiffering().entrySet())
        {
            ItemData world = entry.getValue().leftValue();
            ItemData game = entry.getValue().rightValue();
            mismatchedIds.add(String.format("ID %d is mismatched between world and game", world.getItemId()));
        }
        this.allowContinue = allowContinue;
    }

    @Override
    public void confirmClicked(boolean choice, int par2)
    {
        FMLClientHandler.instance().callbackIdDifferenceResponse(choice);
    }

    @Override

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        if (!allowContinue && buttonList.size() == 2)
        {
            buttonList.remove(0);
        }
        int offset = Math.max(85 - (missingIds.size() + mismatchedIds.size()) * 10, 30);
        this.drawCenteredString(this.fontRenderer, "Forge Mod Loader has found ID mismatches", this.width / 2, 10, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, "Complete details are in the log file", this.width / 2, 20, 0xFFFFFF);
        int maxLines = 20;
        for (String s: missingIds) {
            this.drawCenteredString(this.fontRenderer, s, this.width / 2, offset, 0xEEEEEE);
            offset += 10;
            maxLines --;
            if (maxLines < 0) break;
            if (offset >= this.height - 30) break;
        }
        if (maxLines > 0 && offset < this.height - 30)
        {
            for (String s: mismatchedIds) {
                this.drawCenteredString(this.fontRenderer, s, this.width / 2, offset, 0xEEEEEE);
                offset += 10;
                maxLines --;
                if (maxLines < 0) break;
                if (offset >= this.height - 30) break;
            }
        }
        if (allowContinue)
        {
            this.drawCenteredString(this.fontRenderer, "Do you wish to continue loading?", this.width / 2, this.height - 30, 0xFFFFFF);
        }
        else
        {
            this.drawCenteredString(this.fontRenderer, "You cannot connect to this server", this.width / 2, this.height - 30, 0xFFFFFF);
        }
        // super.super. Grrr
        for (int var4 = 0; var4 < this.buttonList.size(); ++var4)
        {
            GuiButton var5 = (GuiButton)this.buttonList.get(var4);
            var5.yPosition = this.height - 20;
            if (!allowContinue)
            {
                var5.xPosition = this.width / 2 - 75;
                var5.displayString = I18n.getString("gui.done");
            }
            var5.drawButton(this.mc, par1, par2);
        }
    }
}
