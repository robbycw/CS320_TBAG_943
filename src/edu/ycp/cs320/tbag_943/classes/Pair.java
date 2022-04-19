package edu.ycp.cs320.tbag_943.classes;

// Code is from CS320
public class Pair<LeftType, RightType> {
	private LeftType left;
	private RightType right;
	
	public Pair(LeftType left, RightType right) {
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(LeftType left) {
		this.left = left;
	}
	
	public LeftType getLeft() {
		return left;
	}
	
	public void setRight(RightType right) {
		this.right = right;
	}
	
	public RightType getRight() {
		return right;
	}
}
