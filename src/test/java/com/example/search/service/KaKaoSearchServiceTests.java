package com.example.search.service;

import com.example.search.dto.PagingDto;
import com.example.search.dto.ResponseDto;
import com.example.search.dto.SearchBlogDto;
import com.example.search.dto.kakao.DocumentDto;
import com.example.search.dto.kakao.KaKaoSearchResponseDto;
import com.example.search.dto.kakao.MetaDto;
import com.example.search.enumeration.SortType;
import com.example.search.exception.ApplicationException;
import com.example.search.service.impl.KaKaoSearchServiceImpl;
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
import static org.mockito.Mockito.*;

class KaKaoSearchServiceTests {
    private SearchHistoryService searchHistoryService;
    private SearchService searchService;
    private SearchService naverSearchService;
    private RestTemplate restTemplate;

    KaKaoSearchServiceTests(){
        restTemplate = mock(RestTemplate.class);
        searchHistoryService = mock(SearchHistoryService.class);
        naverSearchService = mock(SearchService.class);
        searchService = new KaKaoSearchServiceImpl(searchHistoryService, naverSearchService, restTemplate);
    }

    /**
     * Test Case1. 카카오 검색 API HttpStatus 정상
     */
    @Test
    void searchTest(){
        KaKaoSearchResponseDto searchResponseDto = getSearchResponseDto();
        ResponseEntity<KaKaoSearchResponseDto> responseDtoResponseEntity = ResponseEntity.ok(searchResponseDto);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(KaKaoSearchResponseDto.class))).thenReturn(responseDtoResponseEntity);

        ResponseDto responseDto = searchService.search("컨텐츠1", SortType.ACCURACY, 1, 10);

        Assertions.assertEquals(1, responseDto.getPaging().getPage());
        Assertions.assertEquals(5, responseDto.getPaging().getTotalCount());
        Assertions.assertEquals(1, responseDto.getPaging().getTotalPage());
        Assertions.assertEquals(10, responseDto.getPaging().getSize());
        Assertions.assertEquals(5, responseDto.getData().size());
        verify(naverSearchService, times(0)).search(anyString(), any(SortType.class), anyInt(), anyInt());
    }

    /**
     * Test Case2. 카카오 검색 API HttpStatus 에러 코드 발생 시
     */
    @Test
    void searchErrorTest(){
        int page = 1, size = 10;
        ResponseDto responseDto = getResponseDto(page, size);

        ResponseEntity<KaKaoSearchResponseDto> responseDtoResponseEntity = ResponseEntity.badRequest().build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(KaKaoSearchResponseDto.class))).thenReturn(responseDtoResponseEntity);
        when(naverSearchService.search(anyString(), any(SortType.class), anyInt(), anyInt())).thenReturn(responseDto);

        ResponseDto resultResponse = searchService.search("컨텐츠1", SortType.ACCURACY, page, size);

        Assertions.assertEquals(1, resultResponse.getPaging().getPage());
        Assertions.assertEquals(5, resultResponse.getPaging().getTotalCount());
        Assertions.assertEquals(1, resultResponse.getPaging().getTotalPage());
        Assertions.assertEquals(10, resultResponse.getPaging().getSize());
        Assertions.assertEquals(5, resultResponse.getData().size());
        verify(naverSearchService, times(1)).search(anyString(), any(SortType.class), anyInt(), anyInt());
    }

    /**
     * Test Case3. 카카오 검색 API 예외 발생 시
     */
    @Test
    void searchExceptionTest(){
        int page = 1, size = 10;
        ResponseDto responseDto = getResponseDto(page, size);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(KaKaoSearchResponseDto.class))).thenThrow(new ApplicationException());
        when(naverSearchService.search(anyString(), any(SortType.class), anyInt(), anyInt())).thenReturn(responseDto);

        ResponseDto resultResponse = searchService.search("컨텐츠1", SortType.ACCURACY, page, size);

        Assertions.assertEquals(1, resultResponse.getPaging().getPage());
        Assertions.assertEquals(5, resultResponse.getPaging().getTotalCount());
        Assertions.assertEquals(1, resultResponse.getPaging().getTotalPage());
        Assertions.assertEquals(10, resultResponse.getPaging().getSize());
        Assertions.assertEquals(5, resultResponse.getData().size());
        verify(naverSearchService, times(1)).search(anyString(), any(SortType.class), anyInt(), anyInt());
    }

    /**
     * Test Case4. page / size 유효성 체크
     */
    @ParameterizedTest
    @MethodSource("searchValidTestArguments")
    void searchValidTest(int page, int size){
        Assertions.assertThrows(IllegalArgumentException.class, () -> searchService.search("컨텐츠1", SortType.ACCURACY, page, size));
    }

    static Stream searchValidTestArguments(){
        return Stream.of(
                Arguments.of(0, 50),
                Arguments.of(1, 51),
                Arguments.of(0, 51)
        );
    }

    KaKaoSearchResponseDto getSearchResponseDto(){
        List<DocumentDto> documents = IntStream.rangeClosed(1, 5).mapToObj(i -> DocumentDto.builder()
                .blogname("블로그" + i)
                .contents("컨텐츠"+ i)
                .datetime("2023-03-20T18:50:07.000+09:00")
                .thumbnail("thumbnail")
                .title("타이틀"+ i)
                .url("url" + i)
                .build())
                .collect(Collectors.toList());


        return KaKaoSearchResponseDto.builder()
                .meta(MetaDto.builder()
                        .total_count(10)
                        .pageable_count(5)
                        .is_end(false)
                        .build())
                .documents(documents)
                .build();
    }

    ResponseDto getResponseDto(int page, int size){
        KaKaoSearchResponseDto searchResponseDto = getSearchResponseDto();
        return ResponseDto.builder()
                .paging(PagingDto.of(searchResponseDto.getMeta().getPageable_count(), page, size))
                .data(searchResponseDto.getDocuments().stream()
                        .map(SearchBlogDto::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
