package net.engineerofchaos.firesupport.item;

import net.minecraft.util.StringIdentifiable;

public enum ShellFillers implements StringIdentifiable, ShellFiller {
    ARMOUR_PIERCING("armour_piercing", 0),
    HIGH_EXPLOSIVE("high_explosive", 16766720)

    ;


    private final String name;
    private final int colour;


    private ShellFillers(
            String name,
            int colour
    ) {
        this.name = name;
        this.colour = colour;
    }

    @Override
    public String asString() {
        return this.name;
    }

    @Override
    public int getColour() {
        return this.colour;
    }
}
