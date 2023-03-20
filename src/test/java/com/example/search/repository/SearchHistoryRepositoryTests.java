package com.example.search.repository;

import com.example.search.entity.SearchHistory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class SearchHistoryRepositoryTests {
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    /**
     *  Test Case1. 검색 기록 저장
     */
    @Test
    void save() {
        searchHistoryRepository.save(SearchHistory.builder()
                .keyword("키워드")
                .searchHit(1L)
                .build());

        boolean isPresent = searchHistoryRepository.findByKeyword("키워드").isPresent();

        Assertions.assertEquals(true, isPresent);
    }

    /**
     *  Test Case2. 인기 검색어 조회
     */
    @Test
    void getHistories() {
        for(int i = 1; i <= 12; i++){
            searchHistoryRepository.save(SearchHistory.builder()
                    .keyword("키워드" + i)
                    .searchHit(1L * i)
                    .build());
        }

        List<SearchHistory> historyList = searchHistoryRepository.findTop10ByOrderBySearchHitDesc();

        Assertions.assertEquals(10, historyList.size());
    }
}
