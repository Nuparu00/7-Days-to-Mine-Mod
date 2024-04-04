package nuparu.sevendaystomine.util;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
	private List<Tree<T>> children = new ArrayList<Tree<T>>();
	private Tree<T> parent = null;
	private T data = null;

	public Tree(T data) {
		this.data = data;
	}

	public Tree(T data, Tree<T> parent) {
		this.data = data;
		this.setParent(parent);
	}

	public List<Tree<T>> getChildren() {
		return children;
	}

	public void setParent(Tree<T> parent) {
		if (!parent.children.contains(this)) {
			parent.children.add(this);
		}
		this.parent = parent;
	}

	public Tree<T> addChild(T data) {
		Tree<T> child = new Tree<T>(data);
		this.children.add(child);
		child.setParent(this);
		return child;
	}

	public void addChild(Tree<T> child) {
		child.setParent(this);
		this.children.add(child);
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isRoot() {
		return (this.getParent() == null);
	}

	public boolean isLeaf() {
		return this.children.size() == 0;
	}

	public void removeParent() {
		this.setParent(null);
	}

	public Tree<T> getTopRoot() {
		Tree<T> tree = this;
		while (!tree.isRoot()) {
			tree = tree.getParent();
		}
		return tree;
	}

	public String toString() {
		String s = "{";
		if (data != null) {
			s += data.toString();
		}
		for (int i = 0; i < children.size(); i++) {
			Tree<T> tree = children.get(i);
			s += tree.toString();
			if (i < children.size() - 1) {
				s += ",";
			}
		}
		s += "}";
		return s;
	}

	public void print(String indent, boolean last) {
		System.out.print(indent);
		if (last) {
			System.out.print("\\-");
			indent += "  ";
		} else {
			System.out.print("|-");
			indent += "| ";
		}
		System.out.println(data.toString());

		for (int i = 0; i < children.size(); i++) {
			children.get(i).print(indent, i == children.size() - 1);
		}
	}

	public Tree<T> getParent() {
		return parent;
	}
}