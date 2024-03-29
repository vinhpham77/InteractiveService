package org.caykhe.interactiveservice.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.caykhe.interactiveservice.dtos.ApiException;
import org.caykhe.interactiveservice.dtos.User;
import org.caykhe.interactiveservice.dtos.VoteRequest;
import org.caykhe.interactiveservice.models.Vote;
import org.caykhe.interactiveservice.repositories.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    final VoteRepository voteRepository;
    final UserService userService;
    final EntityManager entityManager;
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

@Transactional
    public Vote voteById(Integer targetId,Boolean targetType) {
        String  username =  SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Vote> vote=voteRepository.findByTargetIdAndTargetTypeAndUsername(targetId,targetType,username);
        if(vote.isPresent())
            return vote.get();
        else throw new ApiException("Vote không tìm thấy", HttpStatus.NOT_FOUND);
    }
    @Transactional
    public Vote createVote(VoteRequest voteRequest) {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        Vote vote = Vote.builder()
                .targetId(voteRequest.getTargetId())
                .voteType(voteRequest.getVoteType())
                .targetType(voteRequest.getTargetType())
                .username(username)
                .updatedAt(Instant.now()).build();
        return voteRepository.save(vote);
    }



    public void unVote(Integer targetId, Boolean targetType) {
        Vote vote= voteById(targetId,targetType);
        voteRepository.delete(vote);
    }
    public Vote hasVoted(Integer targetId,Boolean targetType) {

        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.equals("anonymousUser")){
            return null;
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Vote> vote= voteRepository.findByTargetIdAndTargetTypeAndUsername(targetId,targetType,username);
        return vote.orElse(null);

    }
}
