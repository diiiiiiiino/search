package com.example.search.service;

import com.example.search.dto.ResponseDto;
import com.example.search.dto.naver.ItemDto;
import com.example.search.dto.naver.NaverSearchResponseDto;
import com.example.search.enumeration.SortType;
import com.example.search.exception.ApplicationException;
import com.example.search.service.impl.NaverSearchServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NaverSearchServiceTests {
    SearchHistoryService searchHistoryService;
    SearchService searchService;
    RestTemplate restTemplate;

    NaverSearchServiceTests(){
        restTemplate = mock(RestTemplate.class);
        searchHistoryService = mock(SearchHistoryService.class);
        searchService = new NaverSearchServiceImpl(searchHistoryService, restTemplate);
    }

    /**
     * Test Case1. 네이버 검색 API HttpStatus 정상
     */
    @Test
    void searchTest(){
        NaverSearchResponseDto searchResponseDto = getSearchResponseDto();
        ResponseEntity<NaverSearchResponseDto> responseDtoResponseEntity = ResponseEntity.ok(searchResponseDto);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(NaverSearchResponseDto.class))).thenReturn(responseDtoResponseEntity);

        ResponseDto responseDto = searchService.search("컨텐츠1", SortType.ACCURACY, 1, 10);

        Assertions.assertEquals(1, responseDto.getPaging().getPage());
        Assertions.assertEquals(5, responseDto.getPaging().getTotalCount());
        Assertions.assertEquals(1, responseDto.getPaging().getTotalPage());
        Assertions.assertEquals(10, responseDto.getPaging().getSize());
        Assertions.assertEquals(5, responseDto.getData().size());
    }

    /**
     * Test Case2. 네이버 검색 API HttpStatus 에러 코드 발생 시
     */
    @Test
    void searchErrorTest(){
        int page = 1, size = 10;

        ResponseEntity<NaverSearchResponseDto> responseDtoResponseEntity = ResponseEntity.badRequest().build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(NaverSearchResponseDto.class))).thenReturn(responseDtoResponseEntity);

        Assertions.assertThrows(ApplicationException.class, () -> searchService.search("컨텐츠1", SortType.ACCURACY, page, size));
    }


    /**
     * Test Case3. page / size 유효성 체크
     */
    @ParameterizedTest
    @MethodSource("searchValidTestArguments")
    void searchValidTest(int page, int size){
        Assertions.assertThrows(IllegalArgumentException.class, () -> searchService.search("컨텐츠1", SortType.ACCURACY, page, size));
    }

    static Stream searchValidTestArguments(){
        return Stream.of(
                Arguments.of(0, 100),
                Arguments.of(1, 101),
                Arguments.of(0, 101)
        );
    }

    NaverSearchResponseDto getSearchResponseDto(){
        List<ItemDto> documents = IntStream.rangeClosed(1, 5).mapToObj(i -> ItemDto.builder()
                .bloggername("블로그" + i)
                .description("컨텐츠"+ i)
                .postdate("2023-03-20T18:50:07.000+09:00")
                .title("타이틀"+ i)
                .link("url" + i)
                .bloggerlink("bloggerlink" + i)
                .build())
                .collect(Collectors.toList());


        return NaverSearchResponseDto.builder()
                .total(5)
                .start(1)
                .display(10)
                .lastBuildDate("2023-03-20T18:50:07.000+09:00")
                .items(documents)
                .build();
    }
}
