package com.isep.acme.applicationServices.interfaces.amqp;

import com.isep.acme.applicationServices.events.users.UserRegisteredPayload;

public interface IUserEventSender {

    void notifyRegisteredEvent(UserRegisteredPayload userRegisteredInfo);

}
