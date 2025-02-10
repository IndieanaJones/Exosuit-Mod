package jones.exosuitmod.inventory;

import jones.exosuitmod.entity.EntityMessagerChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMessagerChickenInventory extends Container 
{
    private final EntityMessagerChicken messagerChicken;

    public ContainerMessagerChickenInventory(EntityPlayer player, EntityMessagerChicken mob) 
    {
        this.messagerChicken = mob;

        // Add mob's inventory slots (Example: 5 slots for mob's inventory)
        for (int i = 0; i < messagerChicken.inventory.getSizeInventory(); i++) 
        {
            this.addSlotToContainer(new Slot(messagerChicken.inventory, i, 80 + (i * 18), 18));
        }

        // Add player's inventory slots
        for (int row = 0; row < 3; row++) 
        {
            for (int col = 0; col < 9; col++) 
            {
                this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Add player's hotbar slots
        for (int i = 0; i < 9; i++) 
        {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) 
    {
        return this.messagerChicken.isEntityAlive() && this.messagerChicken.getDistance(playerIn) < 8.0F;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) 
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) 
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < messagerChicken.inventory.getSizeInventory()) 
            {
                if (!this.mergeItemStack(itemstack1, messagerChicken.inventory.getSizeInventory(), this.inventorySlots.size(), true)) 
                {
                    return ItemStack.EMPTY;
                }
            } 
            else if (!this.mergeItemStack(itemstack1, 0, messagerChicken.inventory.getSizeInventory(), false)) 
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) 
            {
                slot.putStack(ItemStack.EMPTY);
            } 
            else 
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
}