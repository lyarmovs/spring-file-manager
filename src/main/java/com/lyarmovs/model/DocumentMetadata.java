package com.lyarmovs.model;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/**
 * @author Lev Yarmovsky
 * @version $Id: DocumentMetadata.java,v 1.0 3/31/2017 2:38 PM lyarmovs Exp $
 *
 * Document Metadata for uploaded file
 */
@Entity
public class DocumentMetadata {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String contentType;
    private Long size;
    private String extension;
    private Date createDate;
    private String filePath;

    public DocumentMetadata() {
    }

    public DocumentMetadata(MultipartFile file, String description) {
        this.name = file.getOriginalFilename();
        this.size = file.getSize();
        this.extension = FilenameUtils.getExtension(file.getOriginalFilename());
        this.createDate = new Date();
        this.description = description;
        this.contentType = file.getContentType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "DocumentMetadata{\n" +
                "id=" + id + "\n" +
                ", name='" + name + '\'' + "\n" +
                ", description='" + description + '\'' + "\n" +
                ", contentType='" + contentType + '\'' + "\n" +
                ", size=" + size + "\n" +
                ", extension='" + extension + '\'' + "\n" +
                ", createDate=" + createDate + "\n" +
                ", filePath='" + filePath + '\'' + "\n" +
                '}';
    }
}
