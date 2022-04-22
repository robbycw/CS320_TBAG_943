package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//Code comes from CS320 Library Example. 
public class ReadCSV implements Closeable {
	private BufferedReader reader;
	
	public ReadCSV(String resourceName) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("edu/ycp/cs320/tbag_943/gamedb/persist/res/" + resourceName);
		
		if (in == null) {
			throw new IOException("Couldn't open " + resourceName);
		}
		this.reader = new BufferedReader(new InputStreamReader(in));
	}
	
	public List<String> next() throws IOException {
		String line = reader.readLine();
		
		if (line == null) {
			return null;
		}
		// Remove the encoded characters at the beginning of the string if present: 
		if(line.substring(0, 3).equals("﻿")) {
			line = line.substring(3); 
		}
		
		List<String> tuple = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(line, "|");
		while (tok.hasMoreTokens()) {
			tuple.add(tok.nextToken().trim());
		}
		return tuple;
	}
	
	public void close() throws IOException {
		reader.close();
	}
}
