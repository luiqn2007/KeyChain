package lq2007.mcmod.drawer_keychain;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StatePacket {

    private final Hand hand;
    private final Status.Type type;
    private final Status.Value value;

    public StatePacket(Hand hand, Status.Type type, Status.Value value) {
        this.hand = hand;
        this.type = type;
        this.value = value;
    }

    public StatePacket(PacketBuffer buffer) {
        hand = buffer.readEnum(Hand.class);
        type = buffer.readEnum(Status.Type.class);
        value = buffer.readEnum(Status.Value.class);
    }

    public void write(PacketBuffer buffer) {
        buffer.writeEnum(hand);
        buffer.writeEnum(type);
        buffer.writeEnum(value);
    }

    public void apply(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() ->  {
            ServerPlayerEntity sender = context.getSender();
            assert sender != null;
            ItemStack item = sender.getItemInHand(hand);
            if (item.getItem() instanceof ItemKeyChain) {
                ItemStack stack = ((ItemKeyChain) item.getItem()).setStatus(item, type, value, false);
                sender.setItemInHand(hand, stack);
            }
            context.setPacketHandled(true);
        });
    }

    public void send() {
        KeyChain.INSTANCE.sendToServer(this);
    }
}
