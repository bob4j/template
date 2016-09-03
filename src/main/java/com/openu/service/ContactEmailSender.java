package com.openu.service;

import org.springframework.stereotype.Service;

@Service
public class ContactEmailSender extends EmailSender {

    private static final String MESSAGE = "<p>new message received from:</p><p><strong> name</strong>: %1$s</p>"
            + "<p><strong>email:</strong> %2$s</p><p><strong>phone:</strong> %3$s</p><p><strong>message:</strong></p><p>%4$s</p>";

    public void sendContactMessage(String to, String name, String email, String phone, String message) {
        send(to, "New contact message received", String.format(MESSAGE, name, email, phone, message));
    }
}
