package com.isep.acme.domain.enums;

public enum VoteEnum {
    UpVote, DownVote;

    public static VoteEnum fromString(String voteString) {
        try {
            return VoteEnum.valueOf(voteString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
