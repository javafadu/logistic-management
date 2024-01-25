package com.logistic.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.engine.internal.Cascade;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="tbl_imagefile")
public class ImageFile {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private String id;

    private String name;
    private String type;
    private long length;

    @OneToOne(cascade = CascadeType.ALL) // If I delete ımagefile, imagedata also is deleted
    private ImageData imageData;


    // without ID and set the length from imagedata, we add below constructor
    public ImageFile(String name, String type, ImageData imageData) {
        this.name=name;
        this.type=type;
        this.imageData=imageData;
        this.length=imageData.getData().length;
    }

}
