package jones.exosuitmod.block;

import jones.exosuitmod.item.ExosuitModCreativeTab;
import jones.exosuitmod.item.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class ExosuitModBlockBase extends Block
{

    public ExosuitModBlockBase(String name) 
    {
        super(Material.ROCK); // Choose a material (e.g. WOOD, IRON, GROUND, etc.)
        setRegistryName(name); // Registry name (must be unique)
        setUnlocalizedName(name); // Used for translation keys
        setHardness(2.0F);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", 1); // Requires an iron pickaxe or better
        setLightLevel(0.0F); // Light emission
        this.setCreativeTab(ExosuitModCreativeTab.INSTANCE);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }
}
