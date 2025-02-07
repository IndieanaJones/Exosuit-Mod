package jones.exosuitmod.network;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import jones.exosuitmod.network.packets.PacketSendJump;
//import jones.exosuitmod.network.packets.PacketSendKnockback;
import net.minecraftforge.fml.common.network.NetworkRegistry;
//import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketInit
{
    public static SimpleNetworkWrapper PACKET_HANDLER_INSTANCE;
    
    public static void registerMessages(FMLPreInitializationEvent event) 
    {
        PACKET_HANDLER_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("exosuitmod");
        //PACKET_HANDLER_INSTANCE.registerMessage(PacketSendKnockback.SendKnockbackHandler.class, PacketSendKnockback.class, 0, Side.CLIENT);
    }
}
