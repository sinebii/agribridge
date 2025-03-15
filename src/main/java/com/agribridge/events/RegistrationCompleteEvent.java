package com.agribridge.events;

import com.agribridge.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter

public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String appUrl;

    public RegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

}
