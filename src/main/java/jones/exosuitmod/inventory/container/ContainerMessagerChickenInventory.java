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
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 0, 9, 69, Arrays.asList(ItemInit.EXOSUIT_EGG_UPGRADE_MK1, ItemInit.EXOSUIT_EGG_UPGRADE_MK2), 1));
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 1, 27, 69, Arrays.asList(ItemInit.EXOSUIT_CHICKEN_DOUBLE_JUMP_MK1, ItemInit.EXOSUIT_CHICKEN_ARMOR_MK1), 1));
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 2, 45, 69, Arrays.asList(ItemInit.EXOSUIT_CHICKEN_SPEED_MK1), 1));
    }
}