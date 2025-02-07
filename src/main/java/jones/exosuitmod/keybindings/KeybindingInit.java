package jones.exosuitmod.keybindings;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.settings.KeyBinding;

public class KeybindingInit
{
	public static void registerKeybindings(FMLInitializationEvent event)
    {
        KeyBinding[] exosuitModKeybinding = new KeyBinding[2]; 
        
        //exosuitModKeybinding[0] = new KeyBinding("key.exosuitleftclick", -100, "key.exosuitmod.category");
        //exosuitModKeybinding[1] = new KeyBinding("key.exosuitrightclick", -99, "key.exosuitmod.category");
        
        for (int i = 0; i < exosuitModKeybinding.length; ++i) 
        {
            ClientRegistry.registerKeyBinding(exosuitModKeybinding[i]);
        }
    }
}
