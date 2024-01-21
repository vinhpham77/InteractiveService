package org.caykhe.interactiveservice.repositories;


import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.caykhe.interactiveservice.models.Bookmark;
import org.caykhe.interactiveservice.models.BookmarkDetail;
import org.caykhe.interactiveservice.models.BookmarkDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BookmarkDetailRepository extends JpaRepository<BookmarkDetail, Integer> {

    List<BookmarkDetail> findByBookmarkAndTypeFalse(Bookmark bookmark);

    List<BookmarkDetail> findByBookmarkAndTypeTrue(Bookmark bookmark);
    Optional<BookmarkDetail> findByTargetIdAndType(@NonNull Integer targetId,@NonNull Boolean type);
    Optional<BookmarkDetail> findByIdAndTargetIdAndType(BookmarkDetailId id, @NotNull Integer targetId, @NotNull Boolean type);
    Optional<BookmarkDetail> findById(BookmarkDetailId id);
    List<BookmarkDetail> findByBookmark(Bookmark bookmark);
}
