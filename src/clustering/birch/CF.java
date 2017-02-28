package clustering.birch;

import java.io.Serializable;
import java.util.Arrays;
import clustering.point.Point;

public class CF implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int dimention;
	private int n;
	private double SS[];
	private double LS[];
	
	private void initialize(int dimention)
	{
		this.dimention = dimention;
		this.n = 0;
		this.SS = new double[this.dimention];
		this.LS = new double[this.dimention];
	}
	public CF()
	{
		this.initialize(Config.dimention);
		for(int i = 0; i < this.dimention; i++)
		{
			LS[i] = 0;
			SS[i] = 0;
		}
	}
	public CF(Point point) throws Exception
	{
		if(point.getValues().length == Config.dimention)
		{
			this.initialize(Config.dimention);
			this.n++;
			for(int i = 0; i < this.dimention; i++)
			{
				LS[i] = point.getValues()[i];
				SS[i] = Math.pow(point.getValues()[i], 2);
			}
		}
		else { throw new Exception("Size incorrect"); }
	}
	public double euclideanDistance(CF cf)
	{
		double distance = 0;
		for(int i = 0; i < this.dimention; i++)
		{
			double difference = (this.getLS()[i] / this.getN()) - (cf.getLS()[i] / cf.getN());
			distance += Math.pow(difference, 2);
		}
		return Math.sqrt(distance);
	}
	public double distance(CF cf)
	{
		double distance = 0;
		for(int i = 0; i < this.dimention; i++)
		{
			distance += this.SS[i] / this.n 
					+ cf.getSS()[i] / cf.getN() 
					- 2 * this.LS[i] * cf.getLS()[i] 
					/ (this.n * cf.getN());
		}
		return Math.sqrt(distance);
	}
	public double diameter()
	{
		double diameter = 0;
		int n = this.getN(), dimention = this.dimention;
		for(int i = 0; i < dimention; i++)
		{
			double ls = this.getLS()[i], ss = this.getSS()[i];
			diameter += 2 * n * ss - 2 * ls * ls;
		}
		diameter = diameter / (n * n - n);
		return Math.sqrt(diameter);
	}
	private void addOrRemove(CF cf, boolean add)
	{
		int i_add = add ? 1 : -1;
		this.n += cf.getN() * i_add;
		for(int i = 0; i < this.dimention; i++)
		{
			this.getLS()[i] += cf.getLS()[i] * i_add;
			this.getSS()[i] += cf.getSS()[i] * i_add;
		}
	}
	public void add(CF cf) { this.addOrRemove(cf, true); }
	public void remove(CF cf) { this.addOrRemove(cf, false); }
	public double[] getCentroid()
	{
		double centroid[] = new double[dimention];
		for(int i = 0; i < dimention; i++)
		{
			centroid[i] = this.getLS()[i] / this.getN();
		}
		return centroid;
	}
	public CF clone()
	{
		CF clone = new CF();
		clone.setN(this.getN());
		clone.setLS(this.getLS().clone());
		clone.setSS(this.getSS().clone());
		return clone;
	}
	@Override
	public String toString() { return "[" + n + ", " + Arrays.toString(this.getLS()) + ", " + Arrays.toString(this.getSS()) + "]"; }
	/*GyS*/
	public int getDimention() { return dimention; }
	public void setDimention(int dimention) { this.dimention = dimention; }
	public int getN() { return n; }
	public void setN(int n) { this.n = n; }
	public double[] getSS() { return SS; }
	public void setSS(double[] SS) { this.SS = SS; }
	public double[] getLS() { return LS; }
	public void setLS(double[] LS) { this.LS = LS; }
	/*GyS*/
}