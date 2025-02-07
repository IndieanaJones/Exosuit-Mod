package jones.exosuitmod.network;

import jones.exosuitmod.network.packets.PacketSendClick;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketInit
{
    public static final SimpleNetworkWrapper PACKET_HANDLER_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("exosuitmod");
    
    public static void registerMessages(FMLPreInitializationEvent event) 
    {
        PACKET_HANDLER_INSTANCE.registerMessage(PacketSendClick.SendClickHandler.class, PacketSendClick.class, 0, Side.SERVER);
    }
}
