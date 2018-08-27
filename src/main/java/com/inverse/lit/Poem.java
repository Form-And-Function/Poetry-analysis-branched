package com.inverse.lit;

import java.util.ArrayList;

public class Poem {
	
	//STATIC VALUES
	public static final String[] deviceNames = {"Alliteration", "Assonance", "Consonance", "Internal Rhyme", "Repetition", "Anaphora", "Polysyndeton", "Asyndeton"};
	
	//ATRIBUTES
	private Line[] lines;
	private DeviceList deviceList;
	private int[] rhymeScheme;
	private int wordCount;
	
	//CONSTRUCTOR
	public Poem(ArrayList<String> text) {
		
		//initiate lines of poem
		lines = new Line[text.size()];
		for(int i = 0; i < lines.length; i++) {
			lines[i] = new Line(text.get(i));
		}
		
		//calculate rhyme scheme
		String[] endRhymes = new String[getLines().length];				//(stores rhyme relevant part of each line (last stressed vowel onward))
		rhymeScheme = new int[getLines().length];						//(stores numbers corresponding to each unique rhyme, in order)
		int n = 1;														//(stores number of rhymes found so far (serves as rhyme number to identify rhyming lines))
		boolean match;													//(stores whether another rhyme of the same kind has been found)
		for(int i = 0; i < getLines().length; i++) {
			if(getLines()[i].getWords().length > 0) {						//get the rhyme-relevant sound of the last word of each line (ignoring blank lines)
				endRhymes[i] = getLines()[i].getWords()[getLines()[i].getWords().length-1].getRhyme();
				if(endRhymes[i] != "") {
					match = false;
					for(int j = 0; j < i; j++) {								//check if it matches any others
						if(endRhymes[i].equals(endRhymes[j])) {
							rhymeScheme[i] = rhymeScheme[j];					//if it does, give it the same rhyme number as the one it matches
							match = true;
							j = i;
						}
					}
					if(match == false) {										//if it doesn't, give it it's own rhyme number
						rhymeScheme[i] = n;											//(and increase the rhyme number for the next one)
						n++;
					}
				}
				else rhymeScheme[i] = 0;
			}
		}
		
		deviceList = new DeviceList(8);
		
		//find and store literary devices
		deviceList.setAlliterationSound(SoundDevice.checkAlliteration(lines));
		deviceList.getAllDevices().add(deviceList.getAlliterationSound());
		
		deviceList.setAssonance(SoundDevice.checkAssonance(lines));
		deviceList.getAllDevices().add(deviceList.getAssonance());

		deviceList.setConsonance(SoundDevice.checkConsonance(lines));
		deviceList.getAllDevices().add(deviceList.getConsonance());
		
		deviceList.setInternalRhyme(SoundDevice.checkInRhyme(lines));
		deviceList.getAllDevices().add(deviceList.getInternalRhyme());
		
		deviceList.setRepetition(MultiLineDevice.checkRepetition(lines));
		deviceList.getAllDevices().add(deviceList.getRepetition());
		
		deviceList.setAnaphora(MultiLineDevice.checkAnaphora(lines));
		deviceList.getAllDevices().add(deviceList.getAnaphora());

		deviceList.setPolysyndeton(MultiLineDevice.checkPolysyndeton(lines));
		deviceList.getAllDevices().add(deviceList.getPolysyndeton());
		
		deviceList.setAsyndeton(MultiLineDevice.checkAsyndeton(lines));
		deviceList.getAllDevices().add(deviceList.getAsyndeton());
		
	}

	public Line[] getLines() {
		return lines;
	}
	
	public void setLines(Line[] lines) {
		this.lines = lines;
	}
	
	public int[] getRhymeScheme() {
		return rhymeScheme;
	}

	public void setRhymeScheme(int[] rhymeScheme) {
		this.rhymeScheme = rhymeScheme;
	}	
	
	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public DeviceList getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(DeviceList deviceList) {
		this.deviceList = deviceList;
	}
	
}
