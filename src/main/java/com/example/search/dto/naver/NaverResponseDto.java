package com.example.search.dto.naver;

import lombok.Data;

import java.util.List;

@Data
public class NaverResponseDto {
    String lastBuildDate;
    Integer total;
    Integer start;
    Integer display;
    List<ItemDto> items;
}
