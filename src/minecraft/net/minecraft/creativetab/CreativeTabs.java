package net.minecraft.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabs
{
    public static CreativeTabs[] creativeTabArray = new CreativeTabs[12];
    public static final CreativeTabs tabBlock = new CreativeTabCombat(0, "buildingBlocks");
    public static final CreativeTabs tabDecorations = new CreativeTabBlock(1, "decorations");
    public static final CreativeTabs tabRedstone = new CreativeTabDeco(2, "redstone");
    public static final CreativeTabs tabTransport = new CreativeTabRedstone(3, "transportation");
    public static final CreativeTabs tabMisc = (new CreativeTabTransport(4, "misc")).func_111229_a(new EnumEnchantmentType[] {EnumEnchantmentType.all});
    public static final CreativeTabs tabAllSearch = (new CreativeTabMisc(5, "search")).setBackgroundImageName("item_search.png");
    public static final CreativeTabs tabFood = new CreativeTabSearch(6, "food");
    public static final CreativeTabs tabTools = (new CreativeTabFood(7, "tools")).func_111229_a(new EnumEnchantmentType[] {EnumEnchantmentType.digger});
    public static final CreativeTabs tabCombat = (new CreativeTabTools(8, "combat")).func_111229_a(new EnumEnchantmentType[] {EnumEnchantmentType.armor, EnumEnchantmentType.armor_feet, EnumEnchantmentType.armor_head, EnumEnchantmentType.armor_legs, EnumEnchantmentType.armor_torso, EnumEnchantmentType.bow, EnumEnchantmentType.weapon});
    public static final CreativeTabs tabBrewing = new CreativeTabBrewing(9, "brewing");
    public static final CreativeTabs tabMaterials = new CreativeTabMaterial(10, "materials");
    public static final CreativeTabs tabInventory = (new CreativeTabInventory(11, "inventory")).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;

    /** Texture to use. */
    private String backgroundImageName = "items.png";
    private boolean hasScrollbar = true;

    /** Whether to draw the title in the foreground of the creative GUI */
    private boolean drawTitle = true;
    private EnumEnchantmentType[] field_111230_s;

    public CreativeTabs(String label)
    {
        this(getNextID(), label);
    }

    public CreativeTabs(int par1, String par2Str)
    {
        if (par1 >= creativeTabArray.length)
        {
            CreativeTabs[] tmp = new CreativeTabs[par1 + 1];
            for (int x = 0; x < creativeTabArray.length; x++)
            {
                tmp[x] = creativeTabArray[x];
            }
            creativeTabArray = tmp;
        }
        this.tabIndex = par1;
        this.tabLabel = par2Str;
        creativeTabArray[par1] = this;
    }

    @SideOnly(Side.CLIENT)
    public int getTabIndex()
    {
        return this.tabIndex;
    }

    public CreativeTabs setBackgroundImageName(String par1Str)
    {
        this.backgroundImageName = par1Str;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public String getTabLabel()
    {
        return this.tabLabel;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets the translated Label.
     */
    public String getTranslatedTabLabel()
    {
        return "itemGroup." + this.getTabLabel();
    }

    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return Item.itemsList[this.getTabIconItemIndex()];
    }

    @SideOnly(Side.CLIENT)

    /**
     * the itemID for the item to be displayed on the tab
     */
    public int getTabIconItemIndex()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public String getBackgroundImageName()
    {
        return this.backgroundImageName;
    }

    @SideOnly(Side.CLIENT)
    public boolean drawInForegroundOfTab()
    {
        return this.drawTitle;
    }

    public CreativeTabs setNoTitle()
    {
        this.drawTitle = false;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldHidePlayerInventory()
    {
        return this.hasScrollbar;
    }

    public CreativeTabs setNoScrollbar()
    {
        this.hasScrollbar = false;
        return this;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns index % 6
     */
    public int getTabColumn()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) % 10) % 5;
        }
        return this.tabIndex % 6;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns tabIndex < 6
     */
    public boolean isTabInFirstRow()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) % 10) < 5;
        }
        return this.tabIndex < 6;
    }

    @SideOnly(Side.CLIENT)
    public EnumEnchantmentType[] func_111225_m()
    {
        return this.field_111230_s;
    }

    public CreativeTabs func_111229_a(EnumEnchantmentType ... par1ArrayOfEnumEnchantmentType)
    {
        this.field_111230_s = par1ArrayOfEnumEnchantmentType;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_111226_a(EnumEnchantmentType par1EnumEnchantmentType)
    {
        if (this.field_111230_s == null)
        {
            return false;
        }
        else
        {
            EnumEnchantmentType[] aenumenchantmenttype = this.field_111230_s;
            int i = aenumenchantmenttype.length;

            for (int j = 0; j < i; ++j)
            {
                EnumEnchantmentType enumenchantmenttype1 = aenumenchantmenttype[j];

                if (enumenchantmenttype1 == par1EnumEnchantmentType)
                {
                    return true;
                }
            }

            return false;
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * only shows items which have tabToDisplayOn == this
     */
    public void displayAllReleventItems(List par1List)
    {
        Item[] aitem = Item.itemsList;
        int i = aitem.length;

        for (int j = 0; j < i; ++j)
        {
            Item item = aitem[j];

            if (item == null)
            {
                continue;
            }

            for (CreativeTabs tab : item.getCreativeTabs())
            {
                if (tab == this)
                {
                    item.getSubItems(item.itemID, this, par1List);
                }
            }
        }

        if (this.func_111225_m() != null)
        {
            this.addEnchantmentBooksToList(par1List, this.func_111225_m());
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * Adds the enchantment books from the supplied EnumEnchantmentType to the given list.
     */
    public void addEnchantmentBooksToList(List par1List, EnumEnchantmentType ... par2ArrayOfEnumEnchantmentType)
    {
        Enchantment[] aenchantment = Enchantment.enchantmentsList;
        int i = aenchantment.length;

        for (int j = 0; j < i; ++j)
        {
            Enchantment enchantment = aenchantment[j];

            if (enchantment != null && enchantment.type != null)
            {
                boolean flag = false;

                for (int k = 0; k < par2ArrayOfEnumEnchantmentType.length && !flag; ++k)
                {
                    if (enchantment.type == par2ArrayOfEnumEnchantmentType[k])
                    {
                        flag = true;
                    }
                }

                if (flag)
                {
                    par1List.add(Item.enchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
                }
            }
        }
    }

    public int getTabPage()
    {
        if (tabIndex > 11)
        {
            return ((tabIndex - 12) / 10) + 1;
        }
        return 0;
    }

    public static int getNextID()
    {
        return creativeTabArray.length;
    }

    /**
     * Get the ItemStack that will be rendered to the tab.
     */
    public ItemStack getIconItemStack()
    {
        return new ItemStack(getTabIconItem());
    }

    /**
     * Determines if the search bar should be shown for this tab.
     * 
     * @return True to show the bar
     */
    public boolean hasSearchBar()
    {
        return tabIndex == CreativeTabs.tabAllSearch.tabIndex;
    }
}
