package lq2007.mcmod.drawer_keychain;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityController;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

public class Reflections {

    static final Reflections INSTANCE = new Reflections();

    private Field TileEntityController$storage;
    private Field TileEntityController$StorageRecord$storage;

    public Map<BlockPos, Object> getStorageFromController(TileEntityController controller) throws NoSuchFieldException, IllegalAccessException {
        Objects.requireNonNull(controller);
        if (TileEntityController$storage == null) {
            Field field = TileEntityController.class.getDeclaredField("storage");
            TileEntityController$storage = checkReturnType(field, Map.class);
            TileEntityController$storage.setAccessible(true);
        }
        return (Map<BlockPos, Object>) TileEntityController$storage.get(controller);
    }

    @Nullable
    public IDrawerGroup getStorageFromRecord(Object record) throws NoSuchFieldException, IllegalAccessException {
        Objects.requireNonNull(record);
        if (TileEntityController$StorageRecord$storage == null) {
            Field field = record.getClass().getDeclaredField("storage");
            TileEntityController$StorageRecord$storage = checkReturnType(field, IDrawerGroup.class);
            TileEntityController$StorageRecord$storage.setAccessible(true);
        }
        return (IDrawerGroup) TileEntityController$StorageRecord$storage.get(record);
    }

    private Field checkReturnType(Field field, Class<?> type) throws NoSuchFieldException {
        Class<?> returnType = field.getType();
        if (!type.isAssignableFrom(returnType)) {
            throw new NoSuchFieldException("Field " + field.getName() + " should return " + type.getSimpleName() + ", but return " + returnType.getSimpleName());
        }
        return field;
    }
}
