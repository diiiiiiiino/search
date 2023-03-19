package com.example.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchHistoryDto {
    String keyword;
    Long searchHit;
}
