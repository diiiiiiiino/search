package com.example.search.dto;

import com.example.search.dto.kakao.DocumentDto;
import com.example.search.dto.naver.ItemDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchBlogDto {
    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    private String datetime;
    private String bloggerlink;

    public static SearchBlogDto of(DocumentDto documentDto){
        return SearchBlogDto.builder()
                .title(documentDto.getTitle())
                .contents(documentDto.getContents())
                .url(documentDto.getUrl())
                .blogname(documentDto.getBlogname())
                .thumbnail(documentDto.getThumbnail())
                .datetime(documentDto.getDatetime())
                .build();
    }

    public static SearchBlogDto of(ItemDto itemDto){
        return SearchBlogDto.builder()
                .title(itemDto.getTitle())
                .contents(itemDto.getDescription())
                .url(itemDto.getLink())
                .blogname(itemDto.getBloggername())
                .datetime(itemDto.getPostdate())
                .bloggerlink(itemDto.getBloggerlink())
                .build();
    }
}
