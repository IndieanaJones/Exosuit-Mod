package jones.exosuitmod.block;

import java.util.Random;

import jones.exosuitmod.item.ItemInit;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MechanicalScrapOre extends ExosuitModBlockBase
{
    public MechanicalScrapOre(String name, Material mat, float hard, float resist, String harvestTool, int harvestStrength) 
    {
        super(name, mat, hard, resist, harvestTool, harvestStrength);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) 
    {
        return ItemInit.MECHANICAL_SCRAP;
    }

    @Override
    public int quantityDropped(Random random) 
    {
        return 1;
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) 
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, 2, 5); // drop 2–5 XP
    }
}
