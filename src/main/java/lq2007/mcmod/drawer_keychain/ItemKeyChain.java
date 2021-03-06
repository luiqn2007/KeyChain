package lq2007.mcmod.drawer_keychain;

import com.jaquadro.minecraft.storagedrawers.api.storage.EmptyDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributesModifiable;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.IProtectable;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityController;
import com.jaquadro.minecraft.storagedrawers.core.ModItemGroup;
import com.jaquadro.minecraft.storagedrawers.item.ItemKey;
import com.jaquadro.minecraft.storagedrawers.security.SecurityManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static lq2007.mcmod.drawer_keychain.Status.Type.*;

public class ItemKeyChain extends ItemKey {

    public ItemKeyChain() {
        super(new Properties().tab(ModItemGroup.STORAGE_DRAWERS));
        ;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClientSide && player.isShiftKeyDown()) {
            openGui(player, hand);
        }
        return super.use(world, player, hand);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        TileEntity tile = context.getLevel().getBlockEntity(pos);
        PlayerEntity player = context.getPlayer();
        if (tile instanceof TileEntityController && player != null && !player.isShiftKeyDown()) {
            return handleTileEntity((TileEntityController) tile, context);
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        TileEntity tile = context.getLevel().getBlockEntity(pos);
        if (tile == null) {
            return ActionResultType.PASS;
        } else {
            IDrawerAttributes attrs = tile.getCapability(DRAWER_ATTRIBUTES_CAPABILITY, null).orElse(EmptyDrawerAttributes.EMPTY);
            if (attrs instanceof IDrawerAttributesModifiable) {
                return handleDrawerAttributes((IDrawerAttributesModifiable) attrs, context);
            } else {
                return ActionResultType.PASS;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void openGui(PlayerEntity playerEntity, Hand hand) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        KeyGui gui = new KeyGui(playerEntity, hand);
        mc.setScreen(gui);
    }

    public Status.Value getStatus(ItemStack stack, Status.Type type) {
        CompoundNBT status = stack.getTagElement("status");
        if (status != null) {
            if (status.contains(type.name, Constants.NBT.TAG_STRING)) {
                return Status.Value.valueOf(status.getString(type.name).toUpperCase(Locale.ROOT));
            }
        }
        return Status.Value.CHANGE;
    }

    public ItemStack setStatus(ItemStack item, Status.Type type, Status.Value value, boolean copy) {
        ItemStack stack = copy ? item.copy() : item;
        if (value == Status.Value.CHANGE) {
            CompoundNBT status = stack.getTagElement("status");
            if (status != null) {
                status.remove(type.name);
            }
        } else {
            CompoundNBT status = stack.getOrCreateTagElement("status");
            status.putString(type.name, value.name);
        }
        return stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, net.minecraft.client.util.ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(DRAWER.text.copy().append(": ").append(getStatus(stack, DRAWER).text));
        tooltip.add(NUMBER.text.copy().append(": ").append(getStatus(stack, NUMBER).text));
        tooltip.add(SHROUD.text.copy().append(": ").append(getStatus(stack, SHROUD).text));
    }

    private ActionResultType handleTileEntity(TileEntityController controller, ItemUseContext context) {
        if (context.getPlayer() == null) {
            return ActionResultType.PASS;
        }
        try {
            boolean isChanged = false;
            Map<BlockPos, Object> storage = Reflections.INSTANCE.getStorageFromController(controller);
            for (Object value : storage.values()) {
                if (value instanceof IProtectable && !SecurityManager.hasAccess(context.getPlayer().getGameProfile(), (IProtectable) value)) {
                    continue;
                }
                IDrawerGroup drawer = Reflections.INSTANCE.getStorageFromRecord(value);
                if (drawer == null) {
                    continue;
                }
                IDrawerAttributes attrs = drawer.getCapability(DRAWER_ATTRIBUTES_CAPABILITY).orElse(EmptyDrawerAttributes.EMPTY);
                if (attrs instanceof IDrawerAttributesModifiable) {
                    if (!isChanged) isChanged = true;
                    handleDrawerAttributes((IDrawerAttributesModifiable) attrs, context);
                }
            }
            return isChanged ? ActionResultType.SUCCESS : ActionResultType.PASS;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return ActionResultType.PASS;
        }
    }

    private ActionResultType handleDrawerAttributes(IDrawerAttributesModifiable attrs, ItemUseContext context) {
        this.handleDrawerAttributes(attrs);
        ItemStack stack = context.getItemInHand();
        for (Status.Type type : values()) {
            getStatus(stack, type).apply(type, attrs);
        }
        return ActionResultType.SUCCESS;
    }
}
