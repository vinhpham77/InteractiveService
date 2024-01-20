package org.caykhe.interactiveservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VoteId.class)
@Table(name = "votes",schema = "ITForum")

public class Vote {

    @Id
    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @Id
    @Column(name = "target_type", nullable = false)
    private Boolean targetType = false;

    @Id
    private String user;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "vote_type", nullable = false)
    private Boolean voteType;

}