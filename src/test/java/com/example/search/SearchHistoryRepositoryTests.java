package com.example.search;

import com.example.search.entity.SearchHistory;
import com.example.search.repository.SearchHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SearchHistoryRepositoryTests {
    @Autowired
    SearchHistoryRepository searchHistoryRepository;

    @Test
    void save() {
        searchHistoryRepository.save(SearchHistory.builder()
                .keyword("키워드")
                .searchHit(1L)
                .build());
    }

}
