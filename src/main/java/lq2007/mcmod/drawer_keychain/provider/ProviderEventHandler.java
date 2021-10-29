package lq2007.mcmod.drawer_keychain.provider;

import lq2007.mcmod.drawer_keychain.KeyChain;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = KeyChain.MOD_ID)
public class ProviderEventHandler {

    @SubscribeEvent
    public static void onGather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        generator.addProvider(new ProviderLanguageEn(generator));
        generator.addProvider(new ProviderLanguageZh(generator));
        generator.addProvider(new ProviderRecipe(generator));
        generator.addProvider(new ProviderItemModel(generator, fileHelper));
    }
}
