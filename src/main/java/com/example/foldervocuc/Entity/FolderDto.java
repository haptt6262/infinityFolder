package com.example.foldervocuc.Entity;

import lombok.Data;

@Data
public class FolderDto {
    private Long id;
    private String name;
    private String path;
    private String url;
    private String type;
}
