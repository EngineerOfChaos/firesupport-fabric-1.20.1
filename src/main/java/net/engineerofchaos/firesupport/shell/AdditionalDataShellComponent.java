package net.engineerofchaos.firesupport.shell;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public abstract class AdditionalDataShellComponent extends ShellComponent {

    public AdditionalDataShellComponent(Multipliers multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
    }

    public float getData(@NotNull HashMap<String, Float> additionalData) {
        String componentID = this.getID();
        if (additionalData.containsKey(componentID)) {
            return additionalData.get(componentID);
        }
        return getDefaultData();
    }

    public void setData(float data, @NotNull HashMap<String, Float> additionalData) {
        String componentID = this.getID();
        additionalData.put(componentID, data);
    }

//    public AdditionalDataShellComponent withData(float data) {
//        this.setData(data);
//        return this;
//    }

    abstract float getDefaultData();
}
