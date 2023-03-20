package com.example.search.controller;

import com.example.search.enumeration.SortType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class SearchControllerTests {
    @Autowired
    private MockMvc mockMvc;

    /**
     *  Test Case1. 블로그 검색
     * @throws Exception
     */
    @Test
    public void getSearchTest() throws Exception {
        mockMvc.perform(
                get("/search")
                        .queryParam("query", "피파")
                        .queryParam("sort", SortType.RECENCY.name())
                        .queryParam("page", "1")
                        .queryParam("size", "10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     *  Test Case2. 블로그 검색 파라미터 유효성 체크
     * @throws Exception
     */
    @ParameterizedTest
    @MethodSource("getSearchParamTestArguments")
    public void getSearchParamTest(int page, int size) throws Exception {
        mockMvc.perform(
                get("/search")
                        .queryParam("query", "피파")
                        .queryParam("sort", SortType.RECENCY.name())
                        .queryParam("page", String.valueOf(page))
                        .queryParam("size", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     *  Test Case3. 인기 검색어 조회
     * @throws Exception
     */
    @Test
    public void getSearchHistoryTest() throws Exception {
        mockMvc.perform(
                get("/search/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andDo(print());
    }

    static Stream getSearchParamTestArguments(){
        return Stream.of(
                Arguments.of(0, 50),
                Arguments.of(1, 51),
                Arguments.of(0, 51)
        );
    }

}
