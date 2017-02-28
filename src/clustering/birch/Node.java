package clustering.birch;

import java.io.Serializable;
import java.util.ArrayList;

import clustering.point.Point;

public abstract class Node implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected CF total;
	protected NonLeaf parent;
	
	public Node() { this.total = new CF(); }
	
	public void FillNewNode(Node new_node) /* Split */
	{
		int farthest[] = Helper.getFarthest(this);
		int index_1 = farthest[0], index_2 = farthest[1]; 
		CF cf1 = this.getEntryByIndex(index_1), cf2 = this.getEntryByIndex(index_2);
		this.transfer(new_node, index_2);
		int size = this.getSize();
		for(int i = 0; i < size; i++)
		{
			if(i != index_1)
			{
				CF cfi = this.getEntryByIndex(i);
				if(cfi.distance(cf2) < cfi.distance(cf1)) { this.transfer(new_node, i); i--; size--; }
			}
		}
		
		NonLeaf parent = (NonLeaf) this.getParent();
		parent.addChild(new_node);
		
		if(!parent.verifyAndSplit())
		{
			CFTree.stop_node = parent;
			CFTree.stop_index_1 = parent.getIndex(this);
			CFTree.stop_index_2 = parent.getSize() - 1;
		}
	}
	
	public abstract void insertIntoClosest(Point point) throws Exception;
	public abstract CF getEntryByIndex(int index);
	public abstract int getSize();
	public abstract void transfer(Node node, int index);
	public abstract void transferFromBrother(int brother_index);
	public abstract boolean isFull();
	public abstract boolean verifyAndSplit();

	/* GyS */
	public CF getTotal() { return total; }
	public void setTotal(CF total) { this.total = total; }
	public NonLeaf getParent() { return parent; }
	public void setParent(NonLeaf parent) { this.parent = parent; }
	/* GyS */

	@Override
	public String toString()
	{
		ArrayList<CF> my_entries = new ArrayList<>();
		for(int i = 0; i < this.getSize(); i++)
		{
			my_entries.add(this.getEntryByIndex(i));
		}
		return my_entries.toString();
	}
}