package lq2007.mcmod.drawer_keychain;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributesModifiable;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

public class Status {

    public enum Type {
        DRAWER(4, 9) {
            @Override
            public void set(IDrawerAttributesModifiable attrs, boolean enabled) {
                attrs.setItemLocked(LockAttribute.LOCK_EMPTY, enabled);
                attrs.setItemLocked(LockAttribute.LOCK_POPULATED, enabled);
            }

            @Override
            public boolean get(IDrawerAttributesModifiable attrs) {
                return attrs.isItemLocked(LockAttribute.LOCK_POPULATED);
            }
        },
        NUMBER(28, 33) {
            @Override
            public void set(IDrawerAttributesModifiable attrs, boolean enabled) {
                attrs.setIsShowingQuantity(enabled);
            }

            @Override
            public boolean get(IDrawerAttributesModifiable attrs) {
                return attrs.isShowingQuantity();
            }
        },
        SHROUD(52, 57) {
            @Override
            public void set(IDrawerAttributesModifiable attrs, boolean enabled) {
                attrs.setIsConcealed(enabled);
            }

            @Override
            public boolean get(IDrawerAttributesModifiable attrs) {
                return attrs.isConcealed();
            }
        };

        public final String name;
        public final int selectedY, btnY;

        Type(int selectedY, int btnY) {
            name = name().toLowerCase(Locale.ROOT);
            this.selectedY = selectedY;
            this.btnY = btnY;
        }

        public abstract void set(IDrawerAttributesModifiable attrs, boolean enabled);

        public abstract boolean get(IDrawerAttributesModifiable attrs);
    }

    public enum Value {
        CHANGE(TextFormatting.YELLOW, 38, 42) {
            @Override
            public void apply(Type type, IDrawerAttributesModifiable attrs) {
                type.set(attrs, !type.get(attrs));
            }
        },
        SHOW(TextFormatting.GREEN, 61, 66) {
            @Override
            public void apply(Type type, IDrawerAttributesModifiable attrs) {
                if (!type.get(attrs)) {
                    type.set(attrs, true);
                }
            }
        },
        HIDE(TextFormatting.RED, 83, 88) {
            @Override
            public void apply(Type type, IDrawerAttributesModifiable attrs) {
                if (type.get(attrs)) {
                    type.set(attrs, false);
                }
            }
        },
        BAN(TextFormatting.GRAY, 106, 110) {
            @Override
            public void apply(Type type, IDrawerAttributesModifiable attrs) {

            }
        };

        public final String name;
        public final TextFormatting color;
        public final int selectedX, btnX;

        Value(TextFormatting color, int selectedX, int btnX) {
            this.name = name().toLowerCase(Locale.ROOT);
            this.color = color;
            this.selectedX = selectedX;
            this.btnX = btnX;
        }

        public abstract void apply(Type type, IDrawerAttributesModifiable attrs);
    }
}
