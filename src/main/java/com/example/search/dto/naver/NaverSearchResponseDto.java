package com.example.search.dto.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NaverSearchResponseDto {
    String lastBuildDate;
    Integer total;
    Integer start;
    Integer display;
    List<ItemDto> items;
}
