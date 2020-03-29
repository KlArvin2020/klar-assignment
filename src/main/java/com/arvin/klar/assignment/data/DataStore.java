package com.arvin.klar.assignment.data;

import com.arvin.klar.assignment.rest.types.Reward;
import com.arvin.klar.assignment.rest.types.RewardsTotal;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DataStore {
    /**
     * Adds rewards to the data store.
     * @param rewards to add
     */
    void addRewards(final List<Reward> rewards);

    /**
     * Gets the tallied "total"
     * @param dateString for which to tally rewards, in the yyyy-MM-dd format (e.g. "2012-02-02")
     * @return a {@link RewardsTotal} object that tallies the "cash back" rewards for a specific date
     */
    RewardsTotal getRewardsTotalForDate(final String dateString);
}
