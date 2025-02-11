package jones.exosuitmod.inventory.slot;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RestrictedExosuitSlot extends Slot 
{
    private final List<Item> itemRestrictions;
    private final int itemLimit;
    
    public RestrictedExosuitSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, List<Item> items, int limit) 
    {
        super(inventoryIn, index, xPosition, yPosition);
        this.itemRestrictions = items;
        this.itemLimit = limit;
    }

    public boolean isItemValid(ItemStack stack) 
    {
        for(Item itemRestriction : itemRestrictions)
        {
            if(stack.getItem() == itemRestriction)
                return true;
        }
        return false;
    }

    public int getSlotStackLimit()
    {
        return itemLimit;
    }
}
