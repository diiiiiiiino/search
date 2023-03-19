package com.example.search.service;

import com.example.search.dto.ResponseDto;
import com.example.search.enumeration.SortType;

public interface SearchService {
    ResponseDto search(String query, SortType sort, Integer page, Integer size);
}
