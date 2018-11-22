package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;


/**
 * <p> This class provides an API for Gaussian Filter  smoothing filter.
 * </p>
 *
 * @author Saurabh Moghe, Abhijeet Sathe
 */
@Component(value = "gaussian")
public class GaussianFilter implements Chain {

    public static final String FILTERTYPE = "gaussian";

    private Chain nextChain;

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public ImageDTO applyFilter(ImageDTO imageDTO) {
        System.out.println(" in  gaussian ");
        if (imageDTO.getFilterType().equalsIgnoreCase(FILTERTYPE)) {
            nu.pattern.OpenCV.loadShared();
            Mat image = Imgcodecs.imread(imageDTO.getImagePath());
            Mat out = new Mat(image.rows(), image.cols(), image.type());
            if (image != null) {
                Imgproc.GaussianBlur(image, out, new Size(imageDTO.getKernelWidth(), imageDTO.getKernelHeight()), imageDTO.getSigma());
                Imgcodecs.imwrite(imageDTO.getDestinationPath(), out);
            } else {
                StringBuilder errorMsg = new StringBuilder(" Unable to read input image");
                imageDTO.getErrorMessage().add(errorMsg.toString());
            }

            return imageDTO;
        } else {

            return nextChain.applyFilter(imageDTO);
        }

    }
}
