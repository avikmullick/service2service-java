package com.sap.cc.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PrettyUserPageCreatorTest {

    @Autowired
    private PrettyUserPageCreator prettyUserPageCreator;

    @Test
    void shouldCreateAPrettyUserPage() {
        User user = new User("someName", "somePhoneNumber", "1");

        assertThat(prettyUserPageCreator.getPrettyPage(user)).isEqualTo(user.getName() + System.lineSeparator() + user.getPhoneNumber());
    }
}