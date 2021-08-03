package com.sap.cc.users;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void returnsPrettyPage() {
        String prettyPage = restTemplate.getForEntity("/api/v1/users/pretty/1", String.class).getBody();
        assertThat(prettyPage).contains("\r\n");
    }
}
