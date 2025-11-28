package net.engineerofchaos.firesupport.shell;

public record Multipliers(float volume, float drag, float ap, float inaccuracy, float payload) {

    public Multipliers() {
        this(1,1,1,1,1);
    }

    public Multipliers combineWith(Multipliers multipliers) {
        return new Multipliers(
                this.volume() * multipliers.volume(),
                this.drag() * multipliers.drag(),
                this.ap() * multipliers.ap(),
                this.inaccuracy() * multipliers.inaccuracy(),
                this.payload() * multipliers.payload()
        );
    }

    public Multipliers overwrite(Multiplier variableMultiplier, float newMultiplier) {
        switch (variableMultiplier) {
            case VOLUME -> {
                return new Multipliers(newMultiplier, this.drag, this.ap, this.inaccuracy, this.payload);
            }
            case DRAG -> {
                return new Multipliers(this.volume, newMultiplier, this.ap, this.inaccuracy, this.payload);
            }
            case AP -> {
                return new Multipliers(this.volume, this.drag, newMultiplier, this.inaccuracy, this.payload);
            }
            case INACCURACY -> {
                return new Multipliers(this.volume, this.drag, this.ap, newMultiplier, this.payload);
            }
            case PAYLOAD -> {
                return new Multipliers(this.volume, this.drag, this.ap, this.inaccuracy, newMultiplier);
            }
        }
        return this;
    }
}
