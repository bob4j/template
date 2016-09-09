package com.openu.service;

import org.springframework.stereotype.Service;

@Service
public class CreateAdminUserEmailSender extends EmailSender {

    private static final String MESSAGE = "<p>hi %1$s</p><p>%2$s added you as administrator to Shoes Store web site."
	    + "<br />your password is:%3$s<br />" + "your user name is :%4$s</p>";

    public void sendCreateAdminUserMessage(String to, String name, String creatorName, String password, String userName) {
	send(to, "You added as Admin user", String.format(MESSAGE, name, creatorName, password, userName));
    }
}
