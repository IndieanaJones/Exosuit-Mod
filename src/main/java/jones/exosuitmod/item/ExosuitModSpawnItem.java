package jones.exosuitmod.item;

import jones.exosuitmod.entity.EntityPatriotExosuit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
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
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) 
    {
        ItemStack itemStack = player.getHeldItem(hand);
        BlockPos targetPos = player.rayTrace(6, 1.0F).getBlockPos();

        if (!world.isRemote) 
        {
            // Replace this with the desired entity type (e.g., a Cow)
            EntityPatriotExosuit entity = new EntityPatriotExosuit(world);

            entity.setPosition(targetPos.getX(), targetPos.getY() + 1, targetPos.getZ());
            world.spawnEntity(entity);
            itemStack.shrink(1);
        }

        // Return the item stack to indicate usage (it doesn't get consumed here)
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
}