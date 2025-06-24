package net.engineerofchaos.firesupport.shellcomponent;

import net.engineerofchaos.firesupport.util.ModRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class ShellComponent {
    private String translationKey;
    private final int colour;
    private final int colourPriority;
    private final List<Float> multipliers;

    public ShellComponent(List<Float> multipliers, int colour, int colourPriority) {
        this.colour = colour;
        this.multipliers = multipliers;
        this.colourPriority = colourPriority;
    }

    public List<Float> getMultipliers() {
        return multipliers;
    }

    public static ShellComponent byRawID(int id) {
        return ModRegistries.SHELL_COMPONENT.get(id);
    }

    public static int getRawID(ShellComponent component) {
        return ModRegistries.SHELL_COMPONENT.getRawId(component);
    }

    public int getRawID() {
        return ModRegistries.SHELL_COMPONENT.getRawId(this);
    }

    protected String loadTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.createTranslationKey("shellComponent", ModRegistries.SHELL_COMPONENT.getId(this));
        }

        return this.translationKey;
    }

    public String getTranslationKey() {
        return this.loadTranslationKey();
    }

    public Text getName() {
        return Text.translatable(this.getTranslationKey());
    }

    public Vector2i getColourWithPriority() {
        return new Vector2i(this.colour, this.colourPriority);
    }


}
