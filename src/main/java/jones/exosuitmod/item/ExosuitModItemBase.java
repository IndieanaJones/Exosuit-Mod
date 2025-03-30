package jones.exosuitmod.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExosuitModItemBase extends Item
{
    public String variantIn;

    public String tooltipString;

    public ExosuitModItemBase(String name) 
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(ExosuitModCreativeTab.INSTANCE);
        this.variantIn = "inventory";
        this.maxStackSize = 1;

        ItemInit.ITEMS.add(this);
    }

    public ExosuitModItemBase(String name, String tooltip) 
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(ExosuitModCreativeTab.INSTANCE);
        this.variantIn = "inventory";
        this.tooltipString = tooltip;
        this.maxStackSize = 1;

        ItemInit.ITEMS.add(this);
    }

    public ExosuitModItemBase(String name, String tooltip, int stackSize) 
    {
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setCreativeTab(ExosuitModCreativeTab.INSTANCE);
        this.variantIn = "inventory";
        this.tooltipString = tooltip;
        this.maxStackSize = stackSize;

        ItemInit.ITEMS.add(this);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String[] tooltipArray = tooltipString.split("\n");
        for(String tooltipLine : tooltipArray)
        {
            tooltip.add(tooltipLine);
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}