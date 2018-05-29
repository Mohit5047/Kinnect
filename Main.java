package TemplateMatching;

import java.io.IOException;

import org.opencv.core.Core;

import edu.ufl.digitalworlds.j4k.J4KSDK;

public class Main {
	static KinectTry kinect = new KinectTry();
	   public static void main(String[] args) throws IOException
	    {
	        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	        
	        kinect.start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.XYZ);        
	        try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        kinect.stop();
	    }
}
