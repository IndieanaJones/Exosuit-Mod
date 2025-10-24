package jones.exosuitmod.block;

import jones.exosuitmod.item.ExosuitModCreativeTab;
import jones.exosuitmod.item.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class ExosuitModBlockBase extends Block
{

    public ExosuitModBlockBase(String name, Material mat, float hard, float resist, String harvestTool, int harvestStrength) 
    {
        super(mat);
        setRegistryName(name);
        setUnlocalizedName(name);
        setHardness(hard);
        setResistance(resist);
        setHarvestLevel(harvestTool, harvestStrength);
        setLightLevel(0.0F); // Light emission
        this.setCreativeTab(ExosuitModCreativeTab.INSTANCE);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }
}
