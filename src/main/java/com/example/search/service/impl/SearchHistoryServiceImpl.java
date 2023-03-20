package com.example.search.service.impl;

import com.example.search.dto.SearchHistoryDto;
import com.example.search.entity.SearchHistory;
import com.example.search.repository.SearchHistoryRepository;
import com.example.search.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;

    @Override
    @Transactional
    public void save(String keyword) {
        Optional<SearchHistory> optionalSearchHistory = searchHistoryRepository.findByKeyword(keyword);

        if(optionalSearchHistory.isPresent()){
            SearchHistory searchHistory = optionalSearchHistory.get();
            searchHistory.plusSearchHit();
        } else {
            searchHistoryRepository.save(SearchHistory.builder()
                    .keyword(keyword)
                    .searchHit(1L)
                    .build());
        }
    }

    @Override
    public List<SearchHistoryDto> getHistories() {
        return searchHistoryRepository.findTop10ByOrderBySearchHitDesc()
                .stream()
                .map(v -> SearchHistoryDto.builder()
                        .keyword(v.getKeyword())
                        .searchHit(v.getSearchHit())
                        .build())
                .collect(Collectors.toList());
    }
}
