package com.example.employeemanagement.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.employeemanagement.exception.ImageValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class CloudinaryImageStorageService {

    private final Cloudinary cloudinary;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public CloudinaryImageStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, Object> uploadImage(
            MultipartFile file,
            String publicId
    ) throws IOException {

        validateImage(file);

        return cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "employee-management/employees",
                        "public_id", publicId,
                        "overwrite", true
                )
        );
    }

    private void validateImage(MultipartFile file) {

        // if (file == null || file.isEmpty()) {
        //     throw new ImageValidationException(
        //             "Profile image is required"
        //     );
        // }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ImageValidationException(
                    "Only JPG, PNG, and WEBP images are allowed"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageValidationException(
                    "Profile image must not exceed 5 MB"
            );
        }
    }
}