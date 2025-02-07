package jones.exosuitmod;

//import jones.exosuitmod.keybindings.KeybindingInit;
//import jones.exosuitmod.network.PacketInit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExosuitMod.MODID, useMetadata = true)
public class ExosuitMod
{
    public static final String MODID = "exosuitmod";
    public static final String NAME = "Exosuit Mod";
    public static final String VERSION = "0.1";

    @Mod.Instance
    public static ExosuitMod instance;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) 
    {
    	//PacketInit.registerMessages(event);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) 
    {
        //KeybindingInit.registerKeybindings(event);
    }
}
