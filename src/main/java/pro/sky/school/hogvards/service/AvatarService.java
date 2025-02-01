package pro.sky.school.hogvards.service;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.school.hogvards.model.Avatar;

import java.io.IOException;

public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    Avatar findAvatarById(Long avatarId);
}
