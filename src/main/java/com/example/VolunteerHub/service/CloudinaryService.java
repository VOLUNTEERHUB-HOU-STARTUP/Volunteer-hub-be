package com.example.VolunteerHub.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    @Autowired
    Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        System.out.println(">>> cloudinary = " + cloudinary); // Kiểm tra có null không

        Map<String, String> uploadResult =
                cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));

        return Map.of(
                "url", uploadResult.get("secure_url"),
                "type", uploadResult.get("resource_type")
                );
    }
}
