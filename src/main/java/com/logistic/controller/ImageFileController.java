package com.logistic.controller;

import com.logistic.domain.ImageFile;
import com.logistic.dto.ImageFileDTO;
import com.logistic.dto.response.ImageSavedResponse;
import com.logistic.dto.response.LogiResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.service.ImageFileService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class ImageFileController {


    private final ImageFileService imageFileService;

    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    // UPLOAD Image  // 48567ba3-efcc-42bc-b231-a9d8f0786f5e
    @PostMapping("/upload")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file") MultipartFile file) {

        String imageId =  imageFileService.saveImage(file);

        ImageSavedResponse imageSavedResponse = new ImageSavedResponse(
                imageId, ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE, true);

        return ResponseEntity.ok(imageSavedResponse);

    }

    // DOWNLOAD Image
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {

        ImageFile imageFile = imageFileService.getImageFileById(id);

        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename="+imageFile.getName())
                .body(imageFile.getImageData().getData());

    }

    // DISPLAY IMAGE
    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> displayFile(@PathVariable String id) {
        ImageFile imageFile = imageFileService.getImageFileById(id);

        HttpHeaders header  = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageFile.getImageData().getData(),header, HttpStatus.OK);
    }


    // GET ALL IMAGES by Manager and Admin
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDTO>> getAllImages() {

        List<ImageFileDTO>  allImageDTOS = imageFileService.getAllImages();

        return ResponseEntity.ok(allImageDTOS);

    }

    // DELETE Image
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> deleteImageFile(@PathVariable String id) {
        imageFileService.deleteImageFile(id);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setMessage(ResponseMessage.IMAGE_DELETED_RESPONSE_MESSAGE);
        logiResponse.setSuccess(true);

        return ResponseEntity.ok(logiResponse);
    }




}
