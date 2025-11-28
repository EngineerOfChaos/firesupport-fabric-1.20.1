package net.engineerofchaos.firesupport.shell;

import net.engineerofchaos.firesupport.util.ModRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector2i;

import java.util.List;
import java.util.Objects;

public class ShellComponent {
    private String translationKey;
    private final int colour1;
    private final int colour2;
    private final int colourPriority;
    private final Multipliers multipliers;

    public ShellComponent(Multipliers multipliers, int colour, int colourPriority) {
        this.colour1 = colour;
        this.multipliers = multipliers;
        this.colourPriority = colourPriority;
        this.colour2 = -1;
    }

    public ShellComponent(Multipliers multipliers, int colour1, int colour2, int colourPriority) {
        this.colour1 = colour1;
        this.colour2 = colour2;
        this.multipliers = multipliers;
        this.colourPriority = colourPriority;
    }

    public static String getID(ShellComponent component) {
        return Objects.requireNonNull(ModRegistries.SHELL_COMPONENT.getId(component)).toString();
    }

    public String getID() {
        return Objects.requireNonNull(ModRegistries.SHELL_COMPONENT.getId(this)).toString();
    }

    public static ShellComponent byID(String identifier) {
        return ModRegistries.SHELL_COMPONENT.get(new Identifier(identifier));
    }

    public Multipliers getMultipliers() {
        return multipliers;
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

    /**
    If single colour component, returns colour and priority, if dual returns both colours
     */
    public Vector2i getColours(ItemStack stack) {
        if (this.isDualColour()) {
            return new Vector2i(this.colour1, this.colour2);
        }
        return new Vector2i(this.colour1, this.colourPriority);
    }

    public boolean isDualColour() {
         return this.colour2 != -1;
    }


}
