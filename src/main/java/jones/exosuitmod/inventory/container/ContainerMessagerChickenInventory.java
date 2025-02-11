package jones.exosuitmod.inventory.container;

import java.util.Arrays;

import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.inventory.slot.RestrictedExosuitSlot;
import jones.exosuitmod.item.ItemInit;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMessagerChickenInventory extends ContainerExosuitInventory 
{
    public ContainerMessagerChickenInventory(EntityPlayer player, AbstractExosuit mob) 
    {
        super(player, mob);
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 0, 80, 18, Arrays.asList(ItemInit.EXOSUIT_EGG_UPGRADE_MK1, ItemInit.EXOSUIT_EGG_UPGRADE_MK2), 1));
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 1, 98, 18, Arrays.asList(ItemInit.EXOSUIT_CHICKEN_DOUBLE_JUMP_MK1), 1));
    }
}