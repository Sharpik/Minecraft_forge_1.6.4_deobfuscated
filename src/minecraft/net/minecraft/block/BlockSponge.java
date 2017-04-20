package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockSponge extends Block
{
    protected BlockSponge(int par1)
    {
        super(par1, Material.sponge);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
}
