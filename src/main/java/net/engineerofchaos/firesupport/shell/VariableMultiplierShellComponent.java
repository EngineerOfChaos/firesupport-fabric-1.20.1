package net.engineerofchaos.firesupport.shell;

import net.engineerofchaos.firesupport.util.ModRegistries;

import java.util.HashMap;

public abstract class VariableMultiplierShellComponent extends ShellComponent {
    private final Multiplier variableMultiplier;

    public VariableMultiplierShellComponent(Multipliers multipliers, int colour, int colourPriority, Multiplier variableMultiplier) {
        super(multipliers, colour, colourPriority);
        this.variableMultiplier = variableMultiplier;
    }

    public Multipliers getVariableMultipliers(HashMap<String, Float> additionalData) {
        if (additionalData.containsKey(this.getID())) {
            float newMultiplier = additionalData.get(this.getID());
            return this.getMultipliers().overwrite(variableMultiplier, newMultiplier);
        } else {
            return this.getMultipliers();
        }
    }
}
