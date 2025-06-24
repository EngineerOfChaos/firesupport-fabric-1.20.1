package net.engineerofchaos.firesupport.shellcomponent;

import java.util.List;

public abstract class AdditionalDataShellComponent extends ShellComponent {
    private float data;

    public AdditionalDataShellComponent(List<Float> multipliers, int colour, int colourPriority) {
        super(multipliers, colour, colourPriority);
        this.data = setDefaultData();
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public AdditionalDataShellComponent withData(float data) {
        this.setData(data);
        return this;
    }

    abstract float setDefaultData();
}
