package com.isep.acme.applicationServices.interfaces.amqp;

import com.isep.acme.applicationServices.events.votes.VoteAddedPayload;

public interface IVoteEventSender {

    void notifyVoteAdded(VoteAddedPayload voteAddedInfo);

}
