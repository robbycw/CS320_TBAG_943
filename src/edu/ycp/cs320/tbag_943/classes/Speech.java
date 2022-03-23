package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;

public class Speech {

	private ArrayList<String> speechOptions; 
	
	public Speech(String speech0) {
		this.speechOptions = new ArrayList<String>();
		addToSpeech(speech0);
	}
	
	public Speech(String speech0,String speech1) {
		this.speechOptions = new ArrayList<String>();
		addToSpeech(speech0);
		addToSpeech(speech1);
	}
	
	public Speech(String speech0,String speech1,String speech2) {
		this.speechOptions = new ArrayList<String>();
		addToSpeech(speech0);
		addToSpeech(speech1);
		addToSpeech(speech2);
	}
	
	public Speech(String speech0,String speech1,String speech2,String speech3) {
		this.speechOptions = new ArrayList<String>();
		addToSpeech(speech0);
		addToSpeech(speech1);
		addToSpeech(speech2);
		addToSpeech(speech3);
	}
	
	public Speech(String speech0,String speech1,String speech2,String speech3,String speech4) {
		this.speechOptions = new ArrayList<String>();
		addToSpeech(speech0);
		addToSpeech(speech1);
		addToSpeech(speech2);
		addToSpeech(speech3);
		addToSpeech(speech4);
	}
	
	public void addToSpeech(String add) {
		speechOptions.add(add);
	}
	
	public void addArrayListSpeech(ArrayList<String> add) {
		speechOptions.addAll(add);
	}
	
	public String getSpeech(int get) {
		
		return speechOptions.get(get);
		
	}
	
}
