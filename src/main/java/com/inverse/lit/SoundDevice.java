//Class for Alliteration, Assonance, Consonance, and Rhyme
//25 July 2018

package com.inverse.lit;

import java.util.ArrayList;

public class SoundDevice extends Device {
	
	//STATIC VARIABLES
	public static int sensitivity = 1;			//maximum number of lines of separations repeated sounds can have and still be considered proximate
		
	//CONSTRUCTOR
	public SoundDevice(String text){
		super();
		setText(text);
	}

	//NON-STATIC METHODS----------------------------------------------

	public void buildScore(Line[] lines) {
		int intensity = 0;										//(stores the intensity score of the SoundDevice)
		int[] separation = new int[getIndices().size()];		//(stores the separation between each occurrence of the sound and the previous one)
		double totalSeparation = 0;								//(stores the average separation between occurrences of the sound)
		int n = 0;												//(stores which index (#1, #2, #3) we are working with (the nth index)
		
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
			for(int b = getIndices().get(0)[1] + 1; b < getIndices().get(getIndices().size()-1)[1]; b++) {
				for(int c = 0; c < lines[getIndices().get(0)[0]].getWords().length; c++) {
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
		intensity = (int)Math.round((double)getIndices().size() / totalSeparation * Math.sqrt(getIndices().size()));
		setIntensity(intensity);
	}
	
	//STATIC METHODS---------------------------------------------------------------------------
	
	public static ArrayList<SoundDevice> checkInRhyme(Line[] lines) {
		ArrayList<SoundDevice> alliterations = new ArrayList<SoundDevice>();
		
		return alliterations;
	}
	
	//finds instances of alliteration in a Line[]
	public static ArrayList<Device> checkAlliteration(Line[] lines) {
		ArrayList<Device> alliterations = new ArrayList<Device>();				//(stores instances of alliteration, henceforth "alliterations")
		String sound;																		//(stores the current sound in question)
		boolean contains;																	//(stores whether current sound is stored in alliteration array)
		for(int a = 0; a < lines.length; a++) {												//go through each line
			for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word in each line
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
					//alliterations.get(alliterations.size() - 1).setDepth(1);							//...and with a depth of one
				}
			}
		}
		
		//TODO: filter out (010 -> null) and split up (01101110 -> 11 111) spread out alliterations
		for(int a = alliterations.size() - 1; a > -1; a--) {
			if(alliterations.get(a).getIndices().size() == 1) {
				alliterations.remove(a);
			}
		}
		
		//TODO: pursue alliterations to the furthers amount of the word they all share, filtering out homophones-dake no alliterations
		
		return alliterations;
	}
		
	//TODO: homophones
	
	//finds all instances of alliteration in list of words, reporting  sound, indices, and depth of alliteration
	
	/*
	public static ArrayList<SoundDevice> checkAlliteration(Word[] words) {
		ArrayList<SoundDevice> alliterations = new ArrayList<SoundDevice>();			//Stores instances of alliteration
		//ArrayList<SoundDevice> homophones = new ArrayList<SoundDevice>();				//Stores homophones (sound of entire word is the same)
		ArrayList<SoundDevice> potential = new ArrayList<SoundDevice>();				//Stores potential alliterations longer than first sound
		String sound;																	//Stores sound in question
		boolean contains;																//Stores whether ArrayList contains SoundDevice in question
		
		//**Alliterations of 1st Sound**
		//Record the starting sound of each word and their indices as instances of alliteration (aka alliterations)
		for(int a = 0; a < words.length; a++) {
			sound = words[a].getSound()[0];													//take first sound from each word
			contains = false;
			for(int b = 0; b < alliterations.size(); b++) {									//check if that sound matches any in an existing instance of alliteration
				if(alliterations.get(b).getText().equals(sound)) {
					alliterations.get(b).getIndices().add(a);								//if so, add the index of the word to the alliteration and record a match
					contains = true;
				}
			}
			if(contains == false){															//if the sound does not match an existing alliteration
				alliterations.add(new SoundDevice(sound));									//add an alliteration with that sound...
				//alliterations.get(alliterations.size() - 1).getIndices().add(a);			//...the index of the word
				alliterations.get(alliterations.size() - 1).setDepth(1);					//...and a depth of 1
			}
		}
		
		//Remove each alliteration that only occurs once (b/c its not really alliteration)
		for(int a = alliterations.size() - 1; a > -1; a--) {
			if(alliterations.get(a).getIndices().size() == 1) {
				alliterations.remove(a);
			}
		}
			
		//**Alliterations of two or more sounds**
		int i = 0;																		//Iterator
		int n = alliterations.size();													//Stores number of alliterations in array (gets updated during while loop)
		int index;																		//Stores index of word in question
		while(i < n) {
			//for an alliteration, create an ArrayList of each sound combo (1 sound longer than the sound of the alliteration) with frequencies
			for(int a = 0; a < alliterations.get(i).getIndices().size(); a++) {				//for each index of the alliterations:
				index = alliterations.get(i).getIndices().get(a);									//find the index of the word in which the alliteration occurs
				if(words[index].getSound().length > alliterations.get(i).getDepth()) {			//if we are not at the end of the word
					sound = alliterations.get(i).getText() + words[index].getSound()[alliterations.get(i).getDepth()];  //add in the next sound of the word
					contains = false;
					for(int b = 0; b < potential.size(); b++) {						//if the combined sound is contained among potential new alliterations
						if(potential.get(b).getText().equals(sound)) {
							potential.get(b).getIndices().add(index);				//add the index of the word in question
							contains = true;
						}
					}
					if(contains == false) {											//if not, add the new sound 
						potential.add(new SoundDevice(sound));
						potential.get(potential.size() - 1).setDepth(alliterations.get(i).getDepth() + 1);	  //with depth 1 greater than the alliteration it extends
						potential.get(potential.size() - 1).getIndices().add(a);							  //and add it's index
					}
				}
			}
			//remove each sound combo with only one occurrence
			for(int a = 0; a < potential.size(); a++) {
				if(potential.get(a).getIndices().size() == 1) {
					potential.remove(a);
				}
				else {
					//check if instance of alliteration is actually merely homophones
					boolean AllHomophones = true;
					for(int b = 0; b < potential.get(a).getIndices().size(); b++) {		//for each index of the word
						String[] wordSounds = words[potential.get(a).getIndices().get(b)].getSound();
						sound = "";
						for(int c = 0; c < wordSounds.length; c++) {		//store the sound of the word as a string
							sound += wordSounds[c];
						}
						if(!potential.get(a).getText().equals(sound)) {	//if the sound of the alliteration and the word are not the same
							AllHomophones = false;							//we're not talking all homophones
						}
					}
					//if not all homophones, add potential alliteration to alliteration list
					if(AllHomophones == false) {
						alliterations.add(potential.get(a));
					}
					//if all homophones, add potential alliteration to the homophones list
					else {
						//homophones.add(potential.get(a));
					}
				}
			}
			n = alliterations.size();		//update the size of the ArrayList
			i++;							//proceed to the next alliteration
		}
		//output.add(alliterations);
		//output.add(homophones);
		return alliterations;
		
	}
	*/
	
