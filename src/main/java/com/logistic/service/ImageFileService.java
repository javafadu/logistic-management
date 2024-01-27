package com.logistic.service;

import com.logistic.domain.ImageData;
import com.logistic.domain.ImageFile;
import com.logistic.dto.ImageFileDTO;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public ImageFile getImageFileById(String id) {
        ImageFile imageFile = imageFileRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.IMAGE_NOT_FOUND_MESSAGE,id)));

        return imageFile;

    }


    public List<ImageFileDTO> getAllImages() {
        List<ImageFile> imageFiles = imageFileRepository.findAll();

        // no need to use mapstruct, manual mapping -> equal one by one
        List<ImageFileDTO>  imageFileDTOS =  imageFiles.stream().map(imFile->{
                    // create URI image1: localhost:8080/files/download/id
            String imageURI = ServletUriComponentsBuilder
                    .fromCurrentContextPath() //localhost:8080
                    .path("/files/download/") //localhost:8080/files/download/
                    .path(imFile.getId()).toUriString();

                return new ImageFileDTO(imFile.getName(), imageURI,imFile.getType(), imFile.getLength());
                }).collect(Collectors.toList());

        return imageFileDTOS;
    }


    public void deleteImageFile(String id) {
        // check1: if the imagefile with the id is exist or not
        ImageFile imageFile = getImageFileById(id);

        imageFileRepository.delete(imageFile);

    }
}
