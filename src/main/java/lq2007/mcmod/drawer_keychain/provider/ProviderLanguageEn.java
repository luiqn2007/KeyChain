package lq2007.mcmod.drawer_keychain.provider;

import lq2007.mcmod.drawer_keychain.KeyChain;
import lq2007.mcmod.drawer_keychain.Status;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ProviderLanguageEn extends LanguageProvider {

    public ProviderLanguageEn(DataGenerator gen) {
        super(gen, KeyChain.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(KeyChain.INSTANCE.getKeyObj(), "Drawer Keychain");
        add(KeyChain.INSTANCE.getKey().getDescriptionId() + ".desc", "Three IN One");

        add("drawer_keychain.tooltips." + Status.Type.DRAWER.name, "DrawerKey");
        add("drawer_keychain.tooltips." + Status.Type.NUMBER.name, "QuantifyKey");
        add("drawer_keychain.tooltips." + Status.Type.SHROUD.name, "ConcealmentKey");
        add("drawer_keychain.tooltips." + Status.Value.BAN.name, "Ban");
        add("drawer_keychain.tooltips." + Status.Value.CHANGE.name, "Change");
        add("drawer_keychain.tooltips." + Status.Value.SHOW.name, "Show");
        add("drawer_keychain.tooltips." + Status.Value.HIDE.name, "Hide");
    }
}
