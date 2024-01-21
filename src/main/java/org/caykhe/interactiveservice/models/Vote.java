package org.caykhe.interactiveservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VoteId.class)
@Table(name = "votes")

public class Vote {
    @Id
    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @Id
    @Column(name = "target_type", nullable = false)
    private Boolean targetType ;

    @Id
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "vote_type", nullable = false)
    private Boolean voteType;

}