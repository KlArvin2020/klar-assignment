package com.arvin.klar.assignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EndpointTests {

    @LocalServerPort
    private int port;

    private String hostUrl;
    private String rewardsUrl;
    private String totalRewardUrl;

    private static HttpHeaders headers;
    private static JSONArray exampleRewardsPayload;

    private String getHostUrl() {
        if (hostUrl == null) {
            hostUrl = "http://localhost:" + port;
        }
        return hostUrl;
    }

    private String getRewardsUrl() {
        if (rewardsUrl == null) {
            rewardsUrl = getHostUrl() + "/rewards";
        }
        return rewardsUrl;
    }

    private String getTotalRewardUrl() {
        if (totalRewardUrl == null) {
            totalRewardUrl = getHostUrl() + "/reward/total/";
        }
        return totalRewardUrl;
    }

    private static JSONArray createExampleRewardsPayload() throws JSONException {
        final JSONArray rewardsList = new JSONArray();
        final JSONObject firstReward = new JSONObject();
        firstReward.put("amount", 150.00);
        firstReward.put("reward", 0.1);
        firstReward.put("timestamp", 1580641200000L);
        rewardsList.put(firstReward);
        final JSONObject secondReward = new JSONObject();
        secondReward.put("amount", 50.00);
        secondReward.put("reward", 0.1);
        secondReward.put("timestamp", 1580641200001L);
        rewardsList.put(secondReward);

        return rewardsList;
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void runBeforeAllTestMethods() throws JSONException {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        exampleRewardsPayload = createExampleRewardsPayload();
    }

    @Test
    @Order(1)
    public void testRewardTotalIsZero() {
        assertThat(
                restTemplate.getForObject(getTotalRewardUrl() + "2020-02-02", String.class)
        ).contains("{\"average\":0.0,\"min\":0.0,\"max\":0.0,\"total\":0.0}");
    }

    @Test
    @Order(2)
    public void testAddRewards() {
        final HttpEntity<String> request = new HttpEntity<>(exampleRewardsPayload.toString(), headers);
        final ResponseEntity<String> responseEntity =
                restTemplate.exchange(getRewardsUrl(), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    public void testRewardTotalGotUpdated() {
        assertThat(
                restTemplate.getForObject(getTotalRewardUrl() + "2020-02-02", String.class)
        ).contains("{\"average\":10.0,\"min\":5.0,\"max\":15.0,\"total\":20.0}");
    }

    @Test
    @Order(4)
    public void testRewardTotalAfterSecondUpdate() {
        final HttpEntity<String> request = new HttpEntity<>(exampleRewardsPayload.toString(), headers);
        final ResponseEntity<String> responseEntity =
                restTemplate.exchange(getRewardsUrl(), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertThat(
                restTemplate.getForObject(getTotalRewardUrl() + "2020-02-02", String.class)
        ).contains("{\"average\":10.0,\"min\":5.0,\"max\":15.0,\"total\":40.0}");
    }

    @Test
    @Order(5)
    public void testRewardsWithLessThanZeroAmount() throws JSONException {
        final JSONArray illegalPayload = new JSONArray(exampleRewardsPayload.toString());
        illegalPayload.getJSONObject(0).put("amount", -0.1);

        final HttpEntity<String> request = new HttpEntity<>(illegalPayload.toString(), headers);
        final ResponseEntity<String> responseEntity =
                restTemplate.exchange(getRewardsUrl(), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(6)
    public void testRewardsWithRewardOutOfRange() throws JSONException {
        final JSONArray illegalPayload = new JSONArray(exampleRewardsPayload.toString());
        illegalPayload.getJSONObject(0).put("reward", -0.1);

        final HttpEntity<String> firstRequest = new HttpEntity<>(illegalPayload.toString(), headers);
        final ResponseEntity<String> responseEntity =
                restTemplate.exchange(getRewardsUrl(), HttpMethod.POST, firstRequest, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        illegalPayload.getJSONObject(0).put("reward", 1.1);
        final HttpEntity<String> secondRequest = new HttpEntity<>(illegalPayload.toString(), headers);
        final ResponseEntity<String> secondResponseEntity =
                restTemplate.exchange(getRewardsUrl(), HttpMethod.POST, secondRequest, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, secondResponseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    public void testRewardsWithInvalidTimestamp() throws JSONException {
        final JSONArray illegalPayload = new JSONArray(exampleRewardsPayload.toString());
        /* What does it mean for a timestamp to be "invalid" when it is specified in milliseconds since 1970-01-01? */
        illegalPayload.getJSONObject(0).put("timestamp", -1);

        final HttpEntity<String> request = new HttpEntity<>(illegalPayload.toString(), headers);
        final ResponseEntity<String> responseEntity =
                restTemplate.exchange(getRewardsUrl(), HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
