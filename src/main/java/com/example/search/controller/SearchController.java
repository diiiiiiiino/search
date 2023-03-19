package com.example.search.controller;

import com.example.search.dto.ResponseDto;
import com.example.search.dto.SearchHistoryDto;
import com.example.search.enumeration.SortType;
import com.example.search.service.SearchHistoryService;
import com.example.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping
    public ResponseEntity<ResponseDto> search(
            @RequestParam String query,
            @RequestParam(required = false) SortType sort,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ){
        return ResponseEntity.ok(searchService.search(query, sort, page, size));
    }

    @GetMapping("/history")
    public ResponseEntity<List<SearchHistoryDto>> getSearchHistory(
    ){
        return ResponseEntity.ok(searchHistoryService.getHistories());
    }
}
