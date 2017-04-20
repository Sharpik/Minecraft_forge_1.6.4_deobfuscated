package net.minecraft.command;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

class CommandSpreadPlayersPosition
{
    double field_111101_a;
    double field_111100_b;

    CommandSpreadPlayersPosition() {}

    CommandSpreadPlayersPosition(double par1, double par3)
    {
        this.field_111101_a = par1;
        this.field_111100_b = par3;
    }

    double func_111099_a(CommandSpreadPlayersPosition par1CommandSpreadPlayersPosition)
    {
        double d0 = this.field_111101_a - par1CommandSpreadPlayersPosition.field_111101_a;
        double d1 = this.field_111100_b - par1CommandSpreadPlayersPosition.field_111100_b;
        return Math.sqrt(d0 * d0 + d1 * d1);
    }

    void func_111095_a()
    {
        double d0 = (double)this.func_111096_b();
        this.field_111101_a /= d0;
        this.field_111100_b /= d0;
    }

    float func_111096_b()
    {
        return MathHelper.sqrt_double(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b);
    }

    public void func_111094_b(CommandSpreadPlayersPosition par1CommandSpreadPlayersPosition)
    {
        this.field_111101_a -= par1CommandSpreadPlayersPosition.field_111101_a;
        this.field_111100_b -= par1CommandSpreadPlayersPosition.field_111100_b;
    }

    public boolean func_111093_a(double par1, double par3, double par5, double par7)
    {
        boolean flag = false;

        if (this.field_111101_a < par1)
        {
            this.field_111101_a = par1;
            flag = true;
        }
        else if (this.field_111101_a > par5)
        {
            this.field_111101_a = par5;
            flag = true;
        }

        if (this.field_111100_b < par3)
        {
            this.field_111100_b = par3;
            flag = true;
        }
        else if (this.field_111100_b > par7)
        {
            this.field_111100_b = par7;
            flag = true;
        }

        return flag;
    }

    public int func_111092_a(World par1World)
    {
        int i = MathHelper.floor_double(this.field_111101_a);
        int j = MathHelper.floor_double(this.field_111100_b);

        for (int k = 256; k > 0; --k)
        {
            int l = par1World.getBlockId(i, k, j);

            if (l != 0)
            {
                return k + 1;
            }
        }

        return 257;
    }

    public boolean func_111098_b(World par1World)
    {
        int i = MathHelper.floor_double(this.field_111101_a);
        int j = MathHelper.floor_double(this.field_111100_b);

        for (int k = 256; k > 0; --k)
        {
            int l = par1World.getBlockId(i, k, j);

            if (l != 0)
            {
                Material material = Block.blocksList[l].blockMaterial;
                return !material.isLiquid() && material != Material.fire;
            }
        }

        return false;
    }

    public void func_111097_a(Random par1Random, double par2, double par4, double par6, double par8)
    {
        this.field_111101_a = MathHelper.getRandomDoubleInRange(par1Random, par2, par6);
        this.field_111100_b = MathHelper.getRandomDoubleInRange(par1Random, par4, par8);
    }
}
