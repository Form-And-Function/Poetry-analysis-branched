//Class for Alliteration, Assonance, Consonance, and Rhyme
//25 July 2018

package com.inverse.lit;

import java.util.ArrayList;

public class SoundDevice extends Device {
	
	//STATIC VARIABLES
	public static int sensitivity = 1;			//maximum number of lines of separations repeated sounds can have and still be considered proximate
	
	//ATRIBUTES
	private double rawIntensity;
	
	//CONSTRUCTOR
	public SoundDevice(String text){
		super();
		setText(text);
	}

	//NON-STATIC METHODS----------------------------------------------

	public double getRawIntensity() {
		return rawIntensity;
	}

	public void setRawIntensity(double rawIntensity) {
		this.rawIntensity = rawIntensity;
	}
	
	public void buildRawIntensity(Line[] lines) {
		double totalSeparation = 0;								//(stores the average separation between occurrences of the sound)		
		int startIndex = 0;
		int endIndex = 0;
		
		//find the index in the word's sound[] of the first instance of the device
		for(int i = 0; i < lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound().length; i++) {
			if(lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound()[i].equals(getText())){
				startIndex = i;
			}
		}
		
		//find the index in the word's sound[] of the last instance of the device (if repeated sound, grab last sound)
		for(int i = lines[getIndices().get(getIndices().size()-1)[0]].getWords()[getIndices().get(getIndices().size()-1)[1]].getSound().length - 1; i > -1;  i--) {
			if(lines[getIndices().get(getIndices().size()-1)[0]].getWords()[getIndices().get(getIndices().size()-1)[1]].getSound()[i].equals(getText())){
				endIndex = i;
			}
		}
		
		//count the number of sounds (weighting vowels more highly) between the first and last instance (currently inclusive of the first sound, exclusive of the last)
		if(getIndices().get(0)[0] == getIndices().get(getIndices().size()-1)[0]) {													//if the first and last instance are on the same line
			for(int c = startIndex; c < lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound().length; c++) {		//count sounds in first word from index of first instances' sound onward
				totalSeparation += soundWeight(lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound()[c]);
			}
			for(int b = getIndices().get(0)[1] + 1; b < getIndices().get(getIndices().size()-1)[1]; b++) {								//count the sounds in all words between those of the first and last index
				for(int c = 0; c < lines[getIndices().get(0)[0]].getWords()[b].getSound().length; c++) {
					totalSeparation += soundWeight(lines[getIndices().get(0)[0]].getWords()[b].getSound()[c]);
				}
			}
			for(int c = 0; c < endIndex; c++) {																							//count sounds in last word up to the last index's sound
				totalSeparation += soundWeight(lines[getIndices().get(getIndices().size()-1)[0]].getWords()[getIndices().get(getIndices().size()-1)[1]].getSound()[c]);
			}
		}
		else {																														//if the first and last instance are on different lines
			for(int c = startIndex; c < lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound().length; c++) {		//count sounds in first word from index of first instances' sound onward
				totalSeparation += soundWeight(lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound()[c]);
			}
			for(int b = getIndices().get(0)[1] + 1; b < lines[getIndices().get(0)[0]].getWords().length; b++) {							//count sounds in all words of first line following first word
				for(int c = 0; c < lines[getIndices().get(0)[0]].getWords()[b].getSound().length; c++) {
					totalSeparation += soundWeight(lines[getIndices().get(0)[0]].getWords()[b].getSound()[c]);
				}
			}
			for(int a = getIndices().get(0)[0] + 1; a < getIndices().get(getIndices().size()-1)[0]; a++) {								//go through all lines between first instance's line and last instance's line
				for(int b = 0; b < lines[a].getWords().length; b++) {
					for(int c = 0; c < lines[a].getWords()[b].getSound().length; c++) {
						totalSeparation += soundWeight(lines[a].getWords()[b].getSound()[c]);
					}
				}
			}
			for(int b = 0; b < getIndices().get(getIndices().size()-1)[1]; b++) {														//count sounds in all words of last line up to the last instance's word
				for(int c = 0; c < lines[getIndices().get(getIndices().size()-1)[0]].getWords()[b].getSound().length; c++) {
					totalSeparation += soundWeight(lines[getIndices().get(getIndices().size()-1)[0]].getWords()[b].getSound()[c]);
				}
			}
			for(int c = 0; c < endIndex; c++) {																							//count sounds in last word up to the last index's sound
				totalSeparation += soundWeight(lines[getIndices().get(getIndices().size()-1)[0]].getWords()[getIndices().get(getIndices().size()-1)[1]].getSound()[c]);
			}
		}
		//set the intensity to instance density (# of instances / total separation) times the square root of the number of instances
		rawIntensity = Math.pow(getIndices().size(), 2) / totalSeparation;
	}
	
