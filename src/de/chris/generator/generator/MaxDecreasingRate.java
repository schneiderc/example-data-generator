package de.chris.generator.generator;

/**
 * Maximum decreasing rate
 */
public class MaxDecreasingRate extends MaxChangeRate {

    public MaxDecreasingRate(float maxChangeRate) {
        super(maxChangeRate);

        if(this.getMaxChangeRate() == 0) {
            throw new UnsupportedOperationException("The max decreasing rate has to be greater than 0!");
        }
    }
}
