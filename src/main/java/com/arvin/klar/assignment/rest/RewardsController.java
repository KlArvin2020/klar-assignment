package com.arvin.klar.assignment.rest;

import com.arvin.klar.assignment.data.DataStore;
import com.arvin.klar.assignment.rest.types.Reward;
import com.arvin.klar.assignment.rest.types.RewardsTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RewardsController {

    @Autowired
    private DataStore dataStore;

    @PostMapping("/rewards")
    public ResponseEntity<List<Reward>> rewards(final @RequestBody List<Reward> rewards) {
        for (final Reward reward : rewards) {
            if (reward.getAmount() < 0 /* Amount can not be less than zero */
                    || reward.getReward() <= 0 /* Reward has to be within range, 0 < reward < 1 */
                    || reward.getReward() >= 1
                    || reward.getTimestamp() < 0) /* Timestamps before 1970-01-01 are invalid */
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        /* Since all rewards are valid, process them */
        dataStore.addRewards(rewards);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/reward/total/{date}")
    public RewardsTotal rewardTotalForDate(final @PathVariable String date) {
        return dataStore.getRewardsTotalForDate(date);
    }
}