	//finds instances of assonance in a given line[]
		public static ArrayList<Device> checkAssonance(Line[] lines) {
			ArrayList<Device> output = null;														//(stores final output: Array of SoundDevices)
			ArrayList<String> vowelSounds = new ArrayList<String>();							//(stores vowel sounds found in Word[])
			ArrayList<ArrayList<int[]>> indices = new ArrayList<ArrayList<int[]>>();			//(stores indices of vowel sounds found)
			
			//record each vowel sound in the line[] and their respective indices
			for(int a = 0; a < lines.length; a++) {												//go through each line
				for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word of each line
					if(lines[a].getWords()[b].getSound() != null) {										//(if we have a pronunciation for the word)
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
			int maxScore = 0;
			for(int a = 0; a < output.size(); a++) {
				output.set(a, new SoundDevice(vowelSounds.get(a)));
				output.get(a).setIndices(indices.get(a));
				((SoundDevice) output.get(a)).buildScore(lines);
				if(output.get(a).getIntensity() > maxScore) {
					maxScore = output.get(a).getIntensity();
				}
			}
			for(int a = 0; a < output.size(); a++) {
				output.get(a).setIntensity(output.get(a).getIntensity()/maxScore*100);
			}
			
			return output;
		}
	
	//finds instances of consonance in a given line[]
	public static ArrayList<Device> checkConsonance(Line[] lines) {
		ArrayList<Device> output = null;														//(stores final output: Array of SoundDevices)
		ArrayList<String> consonantSounds = new ArrayList<String>();						//(stores consonant sounds found in Word[])
		ArrayList<ArrayList<int[]>> indices = new ArrayList<ArrayList<int[]>>();			//(stores indices of consonant sounds found)
		
		//record each consonant sound in the line[] and their respective indices
		for(int a = 0; a < lines.length; a++) {												//go through each line
			for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word of each line
				if(lines[a].getWords()[b].getSound() != null) {										//(if we have a pronunciation for the word)
					for(int c = 0; c < lines[a].getWords()[b].getConsonants().length; c++) {			//go through each consonant sound of each word of each line
						int[] index = {a, b, c};															//(stores the index of the sound in question)
						if(!consonantSounds.contains(lines[a].getWords()[b].getConsonants()[c])) {			//If it's not yet in the list of consonant sounds...
							consonantSounds.add(lines[a].getWords()[b].getConsonants()[c]);									//...add it to the list and record its index
							indices.add(new ArrayList<int[]>());
							indices.get(indices.size()-1).add(0, index);
						}
						else {																				//If it is already in the list...
							indices.get(consonantSounds.indexOf(lines[a].getWords()[b].getConsonants()[c])).add(index);		//...record another index for the consonant sound
						}
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
		
		//create consonance SoundDevices from the remaining consonant sounds and return them
		output = new ArrayList<Device>(consonantSounds.size());
		int maxScore = 0;
		for(int a = 0; a < output.size(); a++) {
			output.set(a, new SoundDevice(consonantSounds.get(a)));
			output.get(a).setIndices(indices.get(a));
			((SoundDevice) output.get(a)).buildScore(lines);
			if(output.get(a).getIntensity() > maxScore) {
				maxScore = output.get(a).getIntensity();
			}
		}
		for(int a = 0; a < output.size(); a++) {
			output.get(a).setIntensity(output.get(a).getIntensity()/maxScore*100);
		}
		return output;
	}
	
	private static int soundWeight(String sound) {
		int out = 1;
		if(sound.charAt(0) == 'A' || sound.charAt(0) == 'E' || sound.charAt(0) == 'I' || sound.charAt(0) == 'O' || sound.charAt(0) == 'U') {
			out = 5;
		}
		return out;
	}
	
}
