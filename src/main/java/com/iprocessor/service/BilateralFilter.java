package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;


/**
 * <p> This class provides an API for Bilateral Filter  smoothing filter.
 * </p>
 *
 * @author Saurabh Moghe, Abhijeet Sathe
 */
@Component(value = "bilateral")
public class BilateralFilter implements  Chain {

    public static final String FILTERTYPE="bilateral";

    private Chain nextChain;
    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain=nextChain;
    }

    @Override
    public ImageDTO applyFilter(ImageDTO imageDTO) {
        System.out.println(" in bilateral ");
        if(imageDTO.getFilterType().equalsIgnoreCase(FILTERTYPE)){
            nu.pattern.OpenCV.loadShared();
            Mat image =  Imgcodecs.imread(imageDTO.getImagePath());
            Mat out= new Mat(image.rows(),image.cols(),image.type());
            if (image != null) {
                Imgproc.bilateralFilter(image, out, imageDTO.getDiameter(),imageDTO.getSigmaColor(),imageDTO.getSigmaSpace());
                Imgcodecs.imwrite(imageDTO.getDestinationPath(), out);
            }
            return imageDTO;
        }else{
            if(nextChain!=null) {
                return nextChain.applyFilter(imageDTO);
            }else{
                 imageDTO.getErrorMessage().add(" Filter type not supported "+imageDTO.getFilterType());
                 return  imageDTO;
            }
        }

    }
}
