package pro.sky.school.hogvards;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.school.hogvards.controller.AvatarController;
import pro.sky.school.hogvards.model.Avatar;
import pro.sky.school.hogvards.repositories.AvatarRepository;
import pro.sky.school.hogvards.service.AvatarServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvatarController.class)
public class AvatarControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvatarServiceImpl avatarService;

    @MockBean
    private AvatarRepository avatarRepository;

    @Test
    public void testDownloadAvatarFromDb() throws Exception {

        Avatar avatar = new Avatar();
        avatar.setId(1L);
        avatar.setMediaType("image/png");
        avatar.setData(new byte[]{1, 2, 3});


        Mockito.when(avatarService.findAvatarById(anyLong())).thenReturn(avatar);


        mockMvc.perform(MockMvcRequestBuilders.get("/avatars/1/avatar-from-db"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/png"))
                .andExpect(header().longValue(HttpHeaders.CONTENT_LENGTH, 3))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));
    }

    @Test
    public void testDownloadAvatarFromFile() throws Exception {

        Path tempFile = Files.createTempFile("avatar", ".jpg");
        Files.write(tempFile, new byte[]{1, 2, 3});


        Avatar avatar = new Avatar();
        avatar.setId(1L);
        avatar.setMediaType("image/jpeg");
        avatar.setFilePath(tempFile.toString());
        avatar.setFileSize(Files.size(tempFile));


        Mockito.when(avatarService.findAvatarById(1L)).thenReturn(avatar);


        mockMvc.perform(MockMvcRequestBuilders.get("/avatars/1/avatar-from-file"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));


        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testDownloadAvatarNotFound() throws Exception {

        Mockito.when(avatarService.findAvatarById(anyLong())).thenReturn(null);


        mockMvc.perform(MockMvcRequestBuilders.get("/avatars/1/avatar-from-db"))
                .andExpect(status().isNotFound());
    }


}