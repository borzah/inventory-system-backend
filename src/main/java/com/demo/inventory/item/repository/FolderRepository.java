package com.demo.inventory.item.repository;

import com.demo.inventory.item.model.Folder;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByUserIdAndParentId(Long userId, Long parentId);

    List<Folder> findAllByFolderNameAndUserIdAndParentId(String folderName, Long userId, Long parentId);

    List<Folder> findAllByUserId(Long userId);

    List<Folder> findAllByFolderIdAndUserId(Long folderId, Long userId);

    Folder findByFolderId(Long folderId);

    List<Folder> findAllByUserIdAndFolderIdIsLessThan(Long userId, Long folderId);
}
