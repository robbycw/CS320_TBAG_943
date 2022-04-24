package edu.ycp.cs320.tbag_943.classes;

import java.util.ArrayList;

public class Speech {

	private ArrayList<String> speechOptions;
	private ArrayList<String> speechResponses; 
	private int id; 
	private String intimidateOption;
	private String intimidateResponse;
	private String intimidateResponseFail;
	private String persuadeOption;
	private String persuadeResponse;
	private String persuadeResponseFail;
	
	public Speech() {
		this.speechOptions = new ArrayList<String>();
		this.speechResponses = new ArrayList<String>();
	}
	
	public Speech(ArrayList<String> speechOptionsIn, ArrayList<String> speechResponsesIn) {
		this.speechOptions = speechOptionsIn;
		this.speechResponses = speechResponsesIn;
	}
	
	
	public void addToSpeech(String add) 
	{
		speechOptions.add(add);
	}
	
	public void addArrayListSpeech(ArrayList<String> add) 
	{
		speechOptions.addAll(add);
	}
	
	public void addToResponse(String add) 
	{
		speechResponses.add(add);
	}
	
	public void addArrayListResponse(ArrayList<String> add) 
	{
		speechResponses.addAll(add);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSpeech(int get) 
	{
		return speechOptions.get(get);
	}
	
	public ArrayList<String> getSpeeches() 
	{
		return speechOptions;
	}
	
	public String getResponse(int get) 
	{
		return speechResponses.get(get);
	}
	
	public ArrayList<String> getResponses() 
	{
		return speechResponses;
	}
	
	public void setIntimOp(String promptIn)
	{
		intimidateOption = promptIn;
	}
	
	public String getIntimOp()
	{
		return intimidateOption;
	}
	
	public void setPersOp(String promptIn)
	{
		persuadeOption = promptIn;
	}
	
	public String getPersOp()
	{
		return persuadeOption;
	}
	
	public void setIntimRes(String promptIn)
	{
		intimidateResponse = promptIn;
	}
	
	public String getIntimRes()
	{
		return intimidateResponse;
	}
	
	public void setPersRes(String promptIn)
	{
		persuadeResponse = promptIn;
	}
	
	public String getPersRes()
	{
		return persuadeResponse;
	}
	
	public void setIntimResFail(String promptIn)
	{
		intimidateResponseFail = promptIn;
	}
	
	public String getIntimResFail()
	{
		return intimidateResponseFail;
	}
	
	public void setPersResFail(String promptIn)
	{
		persuadeResponseFail = promptIn;
	}
	
	public String getPersResFail()
	{
		return persuadeResponseFail;
	}

	public ArrayList<String> getSpeechOptions() {
		return speechOptions;
	}

	public ArrayList<String> getSpeechResponses() {
		return speechResponses;
	}

	public void setSpeechOptions(ArrayList<String> speechOptions) {
		this.speechOptions = speechOptions;
	}

	public void setSpeechResponses(ArrayList<String> speechResponses) {
		this.speechResponses = speechResponses;
	}
	
	
	
}
