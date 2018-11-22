package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;
/**
 * <p> This class provides an API for the laplace filter</p>
 *
 * @author Saurabh Moghe, Abhijeet Sathe
 * */
@Component(value = "laplace")
public class LaplaceFilter implements Chain {

    public static final String FILTERTYPE = "laplace";
    private Chain nextChain;
    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public ImageDTO applyFilter(ImageDTO imageDTO) {
        {
            System.out.println("LaplaceFilter filter");
            if (imageDTO.getFilterType().equalsIgnoreCase(FILTERTYPE)) {
                nu.pattern.OpenCV.loadShared();
                Mat image = Imgcodecs.imread(imageDTO.getImagePath());
                Mat out = new Mat(image.rows(), image.cols(), image.type());
                if (image != null) {
                    if (imageDTO.getKsize() != 0 && imageDTO.getScale() != null && imageDTO.getDelta() != null) {
                        Imgproc.Laplacian(image, out, CvType.CV_8U, imageDTO.getKsize(), imageDTO.getScale(), imageDTO.getDelta());
                    } else {

                        Imgproc.Laplacian(image, out, CvType.CV_8U);
                    }
                    Imgcodecs.imwrite(imageDTO.getDestinationPath(), out);
                }
                return imageDTO;
            } else if (nextChain != null) {
                return nextChain.applyFilter(imageDTO);
            } else {
                return null;
            }

        }


    }
}
