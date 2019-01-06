package com.inverse.lit;

import java.util.ArrayList;

public class Poem {
	
	//STATIC VALUES
	public static final String[] deviceNames = {"Alliteration", "Assonance", "Consonance", "Internal Rhyme", "Repetition", "Anaphora", "Polysyndeton", "Asyndeton", "Simile"};
	
	//ATTRIBUTES
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
            System.out.println(i);
		}

		//calculate rhyme scheme
		String[] endRhymes = new String[getLines().length];				//(stores rhyme relevant part of each line (last stressed vowel onward))
		rhymeScheme = new int[getLines().length];						//(stores numbers corresponding to each unique rhyme, in order)
		int n = 1;														//(stores number of rhymes found so far (serves as rhyme number to identify rhyming lines))
		boolean match;													//(stores whether another rhyme of the same kind has been found)

		for(int i = 0; i < getLines().length; i++) {


			if(getLines()[i].getWords().length > 0) {						//get the rhyme-relevant sound of the last word of each line (ignoring blank lines)
                var words = getLines()[i].getWords();
                System.out.println("reacheddddd "+i);
				endRhymes[i] = words[words.length-1].getRhyme();
                System.out.println("reached "+i);
				if(endRhymes[i] != "") {
                    System.out.println("reached 2 "+i);
					match = false;
					for(int j = 0; j < i; j++) {								//check if it matches any others
                        System.out.println("reached "+j);
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
		System.out.println("reached 00");
		deviceList = new DeviceList(9);
        System.out.println("reached 10");
		//find and store literary devices
		deviceList.setAlliterationSound(SoundDevice.checkAlliteration(lines));
        System.out.println("reached 20");
		deviceList.getAllDevices().add(deviceList.getAlliterationSound());
        System.out.println("reached 30");
		deviceList.setAssonance(SoundDevice.checkAssonance(lines));
		deviceList.getAllDevices().add(deviceList.getAssonance());
        System.out.println("reached 40");
		deviceList.setConsonance(SoundDevice.checkConsonance(lines));
		deviceList.getAllDevices().add(deviceList.getConsonance());
        System.out.println("reached 50");
		deviceList.setInternalRhyme(SoundDevice.checkInRhyme(lines));
		deviceList.getAllDevices().add(deviceList.getInternalRhyme());
        System.out.println("reached 60");
		deviceList.setRepetition(MultiLineDevice.checkRepetition(lines));
		deviceList.getAllDevices().add(deviceList.getRepetition());
        System.out.println("reached 70");
		deviceList.setAnaphora(MultiLineDevice.checkAnaphora(lines));
		deviceList.getAllDevices().add(deviceList.getAnaphora());
        System.out.println("reached 80");
		deviceList.setPolysyndeton(MultiLineDevice.checkPolysyndeton(lines));
		deviceList.getAllDevices().add(deviceList.getPolysyndeton());
        System.out.println("reached 90");
		deviceList.setAsyndeton(MultiLineDevice.checkAsyndeton(lines));
		deviceList.getAllDevices().add(deviceList.getAsyndeton());
        deviceList.setSimile(MultiLineDevice.checkSimile(lines));
        deviceList.getAllDevices().add(deviceList.getSimile());

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
