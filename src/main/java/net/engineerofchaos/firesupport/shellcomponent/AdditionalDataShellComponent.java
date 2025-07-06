package net.engineerofchaos.firesupport.shellcomponent;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public abstract class AdditionalDataShellComponent extends ShellComponent {

    public AdditionalDataShellComponent(List<Float> multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
    }

    public float getData(@NotNull HashMap<Integer, Float> additionalData) {
        int componentID = this.getRawID();
        if (additionalData.containsKey(componentID)) {
            return additionalData.get(componentID);
        }
        return getDefaultData();
    }

    public void setData(float data, @NotNull HashMap<Integer, Float> additionalData) {
        int componentID = this.getRawID();
        additionalData.put(componentID, data);
    }

//    public AdditionalDataShellComponent withData(float data) {
//        this.setData(data);
//        return this;
//    }

    abstract float getDefaultData();
}
