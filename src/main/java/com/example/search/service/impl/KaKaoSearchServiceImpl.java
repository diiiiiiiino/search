package com.example.search.service.impl;

import com.example.search.dto.PagingDto;
import com.example.search.dto.ResponseDto;
import com.example.search.dto.SearchBlogDto;
import com.example.search.dto.SearchResponseDto;
import com.example.search.enumeration.SortType;
import com.example.search.service.SearchHistoryService;
import com.example.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class KaKaoSearchServiceImpl implements SearchService {
    @Value("${search.kakao.key}")
    String restApiKey;

    final SearchHistoryService searchHistoryService;
    final SearchService naverSearchService;

    @Override
    @Transactional
    public ResponseDto search(String query, SortType sort, Integer page, Integer size) {
        if(page < 1 || page > 50)
            throw new IllegalArgumentException("page is more than max");

        if(size < 1 || size > 50)
            throw new IllegalArgumentException("size is more than max");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "KakaoAK " + restApiKey);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        try{
            ResponseEntity<SearchResponseDto> responseEntity = restTemplate.exchange(getQueryParameter("https://dapi.kakao.com/v2/search/blog", query, sort, page, size), HttpMethod.GET, httpEntity, SearchResponseDto.class);
            if(HttpStatus.OK == responseEntity.getStatusCode()){
                SearchResponseDto searchResponse = responseEntity.getBody();

                searchHistoryService.save(query);

                return ResponseDto.builder()
                        .paging(PagingDto.of(searchResponse.getMeta().getPageable_count(), page, size))
                        .data(searchResponse.getDocuments().stream()
                                .map(SearchBlogDto::of)
                                .collect(Collectors.toList()))
                        .build();
            } else {
                return naverSearchService.search(query, sort, page, size);
            }
        } catch(Exception e) {
            return naverSearchService.search(query, sort, page, size);
        }
    }

    String getQueryParameter(String url, String query, SortType sort, Integer page, Integer size) {
        StringBuilder sb = new StringBuilder();
        sb.append("?query").append("=").append(query);

        if(Objects.nonNull(sort)){
            sb.append("&sort").append("=").append(sort.getKakao());
        }

        if(Objects.nonNull(page)){
            sb.append("&page").append("=").append(page);
        }

        if(Objects.nonNull(size)){
            sb.append("&size").append("=").append(size);
        }

        return url + sb;
    }
}
