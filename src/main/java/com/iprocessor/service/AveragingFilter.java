package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;


/**
 * <p> This class provides an API for averaging filter
 * </p>
 *
 * @author Saurabh Moghe, Abhijeet Sathe
 */
@Component(value = "averaging")
public class AveragingFilter implements Chain {
    public static final String FILTERTYPE = "averaging";

    private Chain nextChain;

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public ImageDTO applyFilter(ImageDTO imageDTO) {
        System.out.println("averaging");
        if (imageDTO.getFilterType().equalsIgnoreCase(FILTERTYPE)) {
            nu.pattern.OpenCV.loadShared();
            Mat image = Imgcodecs.imread(imageDTO.getImagePath());
            Mat out = new Mat(image.rows(), image.cols(), image.type());
            if (image != null) {
                Imgproc.blur(image, out, new Size(imageDTO.getKernelWidth(), imageDTO.getKernelHeight()), new Point(imageDTO.getPointX(), imageDTO.getPointY()), 1);
                Imgcodecs.imwrite(imageDTO.getDestinationPath(), out);
            }
            return imageDTO;
        } else {
            return nextChain.applyFilter(imageDTO);
        }

    }
}
