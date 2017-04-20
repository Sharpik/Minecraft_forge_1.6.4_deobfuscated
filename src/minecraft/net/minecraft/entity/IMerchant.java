package net.minecraft.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public interface IMerchant
{
    void setCustomer(EntityPlayer entityplayer);

    EntityPlayer getCustomer();

    MerchantRecipeList getRecipes(EntityPlayer entityplayer);

    @SideOnly(Side.CLIENT)
    void setRecipes(MerchantRecipeList merchantrecipelist);

    void useRecipe(MerchantRecipe merchantrecipe);

    void func_110297_a_(ItemStack itemstack);
}
