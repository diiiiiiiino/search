package com.example.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagingDto {
    private Integer page;
    private Integer size;
    private Integer totalCount;
    private Integer totalPage;

    public static PagingDto of(Integer totalCount, Integer page, Integer size){
        return PagingDto.builder()
                .page(page)
                .size(size)
                .totalCount(totalCount)
                .totalPage(totalCount / size + (totalCount % size == 0 ? 0 : 1))
                .build();
    }
}
