package clustering.birch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import clustering.Clustering;
import clustering.point.Point;

public class CFTree extends Clustering implements Serializable
{
	private static String id;
	private static final long serialVersionUID = 1L;
	
	public static int B = Config.B;
	public static int L = Config.L;
	public static double T = Config.T;
	
	private NonLeaf root;
	private StringBuilder tree;
	private Leaf firstLeaf;
//	private static String file = "cftree"; 
	
	/* Usado para el refinamiento */
	public static NonLeaf stop_node;
	public static int stop_index_1;
	public static int stop_index_2;
	/* Fin de Usado para el refinamiento */
	
	private CFTree()
	{
		super();
		this.tree = new StringBuilder();
	}
	public static CFTree getInstace(String id) throws ClassNotFoundException, IOException
	{
		String path = id + "_" + serialVersionUID;
		File f = new File(path);
		CFTree cft;
		if(f.exists() && !f.isDirectory())
		{
			System.out.println("Si existe");
			CFTree.id = id;
			cft = (CFTree) loadFromFile(path);
			cft.getTree().setLength(0);
		}
		else
		{
			CFTree.id = id;
			cft = new CFTree();
		}
		return cft;
	}
	
	public void insertPoint(Point point) throws Exception
	{
		if(this.getRoot() == null) { this.addFirstLeaf(point); }
		else
		{
			this.getRoot().insertIntoClosest(point);
			if(this.getRoot().getParent() != null) { this.setRoot(this.getRoot().getParent()); }
		}
	}
	private void addFirstLeaf(Point point) throws Exception
	{
		Leaf leaf = new Leaf();
		this.setRoot(new NonLeaf());
		this.getRoot().addChild(leaf);
		leaf.addPoint(point);
		leaf.updateParents(point);
		this.setFirstLeaf(leaf);
	}
	public static void refine()
	{
		int closest[] = Helper.getClosest(CFTree.stop_node);
		int closest_1 = closest[0];
		int closest_2 = closest[1];
		if(closest_1 != CFTree.stop_index_1 || closest_2 != CFTree.stop_index_2)
		{
			Node node1 = CFTree.stop_node.getChildByIndex(closest_1);
			node1.transferFromBrother(closest_2);
		}
	}
	private void writeTree() throws Exception
	{
		String name = "birch_tree";
		this.generateTree();
		helper.Helper.writeFile(name, true, this.getTree().toString());
		
	}
	private void printVG(Node node)
	{
		if(node != null)
		{
			this.getTree().append(node.hashCode() + "[label=\"" + node + "\"];");
			this.getTree().append("\n");
			if(node.getClass().equals(NonLeaf.class))
			{
				NonLeaf non_leaf = (NonLeaf) node;
				int size = non_leaf.getSize();
				for(int i = 0; i < size; i++)
				{
					Node child = non_leaf.getChildByIndex(i); 
					printVG(child);
					this.getTree().append(node.hashCode() + " -> " + child.hashCode());
					this.getTree().append("\n");
				}
			}
		}
	}
	private void generateTree()
	{
		if(this.root != null)
		{
			this.getTree().append("digraph g {");
			this.getTree().append("\n");
			this.getTree().append("node [shape=record];");
			this.getTree().append("\n");
			Node node = this.root;
			this.printVG(node);
			this.getTree().append("}");
		}
	}
	
	@Override
	public void execute(ArrayList<Point> points) throws Exception
	{
		this.start();
		this.points = points;
		int size = points.size();
		for(int i = 0; i < size; i++) { this.insertPoint(points.get(i)); }
		this.end();
	}
	
	/* GyS */
	public NonLeaf getRoot() { return root; }
	public void setRoot(NonLeaf root) { this.root = root; }
	public StringBuilder getTree() { return this.tree; }
	public Leaf getFirstLeaf() { return firstLeaf; }
	public void setFirstLeaf(Leaf firstLeaf) { this.firstLeaf = firstLeaf; }
//	public void setSb(StringBuilder sb) { this.getTree() = sb; }
	/* GyS */
	
	public void saveToFile() throws IOException
	{
		FileOutputStream fileOut = new FileOutputStream(id + "_" + serialVersionUID);
		ObjectOutputStream oos = new ObjectOutputStream(fileOut);
		oos.writeObject(this);
		oos.close();
	    fileOut.close();
	}
	
	public static CFTree loadFromFile(String path) throws IOException, ClassNotFoundException
	{
		FileInputStream fileIn = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fileIn);
		CFTree cft = (CFTree) ois.readObject();
		ois.close();
		fileIn.close();
		return cft;
	}
	
	public ArrayList<Point> getCentroids()
	{
		ArrayList<Point> points = new ArrayList<>();
		Leaf current = this.firstLeaf;
		while(current != null)
		{
			int size = current.getSize();
			for(int i = 0; i < size; i++) { points.add(new Point(current.getEntryByIndex(i).getCentroid())); }
			current = current.getNext();
		}
		return points;
	}

	@Override
	public void saveOutput() throws Exception
	{
		this.generateReport();
		this.writeTree();
		clustering.Helper.pointsToFile(this.getPoints(), this.getCentroids(), "birch_points");
	}

	@Override
	public void generateReport() throws Exception
	{
		this.setStatistics();
		this.report.append("Configuration\n");
		this.report.append("Dimention: " + Config.dimention + "\n");
		this.report.append("L: " + L + "\n");
		this.report.append("B: " + B + "\n");
		this.report.append("T: " + T + "\n");
		helper.Helper.writeFile("birch_report", true, this.report.toString());
	}
}
