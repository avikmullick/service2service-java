package com.sap.cc.users;

import org.springframework.stereotype.Component;

@Component
public class PrettyUserPageCreator {
    public PrettyUserPageCreator() {
    }

    public String getPrettyPage(User user) {
        return user.getName() + "\r\n" + user.getPhoneNumber();
    }
}