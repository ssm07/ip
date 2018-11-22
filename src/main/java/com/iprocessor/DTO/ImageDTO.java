package com.iprocessor.DTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ImageDTO {

    private String filterType;
    private String imagePath;
    private String destinationPath;
    private String fileName;
    private String mimeType;


    //  it should be odd numbers
    private Integer kernelWidth;
    private Integer kernelHeight;

    //for gaussian filter
    private Integer sigma;

    //for bilateral filter
    private Integer diameter;
    private Double sigmaColor;
    private Double sigmaSpace;
    private Integer borderType;

    //for median filter,laplace filter
    private Integer ksize;

    //for averaging filter
    private Integer pointX = -1;
    private Integer pointY = -1;


    //for laplace filter and sobel
    Integer dDepth;
    Double scale;
    Double delta;

    // for sobel

    private Integer dx;
    private Integer dy;

    //for canny
    private Integer threshold1;
    private Integer threshold2;

    public Integer getThreshold1() {
        return threshold1;
    }

    public void setThreshold1(Integer threshold1) {
        this.threshold1 = threshold1;
    }

    public Integer getThreshold2() {
        return threshold2;
    }

    public void setThreshold2(Integer threshold2) {
        this.threshold2 = threshold2;
    }

    public Integer getDx() {
        return dx;
    }

    public void setDx(Integer dx) {
        this.dx = dx;
    }

    public Integer getDy() {
        return dy;
    }

    public void setDy(Integer dy) {
        this.dy = dy;
    }

    public Integer getdDepth() {
        return dDepth;
    }

    public void setdDepth(Integer dDepth) {
        this.dDepth = dDepth;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public Integer getPointX() {
        return pointX;
    }

    public void setPointX(Integer pointX) {
        this.pointX = pointX;
    }

    public Integer getPointY() {
        return pointY;
    }

    public void setPointY(Integer pointY) {
        this.pointY = pointY;
    }

    private List<String> errorMessage = new ArrayList<>();
    private String successMessage;

    private String imageAsBase64;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getImageAsBase64() {
        return imageAsBase64;
    }

    public void setImageAsBase64(String imageAsBase64) {
        this.imageAsBase64 = imageAsBase64;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }


    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }


    public Integer getKsize() {
        return ksize;
    }

    public void setKsize(Integer ksize) {
        this.ksize = ksize;
    }

    public Integer getDiameter() {
        return diameter;
    }

    public void setDiameter(Integer diameter) {
        this.diameter = diameter;
    }

    public Double getSigmaColor() {
        return sigmaColor;
    }

    public void setSigmaColor(Double sigmaColor) {
        this.sigmaColor = sigmaColor;
    }

    public Double getSigmaSpace() {
        return sigmaSpace;
    }

    public void setSigmaSpace(Double sigmaSpace) {
        this.sigmaSpace = sigmaSpace;
    }

    public Integer getBorderType() {
        return borderType;
    }

    public void setBorderType(Integer borderType) {
        this.borderType = borderType;
    }

    public Integer getKernelWidth() {
        return kernelWidth;
    }

    public void setKernelWidth(Integer kernelWidth) {
        this.kernelWidth = kernelWidth;
    }

    public Integer getKernelHeight() {
        return kernelHeight;
    }

    public void setKernelHeight(Integer kernelHeight) {
        this.kernelHeight = kernelHeight;
    }

    public Integer getSigma() {
        return sigma;
    }

    public void setSigma(Integer sigma) {
        this.sigma = sigma;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }


}
