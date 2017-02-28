package clustering.birch.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import clustering.birch.CFTree;
import clustering.birch.Config;
import clustering.birch.Leaf;
import clustering.birch.NonLeaf;
import clustering.point.Point;

public class LeafTest
{
	@Test
	public void addPoint() throws Exception
	{
		Config.dimention = 2;
		Leaf leaf = new Leaf();
		leaf.addPoint(new Point(1, 2));
		leaf.addPoint(new Point(3, 4));
		
		Assert.assertEquals(leaf.getTotal().getN(), 2);
		Assert.assertTrue(Arrays.equals(leaf.getTotal().getLS(), new double[]{4, 6}));
		Assert.assertTrue(Arrays.equals(leaf.getTotal().getSS(), new double[]{10, 20}));
	}
	
	@Test
	public void absorb() throws Exception
	{
		Config.dimention = 2;
		Config.T = 100;
		CFTree cft = CFTree.getInstace("test_absorb");;
		cft.insertPoint(new Point(1, 2));
		cft.insertPoint(new Point(3, 4));
		
		Assert.assertEquals(cft.getRoot().getChildByIndex(0).getEntryByIndex(0).getN(), 2);
		Assert.assertTrue(Arrays.equals(cft.getRoot().getChildByIndex(0).getEntryByIndex(0).getLS(), new double[]{4, 6}));
		Assert.assertTrue(Arrays.equals(cft.getRoot().getChildByIndex(0).getEntryByIndex(0).getSS(), new double[]{10, 20}));
	}
	
	@Test
	public void nonAbsorb() throws Exception
	{
		Config.dimention = 2;
		Config.T = 0;
		CFTree cft = CFTree.getInstace("test_na");;
		cft.insertPoint(new Point(1, 2));
		cft.insertPoint(new Point(3, 4));
		
		Assert.assertEquals(cft.getRoot().getChildByIndex(0).getEntryByIndex(0).getN(), 1);
		Assert.assertTrue(Arrays.equals(cft.getRoot().getChildByIndex(0).getEntryByIndex(1).getLS(), new double[]{3, 4}));
		Assert.assertTrue(Arrays.equals(cft.getRoot().getChildByIndex(0).getEntryByIndex(1).getSS(), new double[]{9, 16}));
	}
	@Test
	public void transfer() throws Exception
	{
		Config.dimention = 2;
		Leaf leaf = new Leaf();
		leaf.addPoint(new Point(1, 2));
		leaf.addPoint(new Point(3, 4));
		Leaf otherLeaf = new Leaf();
		leaf.transfer(otherLeaf, 0);
		leaf.transfer(otherLeaf, 0);
		
		Assert.assertEquals(leaf.getEntries().size(), 0);
		Assert.assertEquals(leaf.getTotal().getN(), 0);
		Assert.assertTrue(Arrays.equals(leaf.getTotal().getLS(), new double[]{0, 0}));
		Assert.assertTrue(Arrays.equals(leaf.getTotal().getSS(), new double[]{0, 0}));

		Assert.assertEquals(otherLeaf.getEntries().size(), 2);
		Assert.assertEquals(otherLeaf.getTotal().getN(), 2);
		Assert.assertTrue(Arrays.equals(otherLeaf.getTotal().getLS(), new double[]{4, 6}));
		Assert.assertTrue(Arrays.equals(otherLeaf.getTotal().getSS(), new double[]{10, 20}));
	}
	
	@Test
	public void transferFromBrother() throws Exception
	{
		try
		{
			Config.dimention = 2;
			Config.T = 0;
			Leaf leaf1 = new Leaf();
			leaf1.addPoint(new Point(1, 2));
			Leaf leaf2 = new Leaf();
			leaf2.addPoint(new Point(3, 4));
			NonLeaf nonLeaf = new NonLeaf();
			nonLeaf.addChildAndAddTotal(leaf1);
			nonLeaf.addChildAndAddTotal(leaf2);
			
			Assert.assertEquals(nonLeaf.getChildren().size(), 2);
			Assert.assertEquals(nonLeaf.getChildren().get(0), leaf1);
			Assert.assertEquals(nonLeaf.getChildren().get(1), leaf2);
			
			leaf1.transferFromBrother(1);
			
			Assert.assertEquals(leaf1.getEntries().size(), 2);
			Assert.assertEquals(leaf1.getTotal().getN(), 2);
			Assert.assertTrue(Arrays.equals(leaf1.getTotal().getLS(), new double[]{4, 6}));
			Assert.assertTrue(Arrays.equals(leaf1.getTotal().getSS(), new double[]{10, 20}));
			
			Assert.assertEquals(nonLeaf.getChildren().size(), 1);
			Assert.assertEquals(nonLeaf.getChildren().get(0), leaf1);
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}