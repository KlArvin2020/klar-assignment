package com.arvin.klar.assignment.rest.types;

/**
 * Tallies "cash back" rewards.
 */
public class RewardsTotal {
    final private double average;
    final private double min;
    final private double max;
    final private double total;

    public RewardsTotal(final double average, final double min, final double max, final double total) {
        this.average = average;
        this.min = min;
        this.max = max;
        this.total = total;
    }

    /**
     * @return the average "cash back" reward out of a set of them
     */
    public double getAverage() {
        return average;
    }

    /**
     * @return the minimal "cash back" reward out of a set of them
     */
    public double getMin() {
        return min;
    }

    /**
     * @return the maximal "cash back" reward out of a set of them
     */
    public double getMax() {
        return max;
    }

    /**
     * @return the total "cash back" reward out of a set of them
     */
    public double getTotal() {
        return total;
    }
}
