package jones.exosuitmod.item;

import jones.exosuitmod.entity.EntityPatriotExosuit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExosuitModSpawnItem extends ExosuitModItemBase
{
    public ExosuitModSpawnItem(String name) 
    {
        super(name);
    }

    public ExosuitModSpawnItem(String name, String tooltip) 
    {
        super(name, tooltip);
    }

    public ExosuitModSpawnItem(String name, String tooltip, int stackSize) 
    {
        super(name, tooltip, stackSize);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (!player.canPlayerEdit(pos.offset(facing), facing, itemStack))
        {
            return EnumActionResult.FAIL;
        }
        else 
        {
            // Replace this with the desired entity type (e.g., a Cow)
            EntityPatriotExosuit entity = new EntityPatriotExosuit(worldIn);

            entity.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
            worldIn.spawnEntity(entity);
            itemStack.shrink(1);
        }

        // Return the item stack to indicate usage (it doesn't get consumed here)
        return EnumActionResult.SUCCESS;
    }
}