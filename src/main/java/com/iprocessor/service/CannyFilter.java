package com.iprocessor.service;

import com.iprocessor.DTO.ImageDTO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

/**
 * <p> This class provides an API for Canny Edge detection Filter.
 * </p>
 *
 * @author Saurabh Moghe, Abhijeet Sathe
 */
@Component(value = "canny")
public class CannyFilter implements  Chain {

   private final String FILTERTYPE="canny";
    private Chain nextChain;

    @Override
    public void setNextChain(Chain nextChain) {
        this.nextChain=nextChain;
    }

    @Override
    public ImageDTO applyFilter(ImageDTO imageDTO) {

        if(imageDTO.getFilterType().equalsIgnoreCase(FILTERTYPE)){
            nu.pattern.OpenCV.loadShared();
            Mat image = Imgcodecs.imread(imageDTO.getImagePath());
            Mat out= new Mat(image.rows(),image.cols(),image.type());
            Imgproc.cvtColor(image, out, Imgproc.COLOR_BGR2GRAY);
            // Reduce noise with a kernel 3x3
            Imgproc.blur( out, out, new Size(3,3) );
            Imgproc.Canny(out, out,imageDTO.getThreshold1(),imageDTO.getThreshold2(),3,false);
            Mat dest = new Mat();
            Core.add(dest, Scalar.all(0), dest);
            dest=out.clone();
            Imgcodecs.imwrite(imageDTO.getDestinationPath(), dest);
            return imageDTO;
        }else if(nextChain !=null){
            return    nextChain.applyFilter(imageDTO);
        }else {
            return  null;
        }
    }
}
