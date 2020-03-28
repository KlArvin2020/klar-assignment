package com.arvin.klar.assignment.data.types;

/**
 * Tracks information about cash back rewards. Thread-safe.
 */
public class CashbackTracker {
    private long transactions = 0L;
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private double total = 0;

    /**
     * Takes information about a certain amount of "cash back" and tracks it.
     * @param cashback to track
     */
    public void addCashback(final double cashback) {
        synchronized (this) {
            min = Math.min(min, cashback);
            max = Math.max(max, cashback);
            total += cashback;
            transactions++;
        }
    }

    /**
     * @return the total amount of transactions tracked
     */
    public long getTransactions() {
        synchronized (this) {
            return transactions;
        }
    }

    /**
     * @return the minimum tracked amount of "cash back" for a single transaction
     */
    public synchronized double getMin() {
        synchronized (this) {
            return min;
        }
    }

    /**
     * @return the maximum tracked amount of "cash back" for a single transaction
     */
    public synchronized double getMax() {
        synchronized (this) {
            return max;
        }
    }

    /**
     * @return the total amount of "cash back" for all tracked transactions
     */
    public synchronized double getTotal() {
        synchronized (this) {
            return total;
        }
    }
}
