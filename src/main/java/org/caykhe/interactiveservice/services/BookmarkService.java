package org.caykhe.interactiveservice.services;

import lombok.RequiredArgsConstructor;
import org.caykhe.interactiveservice.dtos.ApiException;
import org.caykhe.interactiveservice.dtos.BookmarkDetailRequest;
import org.caykhe.interactiveservice.dtos.ResultCount;
import org.caykhe.interactiveservice.dtos.User;
import org.caykhe.interactiveservice.models.Bookmark;
import org.caykhe.interactiveservice.models.BookmarkDetail;
import org.caykhe.interactiveservice.models.BookmarkDetailId;
import org.caykhe.interactiveservice.repositories.BookmarkDetailRepository;
import org.caykhe.interactiveservice.repositories.BookmarkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkDetailRepository bookmarkDetailRepository;
    private final UserService userService;

    public Bookmark createBookmark(String username) {
        Optional<User> user = userService.getByUsername(username);
        Bookmark bookmark = new Bookmark();
        if(user.isPresent()){
            bookmark=Bookmark.builder()
                    .username(username).build();
        }

        return bookmarkRepository.save(bookmark);
    }

    public BookmarkDetail addBookmarkDetail(Bookmark bookmark, Integer targetId, Boolean type) {
        BookmarkDetailId id = new BookmarkDetailId();
        id.setBookmarkId(bookmark.getId());
        id.setTargetId(targetId);
        id.setType(type);

        BookmarkDetail bookmarkPost = new BookmarkDetail();
        bookmarkPost.setId(id);
        bookmarkPost.setBookmark(bookmark);
        bookmarkPost.setTargetId(targetId);
        bookmarkPost.setType(type);
        return bookmarkDetailRepository.save(bookmarkPost);

    }
    
    public BookmarkDetail bookmark(BookmarkDetailRequest request) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();

        if (!username.equals("anonymous")) {
            Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByUsername(username);
            Bookmark bookmark;
            bookmark = bookmarkOptional.orElseGet(() -> createBookmark(username));
            return addBookmarkDetail(bookmark, request.getTargetId(), request.getType());
        } else {
            throw new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND);
        }
    }

    public Optional<Bookmark> getBookmarkById(Integer id) {
        return bookmarkRepository.findById(id);
    }

    public void unBookmark(BookmarkDetailRequest bookmarkPostRequest) {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
               Bookmark bookmark = bookmarkRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Bookmark not found for user: " + username, HttpStatus.NOT_FOUND));

        Integer targetId = bookmarkPostRequest.getTargetId();
        Boolean type = bookmarkPostRequest.getType();
        BookmarkDetailId bookmarkDetailId=new BookmarkDetailId();
        bookmarkDetailId.setBookmarkId(bookmark.getId());
        bookmarkDetailId.setType(type);
        bookmarkDetailId.setTargetId(targetId);
        BookmarkDetail bookmarkDetail = bookmarkDetailRepository.findById(bookmarkDetailId)
                .orElseThrow(() -> new ApiException("BookmarkDetail not found for targetId: " + targetId + " and type: " + type, HttpStatus.NOT_FOUND));

        bookmarkDetailRepository.delete(bookmarkDetail);

        if (bookmarkDetailRepository.findByBookmark(bookmark).isEmpty()) {
            bookmarkRepository.delete(bookmark);
        }
    }

    public Boolean isBookmark( BookmarkDetailRequest bookmarkPostRequest) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("anonymous")){
            Optional<Bookmark> bookmark = bookmarkRepository.findByUsername(username);
            if (bookmark.isEmpty()) {
                return false;
            } else {
                Integer targetId = bookmarkPostRequest.getTargetId();
                Boolean type = bookmarkPostRequest.getType();
                BookmarkDetailId bookmarkDetailId=new BookmarkDetailId();
                bookmarkDetailId.setBookmarkId(bookmark.get().getId());
                bookmarkDetailId.setType(type);
                bookmarkDetailId.setTargetId(targetId);
                Optional<BookmarkDetail> bookmarkPost = bookmarkDetailRepository.findById(bookmarkDetailId);
                return bookmarkPost.isPresent();
            }
        } else {
            return false;
        }
    }

    public ResultCount<Integer> getPostByUserName(String createdBy, Integer page, Integer limit) {
        Bookmark bookmark = bookmarkRepository.findByUsername(createdBy)
                .orElseThrow(() -> new ApiException("Bạn chưa bookmark", HttpStatus.NOT_FOUND));
        try {
            List<Integer> targetIds = getTargetsByBookmark(bookmark, false);
            Collections.reverse(targetIds);
            page = page < 1 ? 1 : page;
            int fromIndex = (page-1)*limit;
            int toIndex = fromIndex + limit;
            if(fromIndex > targetIds.size())
                fromIndex = targetIds.size();
            if(toIndex > targetIds.size())
                toIndex = targetIds.size();
            return new ResultCount<>(targetIds.subList(fromIndex, toIndex), targetIds.size());
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra. Vui lòng thử lại sau!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResultCount<Integer> getSeriesByUserName(String createdBy, Integer page, Integer limit) {
        Bookmark bookmark = bookmarkRepository.findByUsername(createdBy)
                .orElseThrow(() -> new ApiException("Bạn chưa bookmark", HttpStatus.NOT_FOUND));
        try {
            List<Integer> targetIds = getTargetsByBookmark(bookmark, true);
            Collections.reverse(targetIds);
            page = page < 1 ? 1 : page;
            int fromIndex = (page-1)*limit;
            int toIndex = fromIndex + limit;
            if(fromIndex > targetIds.size())
                fromIndex = targetIds.size();
            if(toIndex > targetIds.size())
                toIndex = targetIds.size();
            return new ResultCount<>(targetIds.subList(fromIndex, toIndex), targetIds.size());
        } catch (Exception e) {
            throw new ApiException("Có lỗi xảy ra. Vui lòng thử lại sau!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Integer> getTargetsByBookmark(Bookmark bookmark, boolean isSeries) {
        List<BookmarkDetail> bookmarkDetails = isSeries ? bookmarkDetailRepository.findByBookmarkAndTypeFalse(bookmark)
                : bookmarkDetailRepository.findByBookmarkAndTypeTrue(bookmark);
        return bookmarkDetails.stream().map(BookmarkDetail::getTargetId).collect(Collectors.toList());
    }

}
