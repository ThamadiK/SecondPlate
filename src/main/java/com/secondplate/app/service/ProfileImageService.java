package com.secondplate.app.service;

import com.secondplate.app.model.User;
import com.secondplate.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ProfileImageService {

    private final UserRepository userRepository;
    private final Path uploadDirectory = Path.of("src/main/resources/static/uploads");

    public ProfileImageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveProfileImage(User user, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return user;
        }

        Files.createDirectories(uploadDirectory);
        String originalName = image.getOriginalFilename() == null ? "profile-image" : image.getOriginalFilename();
        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.'))
                : "";
        String fileName = UUID.randomUUID() + extension;
        Files.copy(image.getInputStream(), uploadDirectory.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        user.setProfileImageUrl("/uploads/" + fileName);
        return userRepository.save(user);
    }
}
