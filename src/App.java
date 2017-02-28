import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import clustering.Config;
import clustering.birch.CFTree;
import clustering.point.Point;
import generator.Range;
import helper.Helper;

public class App
{
//	public static ArrayList<Point> generateData() throws Exception
//	{
//		String content1 = Generator.generateRange(10000000, new Range(1, 10), new Range(1,  10));
//		String content2 = Generator.generateRange(10000000, new Range(11, 20), new Range(11,  20));
//		String content3 = Generator.generateRange(10000000, new Range(21, 30), new Range(21,  30));
//		String filename = Helper.writeFile("points", true, content1, content2, content3);
//		return clustering.Helper.fileToPoints(Config.files + filename);
//	}
	
	public static ArrayList<Point> generateData() throws Exception
	{
		int size = (int) Math.pow(10, 6);
		Range r1[] = new Range[]{new Range(0, 0.6), new Range(0, 0.3)};
//		Range r2[] = new Range[]{new Range(11, 20), new Range(11, 20)};  
//		Range r3[] = new Range[]{new Range(21, 30), new Range(21, 30)};
		String filename = Helper.writeFile("points", true, new int[]{size}, new Range[][]{r1});
		return clustering.Helper.fileToPoints(Config.files + filename);
	}
	
	public static void main(String[] args)
	{
		try
		{
//			ArrayList<Point> points = generateData(); 
//			/*
			for(int i = 0; i < 5; i++)
			{
				ArrayList<Point> points = clustering.Helper.fileToPoints(Config.files + "dataset/birch/1");
			}
//			ArrayList<Point> points = clustering.Helper.fileToPoints(Config.files + "dataset/birch/1");
//			ArrayList<Point> points = clustering.Helper.fileToPoints(Config.files + "millones/1");
			
//			CFTree cft = new CFTree();
//			CFTree cft = CFTree.getInstace("cft");
//			cft.execute(points);
//			cft.saveOutput();
//			cft.saveToFile();
			
//			cft.
//			*/
			
//			Clustering km = new KMeans();
//			km.execute(points);
//			km.saveOutput();
			
			System.out.println("Comparative1");
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
