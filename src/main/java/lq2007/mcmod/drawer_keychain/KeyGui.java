package lq2007.mcmod.drawer_keychain;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KeyGui extends Screen {

    public static final ResourceLocation GUI = new ResourceLocation("drawer_keychain", "textures/gui/keychain.png");
    private static final int TEX_W = 256, TEX_H = 128;

    private final PlayerEntity player;
    private final Hand hand;

    private int left;
    private int top;
    private final int imageWidth = 151;
    private final int imageHeight = 82;

    protected KeyGui(PlayerEntity player, Hand hand) {
        super(new StringTextComponent("Key Chains"));
        this.player = player;
        this.hand = hand;
    }

    @Override
    protected void init() {
        super.init();
        this.left = (this.width - this.imageWidth) / 2;
        this.top = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        getMinecraft().getTextureManager().bind(GUI);
        stack.pushPose();
        stack.translate(left, top, 0);
        blit(stack, 0, 0, 0, 0, imageWidth, imageHeight, TEX_W, TEX_H);
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (item instanceof ItemKeyChain) {
            ItemKeyChain keyChain = (ItemKeyChain) item;
            for (Status.Type type : Status.Type.values()) {
                Status.Value status = keyChain.getStatus(itemStack, type);
                blit(stack, status.selectedX, type.selectedY, 0, 82, 22, 22, TEX_W, TEX_H);
            }
        }
        for (Status.Value value : Status.Value.values()) {
            for (Status.Type type : Status.Type.values()) {
                if (isIn(mouseX, mouseY, value.btnX, type.btnY)) {
                    renderTooltip(stack, value.text, mouseX - left, mouseY - top);
                }
            }
        }
        stack.popPose();
    }

    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        for (Status.Type type : Status.Type.values()) {
            for (Status.Value value : Status.Value.values()) {
                if (isIn(mouseX, mouseY, value.btnX, type.btnY)) {
                    new StatePacket(hand, type, value).send();
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isIn(double mouseX, double mouseY, int x, int y) {
        mouseX = mouseX - left;
        mouseY = mouseY - top;
        return mouseX >= x && mouseY >= y && mouseX <= x + 14 && mouseY <= y + 14;
    }
}
