package clustering.birch;

import java.io.Serializable;
import java.util.ArrayList;

import clustering.point.Point;

public class NonLeaf extends Node implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<Node> children;
	
	public NonLeaf() { super(); this.children = new ArrayList<Node>(); }
	
	public void addChild(Node child)
	{
		this.getChildren().add(child);
		child.setParent(this);
	}
	public void addChildAndAddTotal(Node child)
	{
		this.addChild(child);
		this.getTotal().add(child.getTotal());
	}
	public void addParent()
	{
		NonLeaf new_non_leaf = new NonLeaf();
		new_non_leaf.addChild(this);
		new_non_leaf.total = this.total.clone();
	}
	public Node getChildByIndex(int index) { return this.children.get(index); }
	public void removeChildByIndex(int index) { this.getTotal().remove(this.getEntryByIndex(index)); this.getChildren().remove(index); }
	public int getIndex(Node node)
	{
		int size = this.children.size();
		for(int i = 0; i < size; i++) { if(this.children.get(i) == node) { return i; } }
		return -1;
	}
	
	@Override
	public void insertIntoClosest(Point point) throws Exception
	{
		CF cf = new CF(point);
		int closest_index = Helper.getIndexClosest(this, cf);
		this.getChildren().get(closest_index).insertIntoClosest(point);
	}
	@Override
	public CF getEntryByIndex(int index) { return this.children.get(index).getTotal(); }
	@Override
	public int getSize() { return this.children.size(); }
	@Override
	public void transfer(Node node, int index)
	{
		NonLeaf non_leaf = (NonLeaf) node;
		Node child = this.getChildByIndex(index);
		non_leaf.addChildAndAddTotal(child);
		this.removeChildByIndex(index);
	}
	private void addChildrenFromNonLeaf(NonLeaf nonLeaf)
	{
		this.getChildren().addAll(nonLeaf.getChildren());
		this.getTotal().add(nonLeaf.getTotal());
		this.getParent().getTotal().add(nonLeaf.getTotal());
	}
	@Override
	public void transferFromBrother(int brother_index)
	{
		NonLeaf parent = this.getParent();
		NonLeaf non_leaf = (NonLeaf) parent.getChildByIndex(brother_index);
		this.addChildrenFromNonLeaf(non_leaf);
		parent.removeChildByIndex(brother_index);
		if(this.verifyAndSplit()) { if(this.getParent() != CFTree.stop_node) {System.err.println("Llego hasta arriba");}  CFTree.refine(); }
	}
	@Override
	public boolean isFull()
	{
		if(this.getSize() >= CFTree.B + 1) { return true; }
		else { return false; }
	}
	@Override
	public boolean verifyAndSplit()
	{
		if(this.isFull())
		{
			if(this.getParent() == null) { this.addParent(); }
			NonLeaf new_non_leaf = new NonLeaf();
			this.FillNewNode(new_non_leaf);
//			CFTree.refine(); // Agregado al final
			return true;
		}
		else { return false; }
	}
	
	/* GyS */
	public ArrayList<Node> getChildren() { return children; }
	public void setChildren(ArrayList<Node> children) { this.children = children; }
	/* GyS */
}