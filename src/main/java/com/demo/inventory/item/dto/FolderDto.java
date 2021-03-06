package com.demo.inventory.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderDto {

    private Long folderId;
    @NotBlank(message = "Folder name cannot be blank")
    @Size(max = 40, message
            = "Folder name cannot be over 40 characters")
    private String folderName;
    private Long parentId;
    private Long userId;
}
