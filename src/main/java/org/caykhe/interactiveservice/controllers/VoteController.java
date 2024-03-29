package org.caykhe.interactiveservice.controllers;

import lombok.RequiredArgsConstructor;
import org.caykhe.interactiveservice.dtos.VoteRequest;
import org.caykhe.interactiveservice.models.Vote;
import org.caykhe.interactiveservice.services.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/votes")
public class VoteController {
    final private VoteService voteService;
    @GetMapping
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }
    
    @PostMapping("/createVote")
    public ResponseEntity<?> createVote(@RequestBody VoteRequest voteRequest) {
        return new ResponseEntity<>(voteService.createVote(voteRequest), HttpStatus.OK) ;
    }
    @PostMapping("/unVote")
    public ResponseEntity<?> unVote(@RequestParam Integer targetId,@RequestParam Boolean targetType) {
        voteService.unVote(targetId,targetType);
        return new ResponseEntity<>("Xóa thành công",HttpStatus.OK) ;
    }

    @GetMapping("/checkVote")
    public ResponseEntity<?> checkVote(@RequestParam Integer targetId,@RequestParam Boolean targetType) {
        return new ResponseEntity<>(voteService.hasVoted(targetId,targetType),HttpStatus.OK) ;
    }
}
