package clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import clustering.point.Point;

public abstract class Helper
{
	private static double[] lineToDouble(String line)
	{
		String[] ls = line.split("\\s+");
		int dimention = ls.length;
		double values[] = new double[dimention];
		for (int i = 0; i < dimention; i++) { values[i] = Double.parseDouble(ls[i]); }
		return values;
	}
	public static ArrayList<Point> fileToPoints(String filename) throws IOException
	{
		ArrayList<Point> points = new ArrayList<>();
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line = null;
		while ((line = br.readLine()) != null)
		{
			double values[] = lineToDouble(line);
			points.add(new Point(values));
		}
		br.close();
		return points;
	}
	public static double getDistance(double a[], double b[]) throws Exception
	{
		double distance = 0;
		int length = a.length;
		if(length == b.length)
		{
			for(int i = 0; i < length; i++)
			{
				double difference = a[i] - b[i]; 
				distance += Math.pow(difference, 2); 
			}
		}
		else { throw new Exception("Length Incorrect"); }
		return Math.sqrt(distance);
	}
	public static int getClosest(Point point, Point points[]) throws Exception
	{
		int points_size = points.length, max_index = 0;
		double max_distance = Double.MAX_VALUE;
		for(int i = 0; i < points_size; i++)
		{
			double distance = getDistance(point.getValues(), points[i].getValues());
			if(distance < max_distance) { max_distance = distance; max_index = i; }
		}
		return max_index;
	}
	public static String pointsToFile(ArrayList<Point> points, ArrayList<Point> centroids, String name) throws Exception
	{
		Point array_centroids[] = centroids.toArray(new Point[centroids.size()]);
		return pointsToFile(points, array_centroids, name);
	}
	public static String pointsToFile(ArrayList<Point> points, Point centroids[], String name) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		int size_points = points.size();
    	for (int i = 0; i < size_points; i++)
    	{
    		double values[] = points.get(i).getValues();
    		int size_values = values.length;
    		for(int j = 0; j < size_values; j++) { sb.append(String.valueOf(values[j]) + " "); }
    		sb.append(getClosest(points.get(i), centroids) + "\n");
//    		sb.append(points.get(i).getCluster() + "\n");
    	}
    	return helper.Helper.writeFile(name, true, sb.toString());
	}
}