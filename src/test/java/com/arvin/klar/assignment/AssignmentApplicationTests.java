package com.arvin.klar.assignment;

import com.arvin.klar.assignment.rest.RewardsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AssignmentApplicationTests {

	@Autowired
	private RewardsController rewardsController;

	@Test
	void contextLoads() {
		assertThat(rewardsController).isNotNull();
	}

}
