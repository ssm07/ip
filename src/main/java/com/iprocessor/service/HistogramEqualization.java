package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> This class provides an API  for applying histogram equalization on an image.
 * </p>
 *
 * @author Saurabh Moghe, Abhijeet Sathe
 */
@Component(value = "equalization")
public class HistogramEqualization implements Chain {


    public static final String FILTERTYPE = "equalization";

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
            Imgproc.cvtColor(image, out, Imgproc.COLOR_BGR2YCrCb);
            //split the image into 3 channels
            List<Mat> channels = new ArrayList<>();
            Core.split(out, channels);
            //equalize on Y channel
            Imgproc.equalizeHist(channels.get(0), channels.get(0));
            Core.merge(channels, out);
            Imgproc.cvtColor(out, out, Imgproc.COLOR_YCrCb2BGR);
            Imgcodecs.imwrite(imageDTO.getDestinationPath(), out);
            return imageDTO;
        } else if (this.nextChain != null) {
            return nextChain.applyFilter(imageDTO);
        } else {
            return null;
        }
    }
}
