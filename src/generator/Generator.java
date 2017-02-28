package generator;

import helper.Helper;

public class Generator
{
	public static String generateRange(int size, Range ... ranges)
	{
		StringBuilder sb = new StringBuilder();
		int length = ranges.length;
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < length; j++)
			{
				sb.append(Helper.getRandom(ranges[j]));
				if(j != length - 1) { sb.append(" "); }
			}
			if(i != size - 1) { sb.append("\n"); }
		}
		return sb.toString();
	}
}