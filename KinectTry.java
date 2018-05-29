package TemplateMatching;

import java.io.IOException;
import TemplateMatching.FaceDetector;


import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.J4KSDK;

public class KinectTry extends J4KSDK {
	int data[] = new int[4];
FaceDetector main = new FaceDetector();

	
	@Override
	public void onColorFrameEvent(byte[] arg0) {
		// TODO Auto-generated method stub
	try {
		data = main.GetColorFrame(arg0);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	@Override
	public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
		// TODO Auto-generated method stub
		DepthMap map = new DepthMap(getDepthWidth(),getDepthHeight(),arg2);
		int x = data[0];
		int y = data[1];
		int width = data[2];
		int height = data[3];
		if(map.validDepthAt(x, y)==true)
		map.maskRect(x, y, width, height);
		int value = x + (y*width);
		float z = arg2[value];
		main.GetLocation(z);
		map.saveWRL("/Users/mohit/Downloads/a.wrl");
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] arg0, float[] arg1, float[] arg2, byte[] arg3) {
		// TODO Auto-generated method stub
		
	}

}
