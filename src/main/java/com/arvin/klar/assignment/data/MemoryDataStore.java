package com.arvin.klar.assignment.data;

import com.arvin.klar.assignment.data.types.CashbackTracker;
import com.arvin.klar.assignment.rest.types.Reward;
import com.arvin.klar.assignment.rest.types.RewardsTotal;
import com.arvin.klar.assignment.util.DateUtility;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * A simple in-memory data store
 */
@Component
public class MemoryDataStore implements DataStore {
    /**
     * Stores "cash back" totals for specific dates, e.g. "2020-02-02".
     */
    private final HashMap<String, CashbackTracker> cashbackTrackersByDate = new HashMap<>();

    /**
     * {@inheritDoc}
     * The rewards will not be saved, but rather, information about them will be tracked.
     * @param rewards to track
     */
    public void addRewards(final List<Reward> rewards) {
        synchronized (this) {
            for (final Reward reward : rewards) {
                final String dateString = DateUtility.timestampToDateString(reward.getTimestamp());
                final CashbackTracker cashbackTracker =
                        cashbackTrackersByDate.getOrDefault(
                                dateString,
                                new CashbackTracker()
                        );

                cashbackTracker.addCashback(reward.getReward() * reward.getAmount());
                cashbackTrackersByDate.put(dateString, cashbackTracker);
            }
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
