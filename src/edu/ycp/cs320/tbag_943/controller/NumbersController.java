package edu.ycp.cs320.tbag_943.controller;

import edu.ycp.cs320.tbag_943.model.Numbers; 

public class NumbersController {
	
	private Numbers model; 
	
	public void setModel(Numbers model) {
		this.model = model; 
	}
	
	public void add() {
		Double result = (Double) (model.getFirst() + model.getSecond() + model.getThird());
		model.setResult(result);
	}
	public void multiply() {
		Double result = (Double) (model.getFirst() * model.getSecond()); 
		model.setResult(result);
	}
}
