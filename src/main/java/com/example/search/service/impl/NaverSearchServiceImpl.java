package com.example.search.service.impl;

import com.example.search.dto.PagingDto;
import com.example.search.dto.ResponseDto;
import com.example.search.dto.SearchBlogDto;
import com.example.search.dto.naver.NaverSearchResponseDto;
import com.example.search.enumeration.SortType;
import com.example.search.exception.ApplicationException;
import com.example.search.service.SearchHistoryService;
import com.example.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NaverSearchServiceImpl implements SearchService {
    @Value("${search.naver.key}")
    private String restApiKey;

    @Value("${search.naver.id}")
    private String restApiId;

    private final SearchHistoryService searchHistoryService;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public ResponseDto search(String query, SortType sort, Integer page, Integer size) {
        if(page < 1 || page > 100)
            throw new IllegalArgumentException("page is more than max");

        if(size < 1 || size > 100)
            throw new IllegalArgumentException("size is more than max");

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", restApiId);
        headers.add("X-Naver-Client-Secret", restApiKey);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<NaverSearchResponseDto> responseEntity = restTemplate.exchange(getQueryParameter("https://openapi.naver.com/v1/search/blog.json", query, sort, page, size), HttpMethod.GET, httpEntity, NaverSearchResponseDto.class);
        if(HttpStatus.OK == responseEntity.getStatusCode()){
            NaverSearchResponseDto searchResponse = responseEntity.getBody();

            searchHistoryService.save(query);

            return ResponseDto.builder()
                    .paging(PagingDto.of(searchResponse.getTotal(), page, size))
                    .data(searchResponse.getItems().stream()
                            .map(SearchBlogDto::of)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new ApplicationException();
        }
    }

    String getQueryParameter(String url, String query, SortType sort, Integer page, Integer size) {
        StringBuilder sb = new StringBuilder();
        sb.append("?query").append("=").append(query);

        if(Objects.nonNull(sort)){
            sb.append("&sort").append("=").append(sort.getNaver());
        }

        if(Objects.nonNull(page)){
            sb.append("&start").append("=").append(page);
        }

        if(Objects.nonNull(size)){
            sb.append("&display").append("=").append(size);
        }

        return url + sb;
    }
}
