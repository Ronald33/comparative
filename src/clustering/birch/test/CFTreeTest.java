package clustering.birch.test;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import clustering.birch.CF;
import clustering.birch.CFTree;
import clustering.birch.Config;
import clustering.birch.Leaf;
import clustering.birch.Node;
import clustering.birch.NonLeaf;
import clustering.point.Point;

public class CFTreeTest
{
	public void firstPoint() throws Exception
	{
		Config.dimention = 2;
		CFTree cft = CFTree.getInstace("test_fp");
		cft.insertPoint(new Point(1, 2));

		Assert.assertEquals(cft.getRoot().getTotal().getN(), 1);
		Assert.assertTrue(Arrays.equals(cft.getRoot().getTotal().getLS(), new double[]{1, 2}));
		Assert.assertTrue(Arrays.equals(cft.getRoot().getTotal().getSS(), new double[]{1, 4}));
	}
	private void verifyNode(Node node)
	{
		if(node.getClass().equals(NonLeaf.class)) { Assert.assertTrue(node.getSize() <= Config.B); }
		if(node.getClass().equals(Leaf.class)) { Assert.assertTrue(node.getSize() <= Config.L); }
		
		CF total = node.getTotal();
		CF total_real = new CF();
		int size = node.getSize();
		for(int i = 0; i < size; i++) { total_real.add(node.getEntryByIndex(i)); }
		
		Assert.assertEquals(total.getN(), total_real.getN());
//		if(!Arrays.equals(total.getLS(), total_real.getLS()))
//		{
//			System.out.println(Arrays.toString(total.getLS()));
//			System.out.println(Arrays.toString(total_real.getLS()));
//		}
		Assert.assertTrue(Arrays.equals(total.getLS(), total_real.getLS()));
		Assert.assertTrue(Arrays.equals(total.getSS(), total_real.getSS()));
		
		if(node.getClass().equals(NonLeaf.class))
		{
			NonLeaf non_leaf = (NonLeaf) node;
			for(int i = 0; i < size; i++)
			{
				Node child = non_leaf.getChildByIndex(i); 
				verifyNode(child);
			}
		}
	}
	public void verify() throws Exception
	{
		Config.dimention = 2;
		ArrayList<Point> points = clustering.Helper.fileToPoints(Config.files + "test/100_2");
		CFTree cft = CFTree.getInstace("test_verify");
		cft.execute(points);
		
		NonLeaf root = cft.getRoot();
		Assert.assertEquals(root.getTotal().getN(), points.size());
		if(root != null) { this.verifyNode(root); }
	}
	public void verifyLeafs() throws Exception
	{
		Config.dimention = 2;
		ArrayList<Point> points = clustering.Helper.fileToPoints(Config.files + "millones/1");
		CFTree cft = CFTree.getInstace("test_vl");;
		cft.execute(points);
		Leaf firstLeaf = cft.getFirstLeaf(), pointer = firstLeaf;
		int counter = 0;
		while(pointer != null)
		{
			counter++;
			pointer = pointer.getNext();
		}
		System.out.println("counter: " + counter);
	}
	@Test
	public void app()
	{
		try
		{
//			this.firstPoint();
//			this.verify();
			this.verifyLeafs();			
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
