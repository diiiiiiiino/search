package com.example.search.service;

import com.example.search.dto.SearchHistoryDto;
import com.example.search.entity.SearchHistory;
import com.example.search.repository.SearchHistoryRepository;
import com.example.search.service.impl.SearchHistoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SearchHistoryServiceTests {
    SearchHistoryRepository searchHistoryRepository;
    SearchHistoryService searchHistoryService;

    SearchHistoryServiceTests(){
        searchHistoryRepository = mock(SearchHistoryRepository.class);
        searchHistoryService = new SearchHistoryServiceImpl(searchHistoryRepository);
    }

    /**
     *  Test Case1. 검색 기록 저장
     */
    @Test
    void saveTest(){
        when(searchHistoryRepository.findByKeyword(anyString())).thenReturn(Optional.empty());

        searchHistoryService.save("keyword");

        verify(searchHistoryRepository, times(1)).save(any(SearchHistory.class));
    }

    /**
     *  Test Case2. 검색 기록 수정
     */
    @Test
    void updateTest(){
        String keyword = "키워드";
        SearchHistory searchHistory = SearchHistory.builder()
                .keyword(keyword)
                .searchHit(1L)
                .build();

        when(searchHistoryRepository.findByKeyword(anyString())).thenReturn(Optional.of(searchHistory));

        searchHistoryService.save(keyword);

        Assertions.assertEquals("키워드", searchHistory.getKeyword());
        Assertions.assertEquals(2, searchHistory.getSearchHit());
    }

    /**
     *  Test Case3. 인기 검색어 조회
     */
    @Test
    void getHistoriesTest(){
        List<SearchHistory> searchHistories = List.of(
                SearchHistory.builder().keyword("키워드1").searchHit(10L).build(),
                SearchHistory.builder().keyword("키워드2").searchHit(20L).build(),
                SearchHistory.builder().keyword("키워드3").searchHit(30L).build(),
                SearchHistory.builder().keyword("키워드4").searchHit(40L).build(),
                SearchHistory.builder().keyword("키워드5").searchHit(50L).build(),
                SearchHistory.builder().keyword("키워드6").searchHit(60L).build(),
                SearchHistory.builder().keyword("키워드7").searchHit(70L).build(),
                SearchHistory.builder().keyword("키워드8").searchHit(80L).build(),
                SearchHistory.builder().keyword("키워드9").searchHit(90L).build(),
                SearchHistory.builder().keyword("키워드10").searchHit(100L).build()
        );

        when(searchHistoryRepository.findTop10ByOrderBySearchHitDesc()).thenReturn(searchHistories);

        List<SearchHistoryDto> historyDtoList = searchHistoryService.getHistories();

        Assertions.assertEquals(10, historyDtoList.size());
    }
}
