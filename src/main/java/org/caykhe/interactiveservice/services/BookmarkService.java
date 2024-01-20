package org.caykhe.interactiveservice.services;

import lombok.RequiredArgsConstructor;
import org.caykhe.interactiveservice.dtos.ApiException;
import org.caykhe.interactiveservice.dtos.BookmarkDetailRequest;
import org.caykhe.interactiveservice.dtos.User;
import org.caykhe.interactiveservice.models.Bookmark;
import org.caykhe.interactiveservice.models.BookmarkDetail;
import org.caykhe.interactiveservice.models.BookmarkDetailId;
import org.caykhe.interactiveservice.repositories.BookmarkDetailRepository;
import org.caykhe.interactiveservice.repositories.BookmarkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
    
    public BookmarkDetail bookmark(String username, BookmarkDetailRequest request) {
        Optional<User> userOptional = userService.getByUsername(username);

        if (userOptional.isPresent()) {
            Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByUsername(String.valueOf(userOptional.get()));
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

    public void unBookmark(String username, BookmarkDetailRequest bookmarkPostRequest) {
        User user = userService.getByUsername(username)
                .orElseThrow(() -> new ApiException("User not found with username: " + username, HttpStatus.NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ApiException("Bookmark not found for user: " + username, HttpStatus.NOT_FOUND));

        Integer targetId = bookmarkPostRequest.getTargetId();
        Boolean type = bookmarkPostRequest.getType();

        BookmarkDetail bookmarkDetail = bookmarkDetailRepository.findByTargetIdAndAndType(targetId, type)
                .orElseThrow(() -> new ApiException("BookmarkDetail not found for targetId: " + targetId + " and type: " + type, HttpStatus.NOT_FOUND));

        bookmarkDetailRepository.delete(bookmarkDetail);

        if (bookmarkDetailRepository.findByBookmark(bookmark).isEmpty()) {
            bookmarkRepository.delete(bookmark);
        }
    }

    public Boolean isBookmark(String username, BookmarkDetailRequest bookmarkPostRequest) {
        Optional<User> user = userService.getByUsername(username);
        if (user.isPresent()) {
            Optional<Bookmark> bookmark = bookmarkRepository.findByUsername(user.get().getUsername());
            if (bookmark.isEmpty()) {
                return false;
            } else {
                Integer targetId = bookmarkPostRequest.getTargetId();
                Boolean type = bookmarkPostRequest.getType();

                Optional<BookmarkDetail> bookmarkPost = bookmarkDetailRepository.findByTargetIdAndAndType(targetId, type);
                return bookmarkPost.isPresent();
            }
        } else {
            return false;
        }
    }

}
