package jones.exosuitmod.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class ExosuitModCreativeTab extends CreativeTabs 
{
    public static final ExosuitModCreativeTab INSTANCE = new ExosuitModCreativeTab();

    private ExosuitModCreativeTab() 
    {
        super("tabExosuitMod");
    }

    public ItemStack getTabIconItem() 
    {
        return new ItemStack(ItemInit.EXOSUIT_EGG_UPGRADE_MK1);
    }
}

