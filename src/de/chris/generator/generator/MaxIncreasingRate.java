package de.chris.generator.generator;

/**
 * Maximum increasing Rate
 */
public class MaxIncreasingRate extends MaxChangeRate {

    public MaxIncreasingRate(float maxChangeRate) {
        super(maxChangeRate);

        if(this.getMaxChangeRate() == 0) {
            throw new UnsupportedOperationException("The max increasing rate has to be greater than 0!");
        }
    }
}
