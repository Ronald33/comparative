package clustering.birch.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import clustering.birch.CF;
import clustering.birch.Config;
import clustering.point.Point;

public class CFTest
{
	@Test
	public void add()
	{
		try
		{
			Config.dimention = 2;
			int x1 = 1, y1 = 2, x2 = 3, y2 = 4;
			CF cf1 = new CF(new Point(new double[]{x1, y1}));
			CF cf2 = new CF(new Point(new double[]{x2, y2}));
			cf1.add(cf2);
			
			Assert.assertEquals(cf1.getN(), 2);
			Assert.assertTrue(Arrays.equals(cf1.getLS(), new double[]{4, 6}));
			Assert.assertTrue(Arrays.equals(cf1.getSS(), new double[]{10, 20}));
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	@Test
	public void remove()
	{
		try
		{
			Config.dimention = 2;
			int x1 = 1, y1 = 2, x2 = 3, y2 = 4, x3 = 2, y3 = 1;
			CF cf1 = new CF(new Point(new double[]{x1, y1}));
			CF cf2 = new CF(new Point(new double[]{x2, y2}));
			CF cf3 = new CF(new Point(new double[]{x3, y3}));
			cf1.add(cf2);
			cf1.remove(cf3);
			
			Assert.assertEquals(cf1.getN(), 1);
			Assert.assertTrue(Arrays.equals(cf1.getLS(), new double[]{2, 5}));
			Assert.assertTrue(Arrays.equals(cf1.getSS(), new double[]{6, 19}));
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
