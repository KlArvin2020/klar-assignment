package com.arvin.klar.assignment.data;

import com.arvin.klar.assignment.data.types.CashbackTracker;
import com.arvin.klar.assignment.rest.types.Reward;
import com.arvin.klar.assignment.rest.types.RewardsTotal;
import com.arvin.klar.assignment.util.DateUtility;

import java.util.HashMap;

/**
 * A simple in-memory data store
 */
public class DataStore {
    private static DataStore instance;

    /**
     * Stores cash back totals for specific dates, e.g. "2020-02-02".
     */
    private final HashMap<String, CashbackTracker> cashbackTrackersByDate = new HashMap<>();

    /**
     * @return the single instance of {@link DataStore}
     */
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Adds rewards to the data store.
     * The rewards will not be saved, but rather, information about them will be tracked.
     * @param reward to track
     */
    public void addReward(final Reward reward) {
        final String dateString = DateUtility.timestampToDateString(reward.getTimestamp());

        synchronized (this) {
            final CashbackTracker cashbackTracker =
                    cashbackTrackersByDate.getOrDefault(
                            dateString,
                            new CashbackTracker()
                    );

            cashbackTracker.addCashback(reward.getReward() * reward.getAmount());
            cashbackTrackersByDate.put(dateString, cashbackTracker);
        }
    }

    public RewardsTotal getRewardsTotalForDate(final String dateString) {
        synchronized (this) {
            final CashbackTracker cashbackTracker = cashbackTrackersByDate.get(dateString);
            if (cashbackTracker != null) {
                return new RewardsTotal(cashbackTracker.getTotal() / cashbackTracker.getTransactions(),
                        cashbackTracker.getMin(),
                        cashbackTracker.getMax(),
                        cashbackTracker.getTotal());
            } else {
                return new RewardsTotal(0, 0, 0, 0);
            }
        }
    }
}
