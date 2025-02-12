package jones.exosuitmod.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemInit 
{
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final ExosuitModItemBase EXOSUIT_EGG_UPGRADE_MK1 = new ExosuitModItemBase("exosuit_egg_upgrade_mk1", "An upgrade for the Messager Chicken.\nAllows eggs to be thrown as opposed to be laid, as well as greatly decreasing the cooldown.");
    public static final ExosuitModItemBase EXOSUIT_EGG_UPGRADE_MK2 = new ExosuitModItemBase("exosuit_egg_upgrade_mk2", "An upgrade for the Messager Chicken.\nThrown eggs now explode on contact.");
    public static final ExosuitModItemBase EXOSUIT_CHICKEN_DOUBLE_JUMP_MK1 = new ExosuitModItemBase("exosuit_chicken_double_jump_mk1", "An upgrade for the Messager Chicken.\nAllows a second jump to be performed mid-air. Jumps are recharged upon landing.\nIncompatible with Iron Feathers.");
    public static final ExosuitModItemBase EXOSUIT_CHICKEN_ARMOR_MK1 = new ExosuitModItemBase("exosuit_chicken_armor_mk1", "An upgrade for the Messager Chicken.\nProvides the Messager Chicken with 10 armor.\nIncompatible with Double Jump.");
    public static final ExosuitModItemBase EXOSUIT_EXPLOSIVE_EGG = new ExosuitModItemBase("exosuit_explosive_egg", "The sprite for the Messager Chicken's explosive egg.\nThis is just here for sprite purposes, you're not meant to get this normally.");

	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) 
    {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
    }

	@SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) 
    {
        for(Item item : ItemInit.ITEMS)
        {
            if(item instanceof ExosuitModItemBase)
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), ((ExosuitModItemBase)item).variantIn));
        }
    }
}
