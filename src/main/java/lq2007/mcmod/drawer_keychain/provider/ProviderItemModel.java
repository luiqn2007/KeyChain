package lq2007.mcmod.drawer_keychain.provider;

import lq2007.mcmod.drawer_keychain.KeyChain;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ProviderItemModel extends ItemModelProvider {

    private final ResourceLocation handheld = mcLoc("handheld");

    public ProviderItemModel(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, KeyChain.MOD_ID, fileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(KeyChain.INSTANCE.getKeyObj().getId().getPath(), handheld, "layer0", modLoc("item/key_chain"));
    }
}
