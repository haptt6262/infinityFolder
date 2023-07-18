package com.example.foldervocuc.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "infinity")
public class FolderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String url;
    @Column
    private String type;
    @Column
    private String path;

    public static FolderEntity convertInToEntity(FolderIn folderIn) {
        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setName(folderIn.getName());
        return folderEntity;
    }

    public static FolderDto convertEntityToDto(FolderEntity folderEntity) {
        FolderDto folderDto = new FolderDto();
        folderDto.setId(folderEntity.getId());
        folderDto.setName(folderEntity.getName());
        folderDto.setPath(folderEntity.getPath());
        folderDto.setType(folderEntity.getType());
        folderDto.setUrl(folderEntity.getUrl());
        return folderDto;
    }
}
