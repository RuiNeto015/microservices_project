package com.isep.acme.applicationServices.events.votes;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class VoteAddedEvent extends EventMessage {

    private static final String EVENT_TOPIC = "vote.added";

    private VoteAddedPayload vote;

    public VoteAddedEvent(VoteAddedPayload vote) {
        super(EVENT_TOPIC);
        this.vote = vote;
    }
}
