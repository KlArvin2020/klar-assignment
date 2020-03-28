package com.arvin.klar.assignment.rest.types;

/**
 * Stores information about a transaction with an associated reward
 */
public class Reward {
    private final double amount;
    private final double reward;
    private final long timestamp;

    public Reward(final double amount, final double reward, final long timestamp) {
        this.amount = amount;
        this.reward = reward;
        this.timestamp = timestamp;
    }

    /**
     * @return the total amount for the transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @return the reward factor for the transaction
     */
    public double getReward() {
        return reward;
    }

    /**
     * @return the UNIX timestamp for the transaction (in milliseconds)
     */
    public long getTimestamp() {
        return timestamp;
    }
}
