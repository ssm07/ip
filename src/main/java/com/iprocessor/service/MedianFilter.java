package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;


/**
 * <p> This class provides an API for the  Median Filter<p/>
 *
 * @author  Saurabh Moghe, Abhijeet Sathe
 * */
@Component(value = "median")
class MedianFilter implements Chain {
    public static final String FILTERTYPE = "median";

    private Chain nextChain;

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public ImageDTO applyFilter(ImageDTO imageDTO) {
        System.out.println("median filter");
        if (imageDTO.getFilterType().equalsIgnoreCase(FILTERTYPE)) {
            nu.pattern.OpenCV.loadShared();
            Mat image = Imgcodecs.imread(imageDTO.getImagePath());
            Mat out = new Mat(image.rows(), image.cols(), image.type());
            if (image != null) {
                Imgproc.medianBlur(image, out, imageDTO.getKsize());
                Imgcodecs.imwrite(imageDTO.getDestinationPath(), out);

            }
            return imageDTO;
        } else {
            return nextChain.applyFilter(imageDTO);
        }

    }
}
