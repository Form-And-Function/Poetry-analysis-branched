package com.poetryanalyzer.lit;

import java.util.ArrayList;

public abstract class Device {
	private String text;																	//text of the specific literary device instance
	private ArrayList<Double> indices = new ArrayList<Double>(); 							//ArraylLst of doubles, serves different purposes for anaphora and polysyndeton
	private int intensity; 																	//strength literary device in poem, based on occurrence rate and other factors
	
	public Device () {
		
	}
	
	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public ArrayList<Double> getIndices() {
		return indices;
	}

	
	
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
