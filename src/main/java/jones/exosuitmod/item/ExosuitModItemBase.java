package jones.exosuitmod.item;

import net.minecraft.item.Item;

public class ExosuitModItemBase extends Item
{
    public String variantIn;

    public ExosuitModItemBase(String name) 
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(ExosuitModCreativeTab.INSTANCE);
        this.variantIn = "inventory";
        this.maxStackSize = 1;

        ItemInit.ITEMS.add(this);
    }
}