	//STATIC METHODS---------------------------------------------------------------------------
	
	public static ArrayList<SoundDevice> checkInRhyme(Line[] lines) {
		ArrayList<SoundDevice> inRhymes = new ArrayList<SoundDevice>();
		
		return inRhymes;
	}
	
	//finds instances of alliteration in a Line[]
	public static ArrayList<Device> checkAlliteration(Line[] lines) {
		System.out.println("Alliteration"); 		//TODO: Debug
		ArrayList<Device> alliterations = new ArrayList<Device>();				//(stores instances of alliteration, henceforth "alliterations")
		String sound;																		//(stores the current sound in question)
		boolean contains;																	//(stores whether current sound is stored in alliteration array)
		for(int a = 0; a < lines.length; a++) {												//go through each line
			for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word in each line
				if(lines[a].getWords()[b].getSound().length != 0) {										//(if we have a pronunciation for the word)
					sound = lines[a].getWords()[b].getSound()[0];										//grab the 1st sound of the word
					contains = false;
					for(int c = 0; c < alliterations.size(); c++) {										//if the sound is present in the alliterations array
						if(alliterations.get(c).getText().equals(sound)) {
							int[] index = {a, b};															//...record add this index to the alliteration
							alliterations.get(c).getIndices().add(index);
							contains = true;
							c = alliterations.size();
						}
					}
					if(!contains) {																		//if the sound is not present in the alliterations array
						int[] index = {a, b};
						alliterations.add(new SoundDevice(sound));										//add it to the alliterations array
						alliterations.get(alliterations.size() - 1).getIndices().add(index);				//...with its index
					}
				}
			}
		}
		
		//remove all alliterations with only one index, and remove all indices of an alliteration separated two far from their compatriots (determined by sensitivity)
		for(int a = alliterations.size() - 1; a > -1; a--) {
			if(alliterations.get(a).getIndices().size() == 1) {
				alliterations.remove(a);
			}
			else {
				for(int b = alliterations.get(a).getIndices().size() - 2; b > 0; b--) {		//if a middle index is separated by more than sensitivity# line(s) from those on either side
					if(alliterations.get(a).getIndices().get(b)[0] - alliterations.get(a).getIndices().get(b-1)[0] > sensitivity && alliterations.get(a).getIndices().get(b+1)[0] - alliterations.get(a).getIndices().get(b)[0] > sensitivity) {
						alliterations.get(a).getIndices().remove(b);							//remove it
					}
				}
				if(alliterations.get(a).getIndices().get(1)[0] - alliterations.get(a).getIndices().get(0)[0] > sensitivity) {		//if the 1st index is separated by more than sensitivity# line(s) from the 2nd
					alliterations.get(a).getIndices().remove(0);													//remove it
				}																	//if the last index is separated by >= sensitivity# line(s) from the 2nd
				if(alliterations.get(a).getIndices().size() != 1 && alliterations.get(a).getIndices().get(alliterations.get(a).getIndices().size()-1)[0] - alliterations.get(a).getIndices().get(alliterations.get(a).getIndices().size()-2)[0] > sensitivity) {
					alliterations.get(a).getIndices().remove(alliterations.get(a).getIndices().size()-1);						//remove it
				}
				if(alliterations.get(a).getIndices().size() < 2) {										//if the alliteration has 0-1 remaining indices, remove it
					alliterations.remove(a);
				}
			}
		}
		
		double maxScore = 0;
		for(int i = 0; i < alliterations.size(); i++) {
			((SoundDevice) alliterations.get(i)).buildRawIntensity(lines);
			if(((SoundDevice) alliterations.get(i)).getRawIntensity() > maxScore) {
				maxScore = ((SoundDevice) alliterations.get(i)).getRawIntensity();
			}
		}
		for(int i = 0; i < alliterations.size(); i++) {
			alliterations.get(i).setIntensity((int)Math.round(100*((SoundDevice)(alliterations.get(i))).getRawIntensity()/maxScore));
		}
				
		return alliterations;
		
