package jones.exosuitmod.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.block.BlockFurnace;

@Mod.EventBusSubscriber
public class SoundHandler 
{
    public static SoundEvent EXOSUIT_STEP;
    public static SoundEvent EXOSUIT_HURT;
    public static SoundEvent EXOSUIT_REPAIR;
    
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        EXOSUIT_STEP = registerSound("entity.exosuit.step", event);
        EXOSUIT_HURT = registerSound("entity.exosuit.hurt", event);
        EXOSUIT_REPAIR = registerSound("entity.exosuit.repair", event);
    }

    public static SoundEvent registerSound(String soundName, RegistryEvent.Register<SoundEvent> event)
    {
        ResourceLocation location = new ResourceLocation("exosuitmod:" + soundName);
        SoundEvent sound = new SoundEvent(location);
        sound.setRegistryName(location);
        event.getRegistry().register(sound);
        return sound;
    }
}
