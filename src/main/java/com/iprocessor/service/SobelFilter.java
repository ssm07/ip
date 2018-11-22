package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

/**
 * <p> This class provides an API for sobel filter
 * </p>
 *
 * @author Saurabh Moghe, Abhijeet Sathe
 */
@Component(value = "sobel")
public class SobelFilter implements Chain {

    public static final String FILTERTYPE = "sobel";

    private Chain nextChain;

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public ImageDTO applyFilter(ImageDTO imageDTO) {
        if (imageDTO.getFilterType().equalsIgnoreCase(FILTERTYPE)) {
            nu.pattern.OpenCV.loadShared();
            Mat image = Imgcodecs.imread(imageDTO.getImagePath());
            Mat out = new Mat(image.rows(), image.cols(), image.type());
            Imgproc.Sobel(image, out, CvType.CV_8U, imageDTO.getDx(), imageDTO.getDy());
            Imgcodecs.imwrite(imageDTO.getDestinationPath(), out);
            return imageDTO;
        } else if (nextChain != null) {
            return nextChain.applyFilter(imageDTO);
        } else {
            return null;
        }

    }
}
