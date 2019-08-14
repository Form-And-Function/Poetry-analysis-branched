//Class for Alliteration, Assonance, Consonance, and Rhyme
//25 July 2018

package com.inverse.lit;

import java.util.ArrayList;

public class SoundDevice extends Device {
	
	//STATIC VALUES
	public static int sensitivity = 1;			//maximum number of lines of separations repeated sounds can have and still be considered proximate
	public static final byte ASSONANCE_OR_CONSONANCE = 0;
	public static final byte ALLITERATION = 1;
	public static final byte INTERNAL_RHYME = 2;
	
	//CONSTRUCTOR
	public SoundDevice(String text){
		super();
		setText(text);
	}

	//NON-STATIC METHODS----------------------------------------------
	/*
	public double getRawIntensity() {
		return rawIntensity;
	}

	public void setRawIntensity(double rawIntensity) {
		this.rawIntensity = rawIntensity;
	}
	*/
	public void buildRawIntensity(Line[] lines, byte deviceType) {
		double totalSeparation = 0;								//(stores the average separation between occurrences of the sound)		
		int startIndex = 0;
		int endIndex = 0;
		
		//find the indecies of the first and last sound of the device within their respective words, to determine where to start and stop for finding total separation
		if(deviceType == ASSONANCE_OR_CONSONANCE) {
			for(int i = 0; i < lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound().length; i++) {			//find the index in the word's sound[] of the first instance of the device
				if(lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound()[i].equals(getText())){
					startIndex = i;
				}
			}
			for(int i = lines[getIndices().get(getIndices().size()-1)[0]].getWords()[getIndices().get(getIndices().size()-1)[1]].getSound().length - 1; i > -1;  i--) {			//find the index in the word's sound[] of the last instance of the device (if repeated sound, grab last sound)
				if(lines[getIndices().get(getIndices().size()-1)[0]].getWords()[getIndices().get(getIndices().size()-1)[1]].getSound()[i].equals(getText())){
					endIndex = i;
				}
			}
		}
		else if(deviceType == ALLITERATION) {
			startIndex = 0;						//sounds are located at beginnings of words
			endIndex = 0;
		}
		else if(deviceType == INTERNAL_RHYME) {
			startIndex = lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound().length - 1;		//rhyme sound ends at the end of the word
			endIndex = 0;																								//rhyme sound begins at last stressed vowel
			Word endWord = lines[getIndices().get(getIndices().size()-1)[0]].getWords()[getIndices().get(getIndices().size()-1)[1]];	//take index
			for(int i = endWord.getStress().length - 1; i > -1; i--) {		//find the index of the last stressed syllable in the vowels[] (same as in stress[])
				if(endWord.getStress()[i] != Word.NO_STRESS) {
					endIndex = i;
					i = -1;
				}
			}
			for(int i = endWord.getSound().length-1; i > -1; i--) {			//find the index of the last stressed vowel in the sound[]
				if(endWord.getSound()[i].equals(endWord.getVowels()[endIndex])){
					endIndex = i;
					i = -1;
				}
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
				totalSeparation += soundWeight(lines[getIndices().get(0)[0]].getWords()[getIndices().get(0)[1]].getSound()[c]);//TODO: Debug
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
		setRawIntensity(Math.pow(getIndices().size(), 2) / totalSeparation);
	}
	
	//STATIC METHODS (PUBLIC)---------------------------------------------------------------------------
	
	//finds instances of internal rhyme in a Line[]
	public static ArrayList<Device> checkInRhyme(Line[] lines) {
		ArrayList<Device> instances = new ArrayList<Device>();		//(stores instances of rhyme (including internal))
		String rhyme;														//(stores the current rhyme-ending being worked with (rhyme-ending == part of the word that is relevant to rhyme))
		boolean contains;													//(stores whether the current rhyme-ending is stored in the instances ArrayList yet)
		for(int a = 0; a < lines.length; a++) {							
			for(int b = 0; b < lines[a].getWords().length; b++) {
				System.out.println(61);
				rhyme = lines[a].getWords()[b].getRhyme();
				int[] index = {a, b};
				contains = false;
				System.out.println(62);
				for(int c = 0; c < instances.size(); c++) {
					if(instances.get(c).getText().equals(rhyme)) {
						instances.get(c).getIndices().add(index);
						contains = true;
						c = instances.size();
					}
				}
				System.out.println(63);
				if(!contains) {
					instances.add(new SoundDevice(rhyme));
					instances.get(instances.size()-1).getIndices().add(index);
				}
			}
		}
		
		//remove rhyme instances with only one index and indices of rhyming words separated too far from their compatriots
		instances = filter(instances);
		
		//score the intensity of each instance, normalized from 0-100 (where most intense device has score 100)
		instances = score(instances, lines, INTERNAL_RHYME);
		
		return instances;
	}
	
	//finds instances of alliteration in a Line[]
	public static ArrayList<Device> checkAlliteration(Line[] lines) {
		ArrayList<Device> instances = new ArrayList<Device>();				//(stores instances of alliteration, henceforth "alliterations")
		String sound;																		//(stores the current sound in question)
		boolean contains;																	//(stores whether current sound is stored in alliteration array)
		for(int a = 0; a < lines.length; a++) {												//go through each line
			for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word in each line
				if(lines[a].getWords()[b].getSound().length != 0) {										//(if we have a pronunciation for the word)
					sound = lines[a].getWords()[b].getSound()[0];										//grab the 1st sound of the word
					contains = false;
					for(int c = 0; c < instances.size(); c++) {										//if the sound is present in the alliterations array
						if(instances.get(c).getText().equals(sound)) {
							int[] index = {a, b};															//...record add this index to the alliteration
							instances.get(c).getIndices().add(index);
							contains = true;
							c = instances.size();
						}
					}
					if(!contains) {																		//if the sound is not present in the alliterations array
						int[] index = {a, b};
						instances.add(new SoundDevice(sound));										//add it to the alliterations array
						instances.get(instances.size() - 1).getIndices().add(index);				//...with its index
					}
				}
			}
		}
		
		//remove all alliterations with only one index, and remove all indices of an alliteration separated two far from their compatriots (determined by sensitivity)
		instances = filter(instances);
		
		//score the intensity of each instance, normalized from 0-100 (where most intense device has score 100)
		instances = score(instances, lines, ALLITERATION);
				
		return instances;
		
		//TODO: filter out alliteration that's actually just homophones
	}
		
	//TODO: homophones
	

	
	//finds instances of assonance in a given line[]
		public static ArrayList<Device> checkAssonance(Line[] lines) {
			ArrayList<Device> instances = new ArrayList<Device>();								//(stores instances of assonance found in the Line[])
			String sound;																		//(stores the current sound being worked with)
			boolean contains;																	//(stores whether instances contains the current sound)
			
			//record each consonant sound in the line[] and their respective indices
			for(int a = 0; a < lines.length; a++) {												//go through each line
				for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word of each line
					for(int c = 0; c < lines[a].getWords()[b].getVowels().length; c++) {			//go through each vowel sound of each word of each line
						int[] index = {a, b, c};															//(stores the index of the sound in question)
						sound = lines[a].getWords()[b].getVowels()[c];
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
			
			//remove all sounds with only one index and all indices that are too far apart
			instances = filter(instances);
			
			//score the intensity of each instance, normalized from 0-100 (where most intense device has score 100)
			instances = score(instances, lines, ASSONANCE_OR_CONSONANCE);
			return instances;
		}
	
	//finds instances of consonance in a given line[]
	public static ArrayList<Device> checkConsonance(Line[] lines) {
		ArrayList<Device> instances = new ArrayList<Device>();								//(stores instances of consonance found in the Line[])
		String sound;																		//(stores the current sound being worked with)
		boolean contains;																	//(stores whether instances contains the current sound)
		
		//record each consonant sound in the line[] and their respective indices
		for(int a = 0; a < lines.length; a++) {												//go through each line
			for(int b = 0; b < lines[a].getWords().length; b++) {								//go through each word of each line
				for(int c = 0; c < lines[a].getWords()[b].getConsonants().length; c++) {			//go through each sound of each word of each line
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
		instances = filter(instances);
		
		//score the intensity of each instance, normalized from 0-100 (where most intense device has score 100)
		instances = score(instances, lines, ASSONANCE_OR_CONSONANCE);
		
		return instances;
	}
	
	//STATIC METHODS (PRIVATE)----------------------------------------------------------------
	
	//returns the weight of a consonant or vowel sound (used for determining the sound difference between words)
	private static int soundWeight(String sound) {
		int out = 1;
		if(sound.charAt(0) == 'A' || sound.charAt(0) == 'E' || sound.charAt(0) == 'I' || sound.charAt(0) == 'O' || sound.charAt(0) == 'U') {
			out = 5;		//(if it's a vowel, weight it 5x more than a consonant)
		}
		return out;
	}
	
	//removes all devices with only one index and all indices that are too far apart
	private static ArrayList<Device> filter(ArrayList<Device> instances){
		for(int a = instances.size() - 1; a > -1; a--) {		//go through each recorded sound
			if(instances.get(a).getIndices().size() == 1) {			//remove all sounds with only one occurance
				instances.remove(a);
			}
			else {														//remove all repeated sounds separated from their counterparts by at least sensitivity# line(s)
				for(int b = instances.get(a).getIndices().size() - 2; b > 0; b--) {						//if a middle index is separated by more than sensitivity# line(s) from those on either side
					if(instances.get(a).getIndices().get(b)[0] - instances.get(a).getIndices().get(b-1)[0] > sensitivity && instances.get(a).getIndices().get(b+1)[0] - instances.get(a).getIndices().get(b)[0] > sensitivity) {
						instances.get(a).getIndices().remove(b);												//remove it
					}
				}
				if(instances.get(a).getIndices().get(1)[0] - instances.get(a).getIndices().get(0)[0] > sensitivity) {		//if the 1st index is separated by more than sensitivity# line(s) from the 2nd, remove it
					instances.get(a).getIndices().remove(0);													//remove it
				}																	//if the last index is separated by >= sensitivity# line(s) from the 2nd, remove it
				if(instances.get(a).getIndices().size() != 1 && instances.get(a).getIndices().get(instances.get(a).getIndices().size()-1)[0] - instances.get(a).getIndices().get(instances.get(a).getIndices().size()-2)[0] > sensitivity) {
					instances.get(a).getIndices().remove(instances.get(a).getIndices().size()-1);						//remove it
				}
				if(instances.get(a).getIndices().size() < 2) {										//if the vowel has 0-1 remaining indices, remove it
					instances.remove(a);
				}
			}
		}
		return instances;
	}
	
	//score the intensity of each instance, normalized from 0-100 (where most intense device has score 100)
	private static ArrayList<Device> score(ArrayList<Device> instances, Line[] lines, byte deviceType){
		double maxScore = 0;
		for(int i = 0; i < instances.size(); i++) {									//get the raw score of each instance and record the highest raw score
			((SoundDevice)instances.get(i)).buildRawIntensity(lines, deviceType);
			if(instances.get(i).getRawIntensity() > maxScore) {
				maxScore = instances.get(i).getRawIntensity();
			}
		}
		for(int i = 0; i < instances.size(); i++) {									//express each intensity score as a rounded percent of the maximum raw score
			instances.get(i).setIntensity((int)Math.round(100* instances.get(i).getRawIntensity()/maxScore));
		}
		return instances;
	}
	
	/*
	 * FOR FUTURE DEVELOPMENT:
	 * Make groups of repeated sounds (each group separated from its compatriots by more than sensitivity# lines) individual Device instances that can be individually homophone-filtered and checked for intensity
	 * Find homophones, internal rhyme, alliteration scheme
	 */

	
}
