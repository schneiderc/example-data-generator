package de.chris.generator.generator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Integer generator
 */
public class FloatGenerator implements Iterator<Float> {

    private float maxValue;
    private float minValue;
    private final float maxChangeRate;

    private float currentValue;
    private MetricChangeOption changeOption;
    private int sampleCount;
    private int maxSampleCount; //Avoid infinite loops for MAY_INCREASE_OR_DECREASE

    private Random rnd = new Random();

    private FloatGenerator(float startValue, MaxChangeRate maxChangeRate) {
        this.currentValue = startValue;
        this.maxChangeRate = maxChangeRate.getMaxChangeRate();
    }

    public FloatGenerator(float startValue,
                          int maxSampleCount,
                          float maxValue,
                          float minValue,
                          MaxChangeRate maxChangeRate)
    {
        this(startValue, maxChangeRate);

        checkStartValueAgainstMaxValue(startValue, maxValue);
        checkStartValueAginstMinValue(startValue, minValue);

        this.changeOption = MetricChangeOption.MAY_INCREASE_OR_DECREASE;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.maxSampleCount = maxSampleCount;
    }

    public FloatGenerator(float startValue,
                          float maxValue,
                          MaxIncreasingRate maxIncreasingRate)
    {
        this(startValue, maxIncreasingRate);

        checkStartValueAgainstMaxValue(startValue, maxValue);

        this.changeOption = MetricChangeOption.MAY_INCREASE;
        this.maxValue = maxValue;
    }

    public FloatGenerator(float startValue,
                          float minValue,
                          MaxDecreasingRate maxDecreasingRate)
    {
        this(startValue, maxDecreasingRate);

        checkStartValueAginstMinValue(startValue, minValue);

        this.changeOption = MetricChangeOption.MAY_DECREASE;
        this.minValue = minValue;
    }

    private float getRandomChangeRate(final boolean increase) {
        return rnd.nextFloat() * (increase ? 1f : -1f) * maxChangeRate;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return changeOption == MetricChangeOption.MAY_INCREASE_OR_DECREASE && (sampleCount < maxSampleCount) ||
                changeOption != MetricChangeOption.MAY_INCREASE_OR_DECREASE && !isCapReached(changeOption == MetricChangeOption.MAY_INCREASE);
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Float next() {
        final boolean mayIncrease;

        if(changeOption == MetricChangeOption.STAYS_CONSTANT) {
            return currentValue;
        }
        else if(changeOption == MetricChangeOption.MAY_INCREASE_OR_DECREASE) {
            mayIncrease = !(currentValue >= maxValue) && (currentValue <= minValue || rnd.nextFloat() > 0.5f);
        }
        else {
            mayIncrease = changeOption == MetricChangeOption.MAY_INCREASE;

            if(mayIncrease && isCapReached(true)) {
                throw new NoSuchElementException("Max value reached.");
            }
            else if(!mayIncrease && isCapReached(false)) {
                throw new NoSuchElementException("Min value reached.");
            }
        }

        float currentValue =  this.currentValue * (1 + getRandomChangeRate(mayIncrease));

        if(mayIncrease) {
            if(currentValue < maxValue) {
                this.currentValue = currentValue;
            }
            else {
                this.currentValue = maxValue;
            }
        }
        else {
            if(currentValue > minValue) {
                this.currentValue = currentValue;
            }
            else {
                this.currentValue = minValue;
            }
        }

        sampleCount++;
        return this.currentValue;
    }

    private boolean isCapReached(boolean mayIncrease) {
        return ( (mayIncrease && currentValue == maxValue) || (!mayIncrease && currentValue == minValue) );
    }

    private static void checkStartValueAgainstMaxValue(final float startValue, final float maxValue) {
        if(startValue > maxValue) {
            throw new UnsupportedOperationException("The start value must not be greater than the max value!");
        }
    }

    private static void checkStartValueAginstMinValue(final float startValue, final float minValue) {
        if(startValue < minValue) {
            throw new UnsupportedOperationException("The start value must not be less than the min value!");
        }
    }
}
