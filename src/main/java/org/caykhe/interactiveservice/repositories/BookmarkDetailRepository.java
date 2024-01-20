package org.caykhe.interactiveservice.repositories;


import org.caykhe.interactiveservice.models.Bookmark;
import org.caykhe.interactiveservice.models.BookmarkDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkDetailRepository extends JpaRepository<BookmarkDetail, Integer> {

    List<BookmarkDetail> findByBookmarkAndTypeFalse(Bookmark bookmark);

    List<BookmarkDetail> findByBookmarkAndTypeTrue(Bookmark bookmark);
    Optional<BookmarkDetail> findByTargetIdAndAndType(Integer target, Boolean type);
    List<BookmarkDetail> findByBookmark(Bookmark bookmark);
}
