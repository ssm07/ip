package com.iprocessor.DTO;

import org.springframework.core.io.FileSystemResource;

import java.io.File;

public class EmailDTO {

    private String to;
    private String subject;
    private String body;
    private FileSystemResource attachment;
    private String attachmentName;

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public FileSystemResource getAttachment() {
        return attachment;
    }

    public void setAttachment(FileSystemResource attachment) {
        this.attachment = attachment;
    }
}
