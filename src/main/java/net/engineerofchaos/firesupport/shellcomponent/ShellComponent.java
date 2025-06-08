package net.engineerofchaos.firesupport.shellcomponent;

import net.engineerofchaos.firesupport.util.ModRegistries;
import org.joml.Vector2i;

import java.util.List;

public class ShellComponent {
    private final int colour;
    private final int colourPriority;
    private final List<Float> multipliers;

    protected ShellComponent(List<Float> multipliers, int colour, int colourPriority) {
        this.colour = colour;
        this.multipliers = multipliers;
        this.colourPriority = colourPriority;
    }

    public List<Float> getMultipliers() {
        return multipliers;
    }

//    public NbtCompound writeIdToNBT(NbtCompound nbt) {
//        nbt.putInt("Id", ModRegistries.SHELL_COMPONENT.getRawId(this));
//        return nbt;
//    }
//    public NbtCompound writeIdToNBT(NbtInt nbt) {
//        new NbtIntArray()
//        nbt.(ModRegistries.SHELL_COMPONENT.getRawId(this));
//        return nbt;
//        DataOutput
//    }

    public static ShellComponent byRawID(int id) {
        return ModRegistries.SHELL_COMPONENT.get(id);
    }

    public static int getRawID(ShellComponent component) {
        return ModRegistries.SHELL_COMPONENT.getRawId(component);
    }

    public Vector2i getColourWithPriority() {
        return new Vector2i(this.colour, this.colourPriority);
    }


}
