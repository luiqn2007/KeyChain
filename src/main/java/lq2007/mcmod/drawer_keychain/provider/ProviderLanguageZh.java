package lq2007.mcmod.drawer_keychain.provider;

import lq2007.mcmod.drawer_keychain.KeyChain;
import lq2007.mcmod.drawer_keychain.Status;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ProviderLanguageZh extends LanguageProvider {

    public ProviderLanguageZh(DataGenerator gen) {
        super(gen, KeyChain.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        addItem(KeyChain.INSTANCE.getKeyObj(), "抽屉钥匙扣");
        add(KeyChain.INSTANCE.getKey().getDescriptionId() + ".desc", "三合一");

        add("drawer_keychain.tooltips." + Status.Type.DRAWER.name, "抽屉钥匙");
        add("drawer_keychain.tooltips." + Status.Type.NUMBER.name, "数量钥匙");
        add("drawer_keychain.tooltips." + Status.Type.SHROUD.name, "屏蔽钥匙");
        add("drawer_keychain.tooltips." + Status.Value.BAN.name, "禁用");
        add("drawer_keychain.tooltips." + Status.Value.CHANGE.name, "更改");
        add("drawer_keychain.tooltips." + Status.Value.SHOW.name, "显示");
        add("drawer_keychain.tooltips." + Status.Value.HIDE.name, "隐藏");
    }
}