		//TODO: filter out alliteration that's actually just homophones
	}
		
	//TODO: homophones
	

	
	//finds instances of assonance in a given line[]
		public static ArrayList<Device> checkAssonance(Line[] lines) {
			System.out.println("Assonance"); 		//TODO: Debug
			ArrayList<Device> output = null;													//(stores final output: ArrayList of Devices)
			ArrayList<String> vowelSounds = new ArrayList<String>();							//(stores vowel sounds found in Line[])
			ArrayList<ArrayList<int[]>> indices = new ArrayList<ArrayList<int[]>>();			//(stores indices of vowel sounds found)
			
			//record each vowel sound in the line[] and their respective indices
			for(int a = 0; a < lines.length; a++) {												//go through each line
				for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word of each line
					for(int c = 0; c < lines[a].getWords()[b].getVowels().length; c++) {				//go through each vowel sound of each word of each line
						int[] index = {a, b, c};															//(stores the index of the sound in question)
						if(!vowelSounds.contains(lines[a].getWords()[b].getVowels()[c])) {					//If it's not yet in the list of vowel sounds...
							vowelSounds.add(lines[a].getWords()[b].getVowels()[c]);									//...add it to the list and record its index
							indices.add(new ArrayList<int[]>());
							indices.get(indices.size()-1).add(0, index);
						}
						else {																				//If it is already in the list...
							indices.get(vowelSounds.indexOf(lines[a].getWords()[b].getVowels()[c])).add(index);		//...record another index for the vowel sound
						}
					}
				}
			}
			
			//remove all vowel sounds with only one index and all indices that are too far apart
			for(int a = vowelSounds.size() - 1; a > -1; a--) {		//go through each recorded vowel sound
				if(indices.get(a).size() == 1) {							//remove all vowel sounds with only one occurance
					vowelSounds.remove(a);
					indices.remove(a);
				}
				else {														//remove all repeated vowel sounds separated from their counterparts by at least one line
					for(int b = indices.get(a).size() - 2; b > 0; b--) {						//if a middle index is separated by more than sensitivity# line(s) from those on either side
						if(indices.get(a).get(b)[0] - indices.get(a).get(b-1)[0] > sensitivity && indices.get(a).get(b+1)[0] - indices.get(a).get(b)[0] > sensitivity) {
							indices.get(a).remove(b);												//remove it
						}
					}
					if(indices.get(a).get(1)[0] - indices.get(a).get(0)[0] > sensitivity) {		//if the 1st index is separated by more than sensitivity# line(s) from the 2nd
						indices.get(a).remove(0);													//remove it
					}																	//if the last index is separated by >= sensitivity# line(s) from the 2nd, remove it
					if(indices.get(a).size() != 1 && indices.get(a).get(indices.get(a).size()-1)[0] - indices.get(a).get(indices.get(a).size()-2)[0] > sensitivity) {
						indices.get(a).remove(indices.get(a).size()-1);						//remove it
					}
					if(indices.get(a).size() < 2) {										//if the vowel has 0-1 remaining indices, remove it
						vowelSounds.remove(a);
						indices.remove(a);
					}
				}
			}
			
			//create assonance SoundDevices from the remaining vowel sounds
			output = new ArrayList<Device>(vowelSounds.size());
			double maxScore = 0;
			for(int i = 0; i < vowelSounds.size(); i++) {
				output.add(new SoundDevice(vowelSounds.get(i)));
				output.get(i).setIndices(indices.get(i));
				((SoundDevice) output.get(i)).buildRawIntensity(lines);
				if(((SoundDevice)output.get(i)).getRawIntensity() > maxScore) {
					maxScore = ((SoundDevice)output.get(i)).getRawIntensity();
				}
			}
			for(int i = 0; i < output.size(); i++) {
				output.get(i).setIntensity((int)Math.round(100*((SoundDevice)output.get(i)).getRawIntensity()/maxScore));
			}
			
			return output;
		}
	
	//finds instances of consonance in a given line[]
	public static ArrayList<Device> checkConsonance(Line[] lines) {
		System.out.println("Consonance"); 		//TODO: Debug
		ArrayList<Device> instances = new ArrayList<Device>();								//(stores instances of consonance found in the Line[])
		ArrayList<String> consonantSounds = new ArrayList<String>();						//(stores consonant sounds found in Line[])
		ArrayList<ArrayList<int[]>> indices = new ArrayList<ArrayList<int[]>>();			//(stores indices of consonant sounds found)
		String sound;																		//(stores the current sound being worked with)
		boolean contains;																	//(stores whether instances contains the current sound)
		
		//record each consonant sound in the line[] and their respective indices
		for(int a = 0; a < lines.length; a++) {												//go through each line
			for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word of each line
				for(int c = 0; c < lines[a].getWords()[b].getConsonants().length; c++) {			//go through each consonant sound of each word of each line
					int[] index = {a, b, c};															//(stores the index of the sound in question)
					sound = lines[a].getWords()[b].getConsonants()[c];
					contains = false;
					for(int d = 0; d < instances.size(); d++) {										//if the sound is already contained in instances
						if(instances.get(d).getText().equals(sound)) {									//add the index of this sound to that instance
							instances.get(d).getIndices().add(index);
							contains = true;
							d = instances.size();
						}
					}
					if(!contains) {																	//otherwise, add a new instance and record its index
						instances.add(new SoundDevice(sound));
						instances.get(instances.size()-1).getIndices().add(index);
					}
				}
			}
		}
		
		//remove all consonant sounds with only one index and all indices that are too far apart
		for(int a = consonantSounds.size() - 1; a > -1; a--) {		//go through each recorded consonant sound
			if(indices.get(a).size() == 1) {							//remove all consonant sounds with only one occurance
				consonantSounds.remove(a);
				indices.remove(a);
			}
			else {														//remove all repeated consonant sounds separated from their counterparts by at least one line
				for(int b = indices.get(a).size() - 2; b > 0; b--) {						//if a middle index is separated by more than sensitivity# line(s) from those on either side
					if(indices.get(a).get(b)[0] - indices.get(a).get(b-1)[0] > sensitivity && indices.get(a).get(b+1)[0] - indices.get(a).get(b)[0] > sensitivity) {
						indices.get(a).remove(b);												//remove it
					}
				}
				if(indices.get(a).get(1)[0] - indices.get(a).get(0)[0] > sensitivity) {		//if the 1st index is separated by more than sensitivity# line(s) from the 2nd, remove it
					indices.get(a).remove(0);													//remove it
				}																	//if the last index is separated by >= sensitivity# line(s) from the 2nd, remove it
				if(indices.get(a).size() != 1 && indices.get(a).get(indices.get(a).size()-1)[0] - indices.get(a).get(indices.get(a).size()-2)[0] > sensitivity) {
					indices.get(a).remove(indices.get(a).size()-1);						//remove it
				}
				if(indices.get(a).size() < 2) {										//if the vowel has 0-1 remaining indices, remove it
					consonantSounds.remove(a);
					indices.remove(a);
				}
			}
		}
		
		//remove instances that are composed entirely of homophones from the instances list
		instances = removeHomophones(instances, lines);
		
		//score the intensity of each instance, normalized from 0-100 (where most intense device has score 100)
		double maxScore = 0;
		for(int i = 0; i < instances.size(); i++) {									//get the raw score of each instance and record the highest raw score
			((SoundDevice)instances.get(i)).buildRawIntensity(lines);
			if(((SoundDevice)instances.get(i)).getRawIntensity() > maxScore) {
				maxScore = ((SoundDevice)instances.get(i)).getRawIntensity();
			}
		}
		for(int i = 0; i < instances.size(); i++) {									//express each intensity score as a rounded percent of the maximum raw score
			instances.get(i).setIntensity((int)Math.round(100*((SoundDevice)instances.get(i)).getRawIntensity()/maxScore));
		}
		return instances;
	}
	
	private static int soundWeight(String sound) {
		int out = 1;
		if(sound.charAt(0) == 'A' || sound.charAt(0) == 'E' || sound.charAt(0) == 'I' || sound.charAt(0) == 'O' || sound.charAt(0) == 'U') {
			out = 5;
		}
		return out;
	}
	
	private static ArrayList<Device> removeHomophones(ArrayList<Device> instances, Line[] lines){
		boolean allHomophones;
		for(int a = instances.size()-1; a > -1; a--) {																						//go through each instance of the SoundDevice
			allHomophones = true;
			String word = lines[instances.get(a).getIndices().get(0)[0]].getWords()[instances.get(a).getIndices().get(0)[1]].getText();			//get the text of the word associated with the first index of the instance
			for(int b = 1; b < instances.get(a).getIndices().size(); b++) {																		//go through each index of the instance 
				if(lines[instances.get(a).getIndices().get(b)[0]].getWords()[instances.get(a).getIndices().get(b)[1]].getText() != word) {			//if any of them are associated with a different word than the first one
					allHomophones = false;																												//the instance of the sound device is not merely a case of homophones (not all homophones)
				}
			}
			if(allHomophones) {																													//if the instance is made up of only homophones
				instances.remove(a);																												//remove it
			}
		}
		return instances;
	}
	
}
