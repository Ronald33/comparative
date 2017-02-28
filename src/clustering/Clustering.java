package clustering;

import java.util.ArrayList;

import clustering.point.Point;

public abstract class Clustering
{
	protected StringBuilder report;
	private long _start;
	private long _end;
	protected ArrayList<Point> points;
	
	public Clustering() { this.report = new StringBuilder(); }
	
	public abstract void execute(ArrayList<Point> points) throws Exception;
	public abstract void saveOutput() throws Exception;
	
	public void start() { this._start = System.currentTimeMillis(); }
	public void end() { this._end = System.currentTimeMillis(); }
	public long getTime() { return this._end - this._start; }
	
	
	protected void setStatistics()
	{
		this.report.append("Data Id: " + this.getPoints().hashCode() + "\n");
		this.report.append("Size: " + this.getPoints().size() + "\n");
		this.report.append("Time: " + this.getTime() + "\n");
	}
	
	public abstract void generateReport() throws Exception;
	
	/* GyS */
	public ArrayList<Point> getPoints() { return points; }
	public void setPoints(ArrayList<Point> points) { this.points = points; }
	/* GyS */
}
