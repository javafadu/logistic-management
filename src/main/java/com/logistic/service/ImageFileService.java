package com.logistic.service;

import com.logistic.domain.ImageData;
import com.logistic.domain.ImageFile;
import com.logistic.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;


    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }

    @Transactional
    public String saveImage(MultipartFile file) {
        ImageFile imageFile = null;
        // 1- name of the file
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // 2- get data and create imageFile
        try {
            ImageData imageData = new ImageData(file.getBytes());
            imageFile= new ImageFile(fileName,file.getContentType(),imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        // 3- save imagefile into the db
        imageFileRepository.save(imageFile);

        // 4- get image Id and return
        return imageFile.getId();


    }
}
