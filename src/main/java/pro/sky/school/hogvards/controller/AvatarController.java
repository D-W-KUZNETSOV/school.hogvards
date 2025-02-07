package pro.sky.school.hogvards.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.school.hogvards.model.Avatar;
import pro.sky.school.hogvards.repositories.AvatarRepository;
import pro.sky.school.hogvards.service.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("avatars")
public class AvatarController {


    private final AvatarService avatarService;
    private final AvatarRepository avatarRepository;

    public AvatarController(AvatarService avatarService, AvatarRepository avatarRepository) {
        this.avatarService = avatarService;
        this.avatarRepository = avatarRepository;
    }


    @GetMapping(value = "/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatarById(id);

        if (avatar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar-from-file")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatarById(id);
        if (avatar == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        Path path = Path.of(avatar.getFilePath());

        if (!Files.exists(path)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {

            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping
    public List<Avatar> getAllAvatar(@RequestParam(defaultValue = "1") Integer pageNumber,
                                     @RequestParam(defaultValue = "5") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        List<Avatar> avatars = avatarRepository.findAll(pageRequest).getContent();

        return avatars;
    }


}
