package com.demo.inventory.data.dto;

import com.demo.inventory.item.dto.FolderDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderResponse {

    private Long currentFolderId;
    private Long parentFolderId;
    private String currentFolderPathName;
    private List<ItemNodeResponse> items;
    private List<FolderDto> folders;
}
