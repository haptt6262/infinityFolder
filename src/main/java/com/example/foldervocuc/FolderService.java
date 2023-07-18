package com.example.foldervocuc;

import com.example.foldervocuc.Entity.FolderDto;
import com.example.foldervocuc.Entity.FolderIn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FolderService {
    String createFolder(FolderIn folderIn) throws Exception;

    String uploadFile(MultipartFile file, String path) throws Exception;

    List<FolderDto> listDataLevel1() throws Exception;

    List<FolderDto> listDataAnotherLevel(String path) throws Exception;
    String updateFolder(FolderIn folderIn, Long id) throws Exception;
    String deleteFolder(Long id, String path) throws Exception;
    String dragAndDropFolder(Long parentId, Long childId, FolderIn folderIn) throws Exception;
    Integer sumA();
}
