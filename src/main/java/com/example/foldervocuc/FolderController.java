package com.example.foldervocuc;

import com.example.foldervocuc.Base.BaseController;
import com.example.foldervocuc.Entity.FolderIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/folder")

public class FolderController extends BaseController {
    @Autowired
    FolderService folderService;

    @PostMapping("")
    public ResponseEntity<?> createFolder(@RequestBody FolderIn folderIn) throws Exception {
        try {
            return successApi(null, folderService.createFolder(folderIn));
        } catch (Exception e) {
            return errorApi(e.getMessage());
        }
    }

    @PostMapping("/img")
    public ResponseEntity<?> uploadFile(@RequestParam("image") MultipartFile file, @RequestParam String path) throws Exception {
        try {
            return successApi(null, folderService.uploadFile(file, path));
        } catch (Exception e) {
            return errorApi(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getListDataLevel1() {
        try {
            return successApi(folderService.listDataLevel1(), "OK");
        } catch (Exception e) {
            return errorApi(e.getMessage());
        }
    }

    @GetMapping("/another-level")
    public ResponseEntity<?> getListDataAnotherLevel(@RequestParam("path") String path) {
        try {
            return successApi(folderService.listDataAnotherLevel(path), "OK");
        } catch (Exception e) {
            return errorApi(e.getMessage());
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateFolder(@RequestBody FolderIn folderIn, @RequestParam Long id) {
        try {
            return successApi(null, folderService.updateFolder(folderIn, id));
        } catch (Exception e) {
            return errorApi(e.getMessage());
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteFolder(@RequestParam Long id, String path) {
        try {
            return successApi(null, folderService.deleteFolder(id, path));
        } catch (Exception e) {
            return errorApi(e.getMessage());
        }
    }

    @PutMapping("/drag")
    public ResponseEntity<?> dragAndDropFolder(@RequestParam Long parentId, Long childId, @RequestBody FolderIn folderIn) {
        try {
            return successApi(null, folderService.dragAndDropFolder(parentId, childId, folderIn));
        } catch (Exception e) {
            return errorApi(e.getMessage());
        }
    }

    @GetMapping("/test")
    public Integer sumA() {
        try {
            return folderService.sumA();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
