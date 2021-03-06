package TemplateMatching;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class MatchingDemo {
	
public static void run(String inFile, String templateFile, String outFile, int match_method) {
    System.out.println("\nRunning Template Matching");

    Mat img = Imgcodecs.imread(inFile);
    Mat templ = Imgcodecs.imread(templateFile);

    double minlocvalue = 7;
    double maxlocvalue = 7;

    double minminvalue = 7;
    double maxmaxvalue = 7;


    // / Create the result matrix
    int result_cols = img.cols() - templ.cols() + 1;
    int result_rows = img.rows() - templ.rows() + 1;
    Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

    // / Do the Matching and Normalize
    Imgproc.matchTemplate(img, templ, result, match_method);
    Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

    // / Localizing the best match with minMaxLoc
    MinMaxLocResult mmr = Core.minMaxLoc(result);

    Point matchLoc;
    if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
        matchLoc = mmr.minLoc;
        minminvalue = mmr.minVal; // test 
    } else {
        matchLoc = mmr.maxLoc;
        maxmaxvalue = mmr.minVal; // test
    }

    // / Show me what you got
    Imgproc.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
            matchLoc.y + templ.rows()), new Scalar(0, 255, 0));

    // Save the visualized detection.
    System.out.println("Writing "+ outFile);
    Imgcodecs.imwrite(outFile, img);

    System.out.println("MinVal "+ minminvalue);
    System.out.println("MaxVal "+ maxmaxvalue);

    System.out.println("xVal "+ matchLoc.x);
    System.out.println("yVal "+ matchLoc.y);

}
public static void main(String[] args) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
   run("rabbit.jpg", "rab_head.png", "rab_out.jpg", Imgproc.TM_SQDIFF);
}
}