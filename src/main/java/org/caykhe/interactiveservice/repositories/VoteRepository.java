package org.caykhe.interactiveservice.repositories;


import jakarta.transaction.Transactional;
import org.caykhe.interactiveservice.models.Vote;
import org.caykhe.interactiveservice.models.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    Optional<Vote> findByTargetIdAndTargetTypeAndUser(Integer targetId, Boolean targetType, String user);
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.targetId = :targetId AND v.targetType = :targetType AND v.user = :user")
    int deleteByTargetIdAndTargetTypeAndUser(
            @Param("targetId") Integer targetId,
            @Param("targetType") Boolean targetType,
            @Param("user") String user
    );
}
