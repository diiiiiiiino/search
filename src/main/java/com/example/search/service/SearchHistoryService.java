package com.example.search.service;

import com.example.search.dto.SearchHistoryDto;

import java.util.List;

public interface SearchHistoryService {
    void save(String keyword);
    List<SearchHistoryDto> getPopularKeyword();
}
