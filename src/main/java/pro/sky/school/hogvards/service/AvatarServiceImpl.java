package pro.sky.school.hogvards.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.school.hogvards.model.Avatar;
import pro.sky.school.hogvards.model.Student;
import pro.sky.school.hogvards.repositories.AvatarRepository;
import pro.sky.school.hogvards.repositories.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.swagger.v3.core.util.AnnotationsUtils.getExtensions;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;

    private final StudentRepository studentRepository;

    private final int BUFFER_SIZE=1024;

@Value("${path.to.avatars.folder}")
    private  String avatarsDirectory;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
            Student student = studentRepository.findById(studentId).orElseThrow();
       Path avatarPath= saveToDisk(studentId,avatarFile);
       saveToDatabase(student,avatarPath,avatarFile);



        }

    @Override
    public Avatar findAvatarById(Long avatarId) {
        return avatarRepository.findById(avatarId).orElseThrow();
    }

    private Path saveToDisk(Long studentId,MultipartFile avatarFile) throws IOException {
        Path filePath = Path.of(avatarsDirectory, "avatar"+studentId + "." + getExtensions(avatarFile.getOriginalFilename()));


        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        ) {
            bis.transferTo(bos);
        }
        return  filePath;
    }

    private void saveToDatabase(Student student,Path avatarPath,MultipartFile avatarFile) throws IOException {

        Avatar avatar = findAvatar(student.getId());


        avatar.setStudent(student);
        avatar.setFilePath(avatarPath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);

    }
    private Avatar findAvatar(Long studentId) {
return avatarRepository.findByStudent_id(studentId).orElse(
        new Avatar()
);
    }


        private String getExtensions(String fileName) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }

    }

