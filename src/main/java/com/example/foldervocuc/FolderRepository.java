package com.example.foldervocuc;

import com.example.foldervocuc.Entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    @Query(value = "select * from infinity where path like concat(:path, '%')", nativeQuery = true)
    List<FolderEntity> searchByPath(String path);

    @Query(value = "select count(*) as count from infinity as c WHERE c.path REGEXP CONCAT('^', :path, '[0-9]+$')", nativeQuery = true)
    Integer countByPath(String path);

    @Query(value = "select count(*) as countFolder from infinity as c WHERE c.path REGEXP '^[0-9]$'", nativeQuery = true)
    Integer countFolder();

    @Query(value = "select * from infinity as c where c.path REGEXP '^[0-9]$'", nativeQuery = true)
    List<FolderEntity> listData();

    @Query(value = "select * from infinity as c WHERE c.path REGEXP CONCAT('^', :path, '[0-9]+$')", nativeQuery = true)
    List<FolderEntity> listDataAnotherLevel(String path);
}
