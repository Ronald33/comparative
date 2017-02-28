package clustering.point;

public class Point
{
	private double values[];
//	private int cluster;
	
	public Point(double ... values)
	{
		this.values = values;
//		this.cluster = -1;
	}

	public void setValues(double ... values) { this.values = values; }
	public double[] getValues() { return values; }
//	public int getCluster() { return cluster; }
//	public void setCluster(int cluster) { this.cluster = cluster; }
	public Point clone()
	{
		Point clone = new Point(this.getValues().clone());
//		clone.setCluster(this.cluster);
		return clone;
	}
	public double getEuclideanDistance(Point point) throws Exception
	{
		return clustering.Helper.getDistance(this.values, point.getValues());
//		double distance = 0;
//		for(int i = 0; i < Config.dimention; i++)
//		{
//			double difference = this.getValues()[i] - point.getValues()[i]; 
//			distance += Math.pow(difference, 2);
//		}
//		return Math.sqrt(distance);
	}
}
