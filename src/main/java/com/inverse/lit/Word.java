package com.inverse.lit;

import java.util.ArrayList;

public class Word {
	
	//Word Attributes
	private String text;
	private int count;
	private String[] sound, vowels, consonants;
	private byte[] stress;
	private ArrayList<int[]> deviceIDs = new ArrayList<int[]>();
	
	//Word Static Constants
	public static final byte UNKNOWN_STRESS = -1;
	public static final byte NO_STRESS = 0;
	public static final byte PRIMARY_STRESS = 1;
	public static final byte SECONDARY_STRESS = 2;
	
	Word (String str) {
		setText(str);
		ArrayList<String> cmuList = Main.getDb().Pronunciation(str);
		if(!cmuList.isEmpty()) {//did we get anything from the DB?
			//String cmu = "S EH1 K AH0 N D EH2 R IY0";
			String cmu = cmuList.get(0);
			sound = cmu.split(" ");						//Split string from CMU into vowel and consonant sounds
			int vowelCount = 0;
			int consonantCount = 0;
			for(int a = 0; a < sound.length; a++) {		//Count number of vowel and consonant sounds in word
				if(sound[a].length() == 3) {
					vowelCount++;
				}
				else {
					consonantCount++;
				}
			}
			stress = new byte[vowelCount];				//initialize arrays of stresses, vowels, and consonants
			vowels = new String[vowelCount];
			consonants = new String[consonantCount];
			vowelCount = 0;
			consonantCount = 0;
			for(int a = 0; a < sound.length; a++) {		//populate arrays with relevant parts of CMU output
				if(sound[a].length() == 3) {
					stress[vowelCount] = (byte)(sound[a].charAt(2) - 48);
					vowels[vowelCount] = sound[a].charAt(0) + "" + sound[a].charAt(1);
					sound[a] = vowels[vowelCount];
					vowelCount++;
				}
				else {
					consonants[consonantCount] = sound[a];
					consonantCount++;
				}
			}
		
		}
		else {
			sound = new String[0];
			vowels = new String[0];
			consonants = new String[0];
			stress = new byte[0];
		}
	}

	public String getText() {
		return text;
	}
	
	public void setText(String txt) {
		text = txt.toLowerCase();
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String[] getSound() {
		return sound;
	}

	public void setSound(String[] sound) {
		this.sound = sound;
	}

	public String[] getVowels() {
		return vowels;
	}

	public void setVowels(String[] vowels) {
		this.vowels = vowels;
	}

	public String[] getConsonants() {
		return consonants;
	}

	public void setConsonants(String[] consonants) {
		this.consonants = consonants;
	}

	public byte[] getStress() {
		return stress;
	}

	public void setStress(byte[] stress) {
		this.stress = stress;
	}
	
	public ArrayList<int[]> getDeviceIDs() {
		return deviceIDs;
	}

	public void setDeviceIDs(ArrayList<int[]> deviceIDs) {
		this.deviceIDs = deviceIDs;
	}
	
	//returns the rhyme-relevant part of the sound of the word (from last stressed vowel sound to end)
	public String getRhyme() {
		String rhyme = "";
		int index = 0;
		for(int i = stress.length - 1; i > -1; i--) {		//find the index of the last stressed syllable in the vowels[] (same as in stress[])
			if(stress[i] != NO_STRESS) {
				index = i;
				i = -1;
			}
		}
		for(int i = sound.length-1; i > -1; i--) {			//find the index of the last stressed vowel in the sound[]
			if(sound[i].equals(vowels[index])){
				index = i;
				i = -1;
			}
		}
		for(int i = index; i < sound.length; i++) {			//add the last stressed vowel and all following sounds to the output
			rhyme += sound[i];
		}
		return rhyme;
	}
	
}
