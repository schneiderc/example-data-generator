package de.chris.generator.generator;

/**
 * Maximum change rate of values
 */
public class MaxChangeRate {
    private final float maxChangeRate;

    public MaxChangeRate(final float maxChangeRate) {
        this.maxChangeRate = (maxChangeRate >= 0 ? maxChangeRate : -maxChangeRate);
    }

    float getMaxChangeRate() {
        return this.maxChangeRate;
    }
}
