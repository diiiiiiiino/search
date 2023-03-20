package com.example.search.dto.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    String title;
    String link;
    String description;
    String bloggername;
    String bloggerlink;
    String postdate;
}
