package org.caykhe.interactiveservice.controllers;

import lombok.RequiredArgsConstructor;
import org.caykhe.interactiveservice.dtos.BookmarkDetailRequest;
import org.caykhe.interactiveservice.services.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    final BookmarkService bookmarkService;

    @PostMapping("/bookmark")
    public ResponseEntity<?> bookmark(@RequestBody BookmarkDetailRequest request) {
        return new ResponseEntity<>(bookmarkService.bookmark( request), HttpStatus.OK);
    }
    @DeleteMapping("/unBookmark")
    public ResponseEntity<?> unBookmark(
                                        @RequestBody BookmarkDetailRequest bookmarkDetailRequest) {
        bookmarkService.unBookmark( bookmarkDetailRequest);
        return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
    }
    @PostMapping("/isBookmark")
    public Boolean isBookmark(
            @RequestBody BookmarkDetailRequest bookmarkDetailRequest) {
        return bookmarkService.isBookmark(bookmarkDetailRequest);
    }
}
