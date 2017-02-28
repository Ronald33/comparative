package clustering.birch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import clustering.point.Point;

public class Leaf extends Node implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<CF> entries;
	private Leaf next; 
	private Leaf prev;
	
	private static Integer id = -1;
	private static HashMap<Integer, Integer> ids = new HashMap<>();
	
	public Leaf() { super(); this.entries = new ArrayList<>(); }
	
	private void addEntry(CF cf)
	{
		this.entries.add(cf);
		this.total.add(cf);
	}
	public void addPoint(Point point) throws Exception
	{
		CF cf = new CF(point);
		this.addEntry(cf);
		
		id++;
		ids.put(cf.hashCode(), id);
//		point.setCluster(id);
	}
	public void updateParents(Point point) throws Exception
	{
		CF cf = new CF(point);
		this.updateParents(cf);
	}
	private void updateParents(CF cf)
	{
		NonLeaf parent = this.getParent();
		while(parent != null) { parent.getTotal().add(cf); parent = parent.getParent(); }
	}
	private void removeEntryByIndex(int index) { this.getTotal().remove(this.getEntryByIndex(index)); this.entries.remove(index); }
	
	@Override
	public void insertIntoClosest(Point point) throws Exception
	{
		CF cf = new CF(point);
		int closest_index = Helper.getIndexClosest(this, cf);
		CF closest_entry = this.getEntryByIndex(closest_index);
		double distance = closest_entry.euclideanDistance(cf);
		
//		/* A punto */
//		CF clone = closest_entry.clone();
//		clone.add(cf);
//		distance = clone.diameter();
//		/* Fin de a punto */
		if(distance <= CFTree.T) // Lo absorbe 
		{
			closest_entry.add(cf);
//			point.setCluster(ids.get(closest_entry.hashCode()));
			this.getTotal().add(cf);
			this.updateParents(point);
		}
		else
		{
			this.addPoint(point);
			this.updateParents(point);
			if(this.verifyAndSplit()) { if(this.getParent() != CFTree.stop_node) {System.err.println("Llego hasta arriba");} CFTree.refine(); }
		}
	}
	@Override
	public CF getEntryByIndex(int index) { return this.entries.get(index); }
	@Override
	public int getSize() { return this.entries.size(); }
	@Override
	public void transfer(Node node, int index)
	{
		Leaf leaf = (Leaf) node;
		CF entry = this.getEntryByIndex(index);
		leaf.addEntry(entry);
		this.removeEntryByIndex(index);
	}
	private void addEntriesFromLeaf(Leaf leaf)
	{
		this.getEntries().addAll(leaf.getEntries());
		this.getTotal().add(leaf.getTotal());
		this.getParent().getTotal().add(leaf.getTotal());
	}
	@Override
	public void transferFromBrother(int brother_index)
	{
		NonLeaf parent = this.getParent();
		Leaf leaf = (Leaf) parent.getChildByIndex(brother_index);
		this.addEntriesFromLeaf(leaf);
		
		leaf.prev.setNext(leaf.next);
		if(leaf.getNext() != null) { leaf.getNext().setPrev(leaf.getPrev()); }
		
		parent.removeChildByIndex(brother_index);
		if(this.verifyAndSplit()) { if(this.getParent() != CFTree.stop_node) {System.err.println("Llego hasta arriba");} CFTree.refine(); }
	}
	@Override
	public boolean isFull()
	{
		if(this.getSize() >= CFTree.L + 1) { return true; }
		else { return false; }
	}
	@Override
	public boolean verifyAndSplit()
	{
		if(this.isFull())
		{
			Leaf new_leaf = new Leaf();
			this.FillNewNode(new_leaf);
			new_leaf.setNext(this.next);
			new_leaf.setPrev(this);
			if(this.next != null) { this.next.setPrev(new_leaf); }
			this.setNext(new_leaf);
//			CFTree.refine();
			return true;
		}
		else { return false; }
	}
	
	/* GyS */
	public Leaf getNext() { return next; }
	public void setNext(Leaf next) { this.next = next; }
	public Leaf getPrev() { return prev; }
	public void setPrev(Leaf prev) { this.prev = prev; }
	public ArrayList<CF> getEntries() { return entries; }
	public void setEntries(ArrayList<CF> entries) { this.entries = entries; }
	/* GyS */
	
	@Override
	public String toString() { return this.entries.toString(); }
}