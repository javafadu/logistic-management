package com.logistic.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageSavedResponse extends LogiResponse{

    private String imageId;

    public ImageSavedResponse(String imageId, String message, Boolean success) {
        super(message,success);
        this.imageId=imageId;
    }

}
