package net.engineerofchaos.firesupport.shell;

public enum CaseLength {
    SHORT(2, 1, 1),
    MED(5, 2, 4),
    LONG(10, 2, 4);

    private final float caseLengthCals;
    private final float areaMultiplier;
    private final float bulletLengthCals;

    CaseLength(float caseLengthCals, float areaMultiplier, float bulletLengthCals) {
        this.caseLengthCals = caseLengthCals;
        this.areaMultiplier = areaMultiplier;
        this.bulletLengthCals = bulletLengthCals;
    }

    public float getCasingVolume(float cal, float caseInset) {
        float calArea = (float) ((Math.PI * cal * cal)/4f);
        return (caseLengthCals - caseInset) * cal * calArea * areaMultiplier;
    }

    public float getBulletVolume(float cal, float caseInset) {
        float calArea = (float) ((Math.PI * cal * cal)/4f);
        return calArea * (bulletLengthCals + caseInset) * cal;
    }

    public static CaseLength getCaseLength(int ordinal) {
        switch (ordinal) {
            case 0 -> {return CaseLength.SHORT;}
            case 1 -> {return CaseLength.MED;}
            case 2 -> {return CaseLength.LONG;}
        }
        return CaseLength.SHORT;
    }
}
