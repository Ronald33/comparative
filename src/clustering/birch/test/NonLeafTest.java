package clustering.birch.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import clustering.birch.Config;
import clustering.birch.Leaf;
import clustering.birch.NonLeaf;
import clustering.point.Point;

public class NonLeafTest
{
	@Test
	public void transfer() throws Exception
	{
		Config.dimention = 2;
		Leaf leaf1 = new Leaf();
		leaf1.addPoint(new Point(1, 2));
		Leaf leaf2 = new Leaf();
		leaf2.addPoint(new Point(3, 4));
		
		NonLeaf nonLeaf1 = new NonLeaf();
		nonLeaf1.addChildAndAddTotal(leaf1);
		nonLeaf1.addChildAndAddTotal(leaf2);
		NonLeaf nonLeaf2 = new NonLeaf(); 
		nonLeaf1.transfer(nonLeaf2, 0);
		nonLeaf1.transfer(nonLeaf2, 0);
		
		Assert.assertEquals(nonLeaf1.getChildren().size(), 0);
		Assert.assertEquals(nonLeaf1.getTotal().getN(), 0);
		Assert.assertTrue(Arrays.equals(nonLeaf1.getTotal().getLS(), new double[]{0, 0}));
		Assert.assertTrue(Arrays.equals(nonLeaf1.getTotal().getSS(), new double[]{0, 0}));

		Assert.assertEquals(nonLeaf2.getChildren().size(), 2);
		Assert.assertEquals(nonLeaf2.getTotal().getN(), 2);
		Assert.assertTrue(Arrays.equals(nonLeaf2.getTotal().getLS(), new double[]{4, 6}));
		Assert.assertTrue(Arrays.equals(nonLeaf2.getTotal().getSS(), new double[]{10, 20}));
	}
	@Test
	public void transferFromBrother()
	{
		try
		{
			Config.dimention = 2;
			Config.T = 0;
			
			Leaf leaf1 = new Leaf();
			leaf1.addPoint(new Point(1, 2));
			Leaf leaf2 = new Leaf();
			leaf2.addPoint(new Point(3, 4));
			
			NonLeaf nonLeaf1 = new NonLeaf();
			nonLeaf1.addChildAndAddTotal(leaf1);
			
			NonLeaf nonLeaf2 = new NonLeaf();
			nonLeaf2.addChildAndAddTotal(leaf2);
			
			NonLeaf parent = new NonLeaf();
			parent.addChildAndAddTotal(nonLeaf1);
			parent.addChildAndAddTotal(nonLeaf2);
			
			nonLeaf1.transferFromBrother(1);
			
			Assert.assertEquals(nonLeaf1.getChildren().size(), 2);
			Assert.assertEquals(nonLeaf1.getTotal().getN(), 2);
			Assert.assertTrue(Arrays.equals(nonLeaf1.getTotal().getLS(), new double[]{4, 6}));
			Assert.assertTrue(Arrays.equals(nonLeaf1.getTotal().getSS(), new double[]{10, 20}));
			Assert.assertEquals(nonLeaf1.getChildren().get(0), leaf1);
			Assert.assertEquals(nonLeaf1.getChildren().get(1), leaf2);
			
			Assert.assertEquals(parent.getChildren().size(), 1);
			Assert.assertEquals(parent.getTotal().getN(), 2);
			Assert.assertTrue(Arrays.equals(parent.getTotal().getLS(), new double[]{4, 6}));
			Assert.assertTrue(Arrays.equals(parent.getTotal().getSS(), new double[]{10, 20}));
			Assert.assertEquals(parent.getChildren().get(0), nonLeaf1);
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
