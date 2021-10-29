package lq2007.mcmod.drawer_keychain;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

import static lq2007.mcmod.drawer_keychain.KeyChain.MOD_ID;

@Mod(MOD_ID)
public class KeyChain {

    public static final String MOD_ID = "drawer_keychain";

    private final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private final RegistryObject<ItemKeyChain> itemKeychain = items.register("key_chain", ItemKeyChain::new);

    private SimpleChannel channel;
    private final String channelVersion = "1.0";

    public static KeyChain INSTANCE;

    public KeyChain() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        items.register(FMLJavaModLoadingContext.get().getModEventBus());
        INSTANCE = this;
    }

    private void setup(final FMLCommonSetupEvent event) {
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "net"), () -> channelVersion, channelVersion::equals, channelVersion::equals);
        channel.registerMessage(0, StatePacket.class, StatePacket::write, StatePacket::new, StatePacket::apply, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public ItemKeyChain getKey() {
        return itemKeychain.get();
    }

    public RegistryObject<ItemKeyChain> getKeyObj() {
        return itemKeychain;
    }

    public void sendToServer(Object packet) {
        channel.sendToServer(packet);
    }
}
