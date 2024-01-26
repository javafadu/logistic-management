package com.logistic.controller;

import com.logistic.dto.response.ImageSavedResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.service.ImageFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class ImageFileController {


    private final ImageFileService imageFileService;

    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    // UPLOAD Image
    @PostMapping("/upload")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file") MultipartFile file) {

        String imageId =  imageFileService.saveImage(file);

        ImageSavedResponse imageSavedResponse = new ImageSavedResponse(
                imageId, ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(imageSavedResponse);

    }

}
