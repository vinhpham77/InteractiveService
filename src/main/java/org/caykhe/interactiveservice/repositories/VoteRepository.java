package org.caykhe.interactiveservice.repositories;


import io.micrometer.common.lang.NonNull;
import jakarta.annotation.Nullable;
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
public interface VoteRepository extends JpaRepository<Vote,VoteId> {
//    @Transactional
//    @Query("SELECT v FROM Vote v WHERE v.targetId = :targetId AND v.targetType = :targetType AND v.username = :username")
//    Optional<Vote> findByTargetIdAndTargetTypeAndUsername(
//            @Param("targetId") Integer targetId,
//            @Param("targetType") Boolean targetType,
//            @Param("username") String username
//    );


    @NonNull
    Optional<Vote> findById(@NonNull VoteId id);
    Optional<Vote> findByTargetIdAndTargetTypeAndUsername(Integer targetId, Boolean targetType, String username);
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.targetId = :targetId AND v.targetType = :targetType AND v.username = :user")
    int deleteByTargetIdAndTargetTypeAndUser(
            @Param("targetId") Integer targetId,
            @Param("targetType") Boolean targetType,
            @Param("user") String user
    );
}
