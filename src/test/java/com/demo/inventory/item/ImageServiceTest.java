package com.demo.inventory.item;

import com.demo.inventory.exception.ItemException;
import com.demo.inventory.item.model.Item;
import com.demo.inventory.item.repository.ImageRepository;
import com.demo.inventory.item.repository.ItemRepository;
import com.demo.inventory.item.service.ImageService;
import com.demo.inventory.security.AuthChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ImageServiceTest {

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private AuthChecker authChecker;

    @MockBean
    private MultipartFile file;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(imageRepository, itemRepository, authChecker);
    }

    @Test
    void shouldNotAddImageBecauseItIsNotImageFileType() throws IOException {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(new Item()));
        when(file.getBytes()).thenReturn(new byte[]{});
        when(file.getSize()).thenReturn(200000L);
        when(file.getContentType()).thenReturn("pdf");
        assertThatThrownBy(() -> imageService.addImage(1L, file, ""))
                .isInstanceOf(ItemException.class)
                .hasMessageContaining("A file must be a image!");
    }

    @Test
    void shouldNotAddImageBecauseItIsTooBig() throws IOException {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(new Item()));
        when(file.getBytes()).thenReturn(new byte[]{});
        when(file.getSize()).thenReturn(1200000L);
        when(file.getContentType()).thenReturn("image/png");
        assertThatThrownBy(() -> imageService.addImage(1L, file, ""))
                .isInstanceOf(ItemException.class)
                .hasMessageContaining("A file cannot be over 1 Mb");
    }
}
