package helper;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Random;

import clustering.Config;
import generator.Range;

public abstract class Helper
{
	private static HashMap<Integer, Integer> ids = new HashMap<>();
	private static int counter_id = 0;
	private static String getFilename(String name)
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return name + "_" + timestamp.getTime(); 
	}
	
	public static String writeFile(String name, boolean createLink, int[] sizes, Range ranges[][]) throws Exception
	{
		int size_length = sizes.length, ranges_length = ranges.length;
		if(size_length == ranges_length)
		{
			String filename = getFilename(name);
			FileWriter fw = new FileWriter(Config.files + filename);
			
			for(int i = 0; i < size_length; i++)
			{
				int size = sizes[i];
				Range[] range = ranges[i];
				int length = range.length;
				for(int j = 0; j < size; j++) // for each line
				{
					for(int k = 0; k < length; k++)
					{
						fw.write(Double.toString(Helper.getRandom(range[k])));
						if(k != length - 1) { fw.write(" "); }
					}
					fw.write("\n");
				}
			}
			fw.close();
			if(createLink) { createLink(filename, name); }
			return filename;
		}
		else { throw new Exception("The sizes is different that the size of ranges"); }
		
	}
	
	private static String writeFile(String name, boolean createLink, String content) throws Exception 
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String filename =  name + "_" + timestamp.getTime();
		FileWriter fw = new FileWriter(Config.files + filename);
		fw.write(content);
		fw.close();
		if(createLink) { createLink(filename, name); }
		return filename;
	}
	public static String writeFile(String filename, boolean createLink, String ... contents) throws Exception 
	{
		StringBuilder sb = new StringBuilder();
		int size = contents.length;
		for(int i = 0; i < size; i++)
		{
			sb.append(contents[i]);
			if(i != size - 1) { sb.append("\n"); }
		}
		return writeFile(filename, createLink, sb.toString());
	}
	public static boolean deleteFile(String filename) throws Exception
	{
		File file = new File(filename);
		if(file.delete()) { return true; }
		else { return false; }
	}
	public static void createLink(String file, String name) throws Exception
	{
		Path _target = Paths.get(file);
		String link = Config.files + name + "_latest";
		deleteFile(link);
		Path _path = Paths.get(link);
		Files.createSymbolicLink(_path, _target);
	}
	public static double getRandom(double start, double end)
	{
//		DecimalFormat df = new DecimalFormat("#.####");
		Random r = new Random();
		double result = r.nextDouble() * (end - start) + start;
		return Math.round(result * 10000.0) / 10000.0;
	}
	public static double getRandom(Range range) { return getRandom(range.getStart(), range.getEnd()); }
	
	// KM
	public static boolean in_array(int value, int array[])
	{
		int size = array.length;
		for (int i = 0; i < size; i++) { if(array[i] == value) { return true; } }
		return false;
	}
	public static double round(double value, int places)
	{
	    if (places < 0) throw new IllegalArgumentException();
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	public static double[] getAverage(double values[], int total, int decimals)
	{
		int dimention = values.length;
		double result[] = new double[dimention];
		for (int i = 0; i < dimention; i++)
		{
			result[i] = round(values[i] / total, decimals);
		}
		return result;
	}
	public static int getIdByHashCode(Object obj)
	{
		int hash = obj.hashCode();
		if(ids.get(hash) != null) { return ids.get(hash); }
		else
		{
			counter_id++;
			ids.put(hash, counter_id);
			return counter_id;
		}
	}
}