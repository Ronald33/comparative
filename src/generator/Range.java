package generator;

public class Range
{
	private double _start;
	private double _end;
	
	public Range(double start, double end)
	{
		this._start = start;
		this._end = end;
	}
	
	public double getStart() { return this._start; }
	public double getEnd() { return this._end; }
}
