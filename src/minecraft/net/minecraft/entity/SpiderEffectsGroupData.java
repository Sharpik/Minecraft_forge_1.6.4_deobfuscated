package net.minecraft.entity;

import java.util.Random;
import net.minecraft.potion.Potion;

public class SpiderEffectsGroupData implements EntityLivingData
{
    public int field_111105_a;

    public void func_111104_a(Random par1Random)
    {
        int i = par1Random.nextInt(5);

        if (i <= 1)
        {
            this.field_111105_a = Potion.moveSpeed.id;
        }
        else if (i <= 2)
        {
            this.field_111105_a = Potion.damageBoost.id;
        }
        else if (i <= 3)
        {
            this.field_111105_a = Potion.regeneration.id;
        }
        else if (i <= 4)
        {
            this.field_111105_a = Potion.invisibility.id;
        }
    }
}
