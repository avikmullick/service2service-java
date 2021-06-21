package com.sap.cc.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PrettyUserPageCreatorTest {

    @Autowired
    private PrettyUserPageCreator prettyUserPageCreator = new PrettyUserPageCreator();

    @Test
    void prettyPageGetsPrinted() {
        User user = new User("Richard S.", "+49 789456123", "1");

        assertThat(prettyUserPageCreator.getPrettyPage(user)).isEqualTo(user.getName() + System.lineSeparator() + user.getPhoneNumber());
    }
}