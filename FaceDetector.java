package TemplateMatching;

 
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.github.sarxos.webcam.Webcam;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import javafx.embed.swing.SwingFXUtils;


 
public class FaceDetector 
{	public static KinectTry kinect = new KinectTry();
	public static Mat Webcam() throws IOException
	{
		

		String file = "/Users/mohit/Desktop/MidTerm/1/webcamcapture1.jpg";
		Mat src = Imgcodecs.imread(file);
		return src;
		
	}
	public static Mat CannyEdgeDetection(Mat src) {
	   

    // Creating an empty matrix to store the result
    Mat gray = new Mat();

    // Converting the image from color to Gray
    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
    Mat edges = new Mat();

    // Detecting the edges
    Imgproc.Canny(gray, edges, 100, 200);

    // Writing the image
    Imgcodecs.imwrite("/Users/mohit/Desktop/Midterm/1/CannyEdge.jpg", edges);
    return edges;
 
}
	public static  void filter (Mat merge, Mat mat) throws IOException {

	    final Mat dst = new Mat(merge.rows(), merge.cols(), merge.type());
	    merge.copyTo(dst);

	    	final List<MatOfPoint> points = new ArrayList<>();
	    final Mat hierarchy = new Mat();
	    Imgproc.findContours(dst, points, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	    
	    Imgproc.cvtColor(dst, dst, Imgproc.COLOR_GRAY2BGR);
		if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
		{
		        // for each contour, display it in blue
		        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
		        {
		                Imgproc.drawContours(dst, points, idx, new Scalar(255,255,255), -1);
		                Imgproc.drawContours(mat, points, idx, new Scalar(255,255,255), -1);
		        }
		}
		String filename = "/Users/mohit/Desktop/MidTerm/1/FilteredGRY.jpg";
		String file = "/Users/mohit/Desktop/Midterm/1/FilteredCOLOR.jpg";
		Imgcodecs.imwrite(file, mat);
		Imgcodecs.imwrite(filename, dst);
		
	}
	
	public static Mat FaceDetector(Mat image)
	{	 CascadeClassifier faceDetector = new CascadeClassifier();
    		faceDetector.load("/Users/mohit/eclipse-workspace/MidTerm/src/TemplateMatching/FaceDetectorClassifier.xml");
		 MatOfRect faceDetections = new MatOfRect();
	        faceDetector.detectMultiScale(image, faceDetections);
	        Rect rectCrop = null;
	        if(faceDetections.toArray().length!=0) {
	        // Creating a rectangular box showing faces detected
	        for(int i =1; i<=faceDetections.toArray().length;i++)
	        { 
	        for (Rect rect : faceDetections.toArray())
	        {
	        	Imgproc.rectangle(
	                    image,                                   //where to draw the box
	                    new Point(rect.x, rect.y),                            //bottom left
	                    new Point(rect.x + rect.width, rect.y + rect.height), //top right
	                    new Scalar(0, 0, 255) );                          //RGB colour
	                
	            rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
	           
	        }}}
	        Mat image_roi = new Mat(image,rectCrop);
            Imgcodecs.imwrite("/Users/mohit/Downloads/5.jpg",image_roi);
	        Imgcodecs.imwrite("/Users/mohit/Downloads/10.jpg", image);
		return image_roi;
	}
	public int[] GetColorFrame( byte[] array) throws IOException
	{    
		Mat mat = new Mat(kinect.getColorWidth(),kinect.getColorHeight(),CvType.CV_32FC1);
		mat.put(0, 0, array);
		Mat image = new Mat();
		image = Webcam();
		Mat templFile = new Mat();
		templFile = FaceDetector(image);
		int[] data = new int[4];
		
        data = MatchingTemplate(mat,templFile,"/Users/mohit/Downloads/Matched.jpg",Imgproc.TM_SQDIFF);
        return data;
	}
public static void GetLocation(float XYZ) 
{
	double latc = 32.986412;
	double lonc = -96.747763;
	double radlatc = lonc * (Math.PI/180);
	double radlonc = latc * (Math.PI/180);
	double x = Math.cos(radlatc) * Math.cos(radlonc);
	double y = Math.cos(radlatc) * Math.sin(radlonc);
	double zc = Math.cos(radlatc);
	double z = (double) XYZ+zc;
	double dis = Math.pow(x, 2) + Math.pow(y, 2);
	double hyp = Math.sqrt(dis);
	double radlat = Math.atan2(z, hyp);
	double lat = latc;
	double lon = radlat * (180/Math.PI);
	browser(lat,lon);
	}

public static void browser(double lat,double lon)
{
	String url = "www.google.com/maps/@"+lat+","+lon;
	if(Desktop.isDesktopSupported()==true)
	{
		Desktop desktop = Desktop.getDesktop();
		try
		{
			desktop.browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
	}
}
public static void FingerprintMatching(BufferedImage croppedimage,Mat templ)
{
	  MatOfByte bytemat = new MatOfByte();

	    Imgcodecs.imencode(".jpg", templ, bytemat);

	    byte[] bytes = bytemat.toArray();

	    InputStream in = new ByteArrayInputStream(bytes);

	    BufferedImage image = null;
		try {
			image = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[][] vals = new int[templ.cols()][templ.rows()];
		int[][] vala = new int[templ.cols()] [templ.rows()];
		int counter =0;
		for(int i=0;i<=templ.cols();i++)
		{
			for(int j=0; j<=templ.rows();j++)
			{
				vals[i][j] = image.getRGB(i, j);
				vala[i][j] = croppedimage.getRGB(i, j);
				if(vals == vala)
					counter++;
			}
		}
		System.out.println("Matching = "+counter/(templ.cols()*templ.rows()));
}

public static int[] MatchingTemplate(Mat img, Mat templ, String outFile, int match_method) {
	    System.out.println("\nRunning Template Matching");
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
	    int[] data = new int[4];
	    // / Show me what you got
	    Imgproc.rectangle(img, matchLoc, new Point(matchLoc.x + templ.cols(),
	            matchLoc.y + templ.rows()), new Scalar(255, 0, 0));
	   
	    MatOfByte bytemat = new MatOfByte();

	    Imgcodecs.imencode(".jpg", img, bytemat);

	    byte[] bytes = bytemat.toArray();

	    InputStream in = new ByteArrayInputStream(bytes);

	    BufferedImage image = null;
		try {
			image = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    BufferedImage croppedimage = image.getSubimage((int)matchLoc.x, (int)matchLoc.y, templ.cols(), templ.rows());
	    FingerprintMatching(croppedimage,templ);
	    int x = (int) matchLoc.x;
	    	int y = (int) matchLoc.y;
	    	data[0] = x;
	    	data[1] = y;
	    	data[2] = templ.width();
	    	data[3] = templ.height();
	    // Save the visualized detection.
	    System.out.println("Writing "+ outFile);
	    Imgcodecs.imwrite(outFile, img);

	    System.out.println("MinVal "+ minminvalue);
	    System.out.println("MaxVal "+ maxmaxvalue);

	    System.out.println("xVal "+ matchLoc.x);
	    System.out.println("yVal "+ matchLoc.y);
	    return data;
	    
	}
 
	
}