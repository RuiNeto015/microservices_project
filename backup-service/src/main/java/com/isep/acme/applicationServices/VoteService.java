package com.isep.acme.applicationServices;

import com.isep.acme.adapters.dto.FetchUserDTO;
import com.isep.acme.adapters.dto.UserForServicesDTO;
import com.isep.acme.adapters.dto.VoteForVoteServiceDTO;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IVoteRepository;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.aggregates.votes.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VoteService {

    private final IVoteRepository voteRepository;

    @Transactional
    public List<VoteForVoteServiceDTO> findAllForVoteService() {
        List<Vote> votes = this.voteRepository.findAll();
        return this.votesListToVotesServiceDTO(votes);
    }

    private List<VoteForVoteServiceDTO> votesListToVotesServiceDTO(List<Vote> votes) {
        List<VoteForVoteServiceDTO> votesDTOS = new ArrayList<>();

        for (Vote v : votes) {
            VoteForVoteServiceDTO voteDTO = new VoteForVoteServiceDTO(v.getUserId(), v.getReviewId(), v.getVote());
            votesDTOS.add(voteDTO);
        }
        return votesDTOS;
    }
}
