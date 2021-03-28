package com.demo.inventory.item.service;

import com.demo.inventory.exception.ItemException;
import com.demo.inventory.item.utils.ItemUtils;
import com.demo.inventory.item.dto.ItemDto;
import com.demo.inventory.item.model.Item;
import com.demo.inventory.item.repository.ItemRepository;
import com.demo.inventory.security.AuthChecker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final AuthChecker authChecker;
    private final ItemUtils itemUtils;

    public ItemDto addItem(ItemDto itemDto, String authToken) {
        Long userId = itemDto.getUserId();
        authChecker.checkUserAttachingTheirInfo(userId, authToken);
        checkNamings(itemDto);
        if (itemDto.getFolderId() != null) {
            itemUtils.checkUserAddingItemOrFolderIntoTheirFolder(itemDto.getFolderId(), userId);
        }
        // checkNameForDuplicates(itemDto, userId);
        Item item = createItemFromDto(itemDto);
        return convertItem(itemRepository.save(item));
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public ItemDto getItem(Long itemId, String authToken) {
        Long userId = itemRepository.findByItemId(itemId).getUserId();
        authChecker.checkUserAttachingTheirInfo(userId, authToken);
        return convertItem(itemRepository.findByItemId(itemId));
    }

    public ItemDto updateItem(Long itemId, ItemDto itemDto, String authToken) {
        Item item = itemRepository.findByItemId(itemId);
        Long userId = item.getUserId();
        authChecker.checkUserAttachingTheirInfo(userId, authToken);
        checkNamings(itemDto);
        if (itemDto.getFolderId() != null) {
            itemUtils.checkUserAddingItemOrFolderIntoTheirFolder(itemDto.getFolderId(), userId);
        }
        // checkNameForDuplicates(itemDto, userId);
        item = createItemFromDto(itemDto);
        item.setItemId(itemId);
        return convertItem(itemRepository.save(item));
    }

    public void deleteItem(Long itemId, String authToken) {
        Long userId = itemRepository.findByItemId(itemId).getUserId();
        authChecker.checkUserAttachingTheirInfo(userId, authToken);
        itemRepository.deleteById(itemId);
    }

    private void checkNamings(ItemDto itemDto) {
        List<String> namings = new ArrayList<>();
        namings.add(itemDto.getItemName());
        if (itemDto.getSerialNumber() != null) namings.add(itemDto.getSerialNumber());
        if (itemDto.getDescription() != null) namings.add(itemDto.getDescription());
        itemUtils.checkNamingRegex(namings);
    }

    private void checkNameForDuplicates(ItemDto itemDto, Long userId) {
        if (!itemRepository.findAllByItemNameAndFolderIdAndUserId(
                itemDto.getItemName(),
                itemDto.getFolderId(),
                userId).isEmpty()) {
            throw new ItemException("Item with such name is already in that folder");
        }
    }

    private ItemDto convertItem(Item item) {
        return ItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .folderId(item.getFolderId())
                .userId(item.getUserId())
                .categoryId(item.getCategoryId())
                .dateAdded(item.getDateAdded())
                .description(item.getDescription())
                .serialNumber(item.getSerialNumber())
                .itemPrice(item.getItemPrice()).build();
    }

    private Item createItemFromDto(ItemDto itemDto) {
        return Item
                .builder()
                .itemName(itemDto.getItemName())
                .folderId(itemDto.getFolderId())
                .userId(itemDto.getUserId())
                .categoryId(itemDto.getCategoryId())
                .description(itemDto.getDescription())
                .dateAdded(new Date())
                .serialNumber(itemDto.getSerialNumber())
                .itemPrice(itemDto.getItemPrice()).build();
    }
}
