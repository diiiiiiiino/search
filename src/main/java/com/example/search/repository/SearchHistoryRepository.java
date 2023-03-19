package com.example.search.repository;

import com.example.search.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SearchHistory> findByKeyword(String keyword);
    List<SearchHistory> findTop10ByOrderBySearchHitDesc();
}
