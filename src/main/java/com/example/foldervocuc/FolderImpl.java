package com.example.foldervocuc;

import com.example.foldervocuc.Entity.FolderDto;
import com.example.foldervocuc.Entity.FolderEntity;
import com.example.foldervocuc.Entity.FolderIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FolderImpl implements FolderService {
    @Autowired
    FolderRepository folderRepository;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    @Override
    public String createFolder(FolderIn folderIn) throws Exception {
        try {
            FolderEntity folderEntity = FolderEntity.convertInToEntity(folderIn);
            if (folderIn.getName() == "" || folderIn.getName() == null) {
                return "Hãy nhập tên folder";
            }
            if (folderIn.getPath() == null || folderIn.getPath() == "") {
                List<FolderEntity> listData = folderRepository.findAll();
                if (listData.isEmpty()) {
                    folderEntity.setPath("1");
                } else {
                    String path = Integer.toString(listData.size() + 1);
                    folderEntity.setPath(path);
                }
            } else {
                List<FolderEntity> data = folderRepository.searchByPath(folderIn.getPath());
                if (data.size() == 0) {
                    return "Không tìm thấy folder phù hợp.";
                } else {
                    Integer countt = folderRepository.countByPath(folderIn.getPath() + "/");
                    if (countt == 0) {
                        folderEntity.setPath(folderIn.getPath() + "/1");
                    } else {
                        String subPath = Integer.toString(countt + 1);
                        folderEntity.setPath(folderIn.getPath() + "/" + subPath);
                    }
                }
            }
            folderEntity.setType("folder");
            folderRepository.save(folderEntity);
            return "Thêm mới thành công.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String uploadFile(MultipartFile image, String path) throws Exception {
        try {
            Path staticPath = Paths.get("static");
            Path imagePath = Paths.get("images");
            if (image.isEmpty()) {
                return "Hãy chọn ảnh để tải lên.";
            }
            if (image.getSize() > 5242880) {
                return "Ảnh quá lớn, hãy chọn ảnh khác.";
            }
            if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
                Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
            }
            Path file = CURRENT_FOLDER.resolve(staticPath)
                    .resolve(imagePath).resolve(image.getOriginalFilename());
            try (OutputStream os = Files.newOutputStream(file)) {
                os.write(image.getBytes());
            }
            FolderEntity folderEntity = new FolderEntity();
            if (path == "") {
                return "Không tìm thấy folder phù hợp.";
            } else {
                List<FolderEntity> data = folderRepository.searchByPath(path);
                if (data.size() == 0) {
                    return "Không tìm thấy folder phù hợp.";
                } else {
                    Integer countt = folderRepository.countByPath(path + "/");
                    if (countt == 0) {
                        folderEntity.setPath(path + "/1");
                    } else {
                        String subPath = Integer.toString(countt + 1);
                        folderEntity.setPath(path + "/" + subPath);
                    }
                }
            }
            folderEntity.setName(image.getOriginalFilename());
            folderEntity.setUrl(imagePath.resolve(image.getOriginalFilename()).toString());
            folderEntity.setType(image.getContentType());
            folderRepository.save(folderEntity);
            return "Thêm mới ảnh thành công";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public List<FolderDto> listDataLevel1() throws Exception {
        try {
            List<FolderEntity> listData = folderRepository.listData();
            List<FolderDto> dataRes = listData.stream().map(FolderEntity::convertEntityToDto).collect(Collectors.toList());
            return dataRes;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<FolderDto> listDataAnotherLevel(String path) throws Exception {
        try {
            List<FolderEntity> listData = folderRepository.listDataAnotherLevel(path + "/");
            List<FolderDto> dataRes = listData.stream().map(FolderEntity::convertEntityToDto).collect(Collectors.toList());
            return dataRes;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String updateFolder(FolderIn folderIn, Long id) throws Exception {
        try {
            Optional<FolderEntity> folderEntity = folderRepository.findById(id);
            if (folderEntity.get().getType().contains("folder")) {
                folderEntity.get().setName(folderIn.getName());
            }
            folderRepository.save(folderEntity.get());
            return "Cập nhật thành công.";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String deleteFolder(Long id, String path) throws Exception {
        try {
            Optional<FolderEntity> folderEntity = folderRepository.findById(id);
            if (folderEntity.get() != null) {
                if (folderEntity.get().getType().contains("folder")) {
                    folderRepository.delete(folderEntity.get());
                    List<FolderEntity> listDescendants = folderRepository.searchByPath(path + '/');
                    folderRepository.deleteAll(listDescendants);
                }
                if (folderEntity.get().getType().contains("image")) {
                    Path staticPath = Paths.get("static");
                    Path imagePath = Paths.get("images");
                    if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
                        Files.delete(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
                    }
                    folderRepository.delete(folderEntity.get());
                }
                return "Xóa thành công.";
            } else {
                return "Không tìm thấy folder/file phù hợp.";
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String dragAndDropFolder(Long parentId, Long childId, FolderIn folderIn) throws Exception {
        try {
            if (parentId != null && childId != null && folderIn.getParentPath() != "") {
                Optional<FolderEntity> folderEntity = folderRepository.findById(childId);
                if (folderEntity.get() != null) {
                    Integer countt = folderRepository.countByPath(folderIn.getParentPath() + '/');
                    if (countt == 0) {
                        folderEntity.get().setPath(folderIn.getParentPath() + "/1");
                    } else {
                        String subPath = Integer.toString(countt + 1);
                        folderEntity.get().setPath(folderIn.getParentPath() + "/" + subPath);
                    }
                    folderRepository.save(folderEntity.get());
                    return "Di chuyển thành công.";
                } else {
                    return "Không tìm thấy folder phù hợp";
                }
            } else {
                return "Không tìm thấy folder phù hợp.";
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Integer sumA() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        Integer sum = integers.stream()
                .mapToInt(Integer::intValue)
                .sum();
        return sum;
    }

}