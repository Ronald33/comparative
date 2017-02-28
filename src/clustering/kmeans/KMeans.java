package clustering.kmeans;

import java.util.ArrayList;
import java.util.Arrays;

import clustering.Clustering;
import clustering.point.Point;
import helper.Helper;

public class KMeans extends Clustering
{
	private static int k = Config.k;
	private static int maxIterations = Config.maxIterations;
	
	private static int decimals = 3;
	private Point _clusters[];
	
	public KMeans() { this._clusters = new Point[k]; }
	private void setCentroids() throws Exception
	{
		int size = this.getPoints().size();
		if (k >= 0 && k <= size )
		{
			int centroid_index[] = new int[k];
			int selected, index = 0;

			do
			{
				selected = (int) Helper.getRandom(0, size - 1);
				if (!Helper.in_array(selected, centroid_index))
				{
					this._clusters[index] = this.getPoints().get(index).clone();
					centroid_index[index] = selected;
					index++;
				}
			}
			while (index != k);
		}
		else { throw new Exception("The value of K is incorrect"); }
	}
	private void setClusters() throws Exception
	{
		int size_points = this.getPoints().size(), size_clusters = this._clusters.length, iterations = 0;
		boolean moved;

		do
		{
			double summation[][] = new double[size_clusters][Config.dimention];
			int size_cluster[] = new int[size_clusters];

			for (int i = 0; i < size_points; i++)
			{
				Point p = this.getPoints().get(i);
				double distance_min = Double.MAX_VALUE;
				int index = 0;

				// Detectamos el centroide mas cercano
				for (int j = 0; j < size_clusters; j++)
				{
					double distance = p.getEuclideanDistance(this._clusters[j]);
					if (distance < distance_min) { distance_min = distance; index = j; }
				}
				
//				p.setCluster(Helper.getIdByHashCode(this._clusters[index]));

				// Vamos acumulando los puntos que pertenecen al cluster
				for (int j = 0; j < Config.dimention; j++) { summation[index][j] += this.getPoints().get(i).getValues()[j]; }
				// Vamos sumando la cantidad de puntos que pertenece al cluster
				size_cluster[index]++;
			}
			
			moved = this.moveCentroids(summation, size_cluster); // Movemos los centroides

			iterations++;
			if (iterations != 0 && iterations == maxIterations) { break; }
		}
		while (moved);
	}
	private boolean moveCentroids(double summation[][], int size_cluster[])
	{
		int size_clusters = this._clusters.length;
		boolean moved = false;
		for (int i = 0; i < size_clusters; i++)
		{
			Point centroid = this.getClusters()[i];
			double values[] = Helper.getAverage(summation[i], size_cluster[i], KMeans.decimals);
			if (!Arrays.equals(centroid.getValues(), values))
			{
				centroid.setValues(values);
				moved = true;
			}
		}
		return moved;
	}
	public Point[] getClusters() { return _clusters; }
	public void setClusters(Point[] _clusters) { this._clusters = _clusters; }
	
	@Override
	public void execute(ArrayList<Point> points) throws Exception
	{
		this.start();
		this.points = points;
		this.setCentroids();
		this.setClusters();
		this.end();
	}
	@Override
	public void generateReport() throws Exception
	{
		this.setStatistics();
		this.report.append("Configuration\n");
		this.report.append("Dimention: " + Config.dimention + "\n");
		this.report.append("K: " + k + "\n");
		this.report.append("Max. Iterations: " + maxIterations + "\n");
		helper.Helper.writeFile("kmeans_report", true, this.report.toString());
	}
	@Override
	public void saveOutput() throws Exception
	{
		this.generateReport();
		clustering.Helper.pointsToFile(this.getPoints(), this._clusters, "kmeans_points");
	}
}