package pro.sky.school.hogvards.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.NoSuchElementException;
import java.util.Optional;


import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl implements AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final AvatarRepository avatarRepository;

    private final StudentRepository studentRepository;

    private final int BUFFER_SIZE = 1024;

    @Value("${path.to.avatars.folder}")
    private String avatarsDirectory;

    public AvatarServiceImpl(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Attempting to upload avatar for student with ID: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Student with ID: {} not found.", studentId);
                    return new EntityNotFoundException("Student not found");
                });

        try {
            Path avatarPath = saveToDisk(studentId, avatarFile);
            saveToDatabase(student, avatarPath, avatarFile);
            logger.info("Avatar uploaded successfully for student with ID: {}", studentId);
        } catch (IOException e) {
            logger.error("Error occurred while uploading avatar for student with ID: {}. Error: {}", studentId, e.getMessage());
            throw e;
        }
    }


    @Override
    public Avatar findAvatarById(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> {
                    String errorMessage = "Аватар с ID " + avatarId + " не найден!";
                    logger.error(errorMessage);
                    return new NoSuchElementException(errorMessage);
                });
    }


    private Path saveToDisk(Long studentId, MultipartFile avatarFile) throws IOException {
        Path filePath = Path.of(avatarsDirectory, "avatar" + studentId + "." + getExtensions(avatarFile.getOriginalFilename()));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        logger.info("Saving an avatar for a student with an ID: " + studentId + " в " + filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            logger.error("Error saving avatar: " + e.getMessage(), e);
            throw e;
        }

        logger.info("Avatar successfully saved: " + filePath);
        return filePath;
    }


    private void saveToDatabase(Student student, Path avatarPath, MultipartFile avatarFile) throws IOException {

        Avatar avatar = findAvatar(student.getId());


        avatar.setStudent(student);
        avatar.setFilePath(avatarPath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        try {
            avatarRepository.save(avatar);
            logger.info("Аватар успешно сохранен для студента с ID: " + student.getId());
        } catch (Exception e) {
            logger.error("Ошибка при сохранении аватара в базу данных: " + e.getMessage(), e);
            throw e;
        }

    }

    private Avatar findAvatar(Long studentId) {
        Optional<Avatar> avatarOptional = avatarRepository.findByStudent_id(studentId);

        if (avatarOptional.isPresent()) {
            logger.info("Avatar found for student ID: {}", studentId);
            return avatarOptional.get();
        } else {
            logger.warn("Avatar not found for student ID: {}", studentId);
            return new Avatar();
        }
    }



    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}

