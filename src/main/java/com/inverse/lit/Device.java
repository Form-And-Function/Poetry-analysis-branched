package com.inverse.lit;

import java.util.ArrayList;

public abstract class Device {
	
	private String text;													//text of the specific literary device instance
	private ArrayList<int[]> indices; 										//the indices of a specific literary device within the poem
	private int intensity; 													//normalized strength literary device in poem, based on occurrence rate and other factors
	private double rawIntensity;											//non-normalized version of above
	
	public Device () {
		indices = new ArrayList<int[]>();
	}
	
	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public ArrayList<int[]> getIndices() {
		return indices;
	}

	public void setIndices(ArrayList<int[]> indices) {
		this.indices = indices;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getRawIntensity() {
		return rawIntensity;
	}

	public void setRawIntensity(double rawIntensity) {
		this.rawIntensity = rawIntensity;
	}
}
