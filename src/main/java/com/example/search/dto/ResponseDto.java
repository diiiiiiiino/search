package com.example.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseDto {
    PagingDto paging;
    List<SearchBlogDto> data;
}
