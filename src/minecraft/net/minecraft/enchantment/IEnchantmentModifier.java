package net.minecraft.enchantment;

interface IEnchantmentModifier
{
    /**
     * Generic method use to calculate modifiers of offensive or defensive enchantment values.
     */
    void calculateModifier(Enchantment enchantment, int i);
}
