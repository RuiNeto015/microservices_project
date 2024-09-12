package com.isep.acme.applicationServices.events.users;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class UserRegisteredEvent extends EventMessage {

    private static final String EVENT_TOPIC = "user.registered";

    private UserRegisteredPayload user;

    public UserRegisteredEvent(UserRegisteredPayload userRegistered) {
        super(EVENT_TOPIC);
        this.user = userRegistered;
    }
}
