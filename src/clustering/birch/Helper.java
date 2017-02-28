package clustering.birch;

public class Helper
{
	public static int getIndexClosest(Node node, CF cf)
	{
		int size = node.getSize(), min_index = 0;
		double min_distance = Double.MAX_VALUE;
		for(int i = 0; i < size; i++)
		{
			double distance = node.getEntryByIndex(i).euclideanDistance(cf);
			if(distance < min_distance) { min_distance = distance; min_index = i; }
		}
		return min_index;
	}
	public static int[] getFarthest(Node node)
	{
		int size = node.getSize(), index_1 = 0, index_2 = 0;
		double distance_greater = 0;
		for(int i = 0; i < size - 1; i++)
		{
			for(int j = i + 1; j < size; j++)
			{
				CF cf1 = node.getEntryByIndex(i);
				CF cf2 = node.getEntryByIndex(j);
				double distance = cf1.distance(cf2);
				if(distance > distance_greater)
				{
					index_1 = i;
					index_2 = j;
					distance = distance_greater;
				}
			}
		}
		return new int[]{index_1, index_2};
	}
	public static int[] getClosest(Node node)
	{
		int size = node.getSize(), index_1 = 0, index_2 = 0;
		double distance_lower = Double.MAX_VALUE;
		for(int i = 0; i < size - 1; i++)
		{
			for(int j = i + 1; j < size; j++)
			{
				CF cf1 = node.getEntryByIndex(i);
				CF cf2 = node.getEntryByIndex(j);
				double distance = cf1.distance(cf2);
				if(distance < distance_lower)
				{
					index_1 = i;
					index_2 = j;
					distance = distance_lower;
				}
			}
		}
		return new int[]{index_1, index_2};
	}
}