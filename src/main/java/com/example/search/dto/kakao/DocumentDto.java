package com.example.search.dto.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    String title;
    String contents;
    String url;
    String blogname;
    String thumbnail;
    String datetime;
}
