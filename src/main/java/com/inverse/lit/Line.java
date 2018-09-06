package com.inverse.lit;

import java.util.ArrayList;
import com.inverse.lit.DeviceList;

public class Line {
	
	//Attributes
	private Word[] words;
	private String text;
	private DeviceList devices;
	
	//CONSTRUCTOR
	public Line (String lineText) {
		text = lineText;														//Store text of line under Line.text
		System.out.println(text);		//TODO: Debug
		lineText += " ";														//(adds padding to end of line text to allow iterating 1 char past last word)
		ArrayList<String> wordStrings = new ArrayList<String>();				//(stores String of each word in line)
		int startIndex = 0;														//(stores index at beginning of word)
		boolean startOfWord = true;												//(stores whether at the start of a word)
		char c;																	//(stores the current char being looked at)
		for(int endIndex = 0; endIndex < lineText.length(); endIndex++) {		//go through each character
			c = lineText.charAt(endIndex);
		    if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z') && (c != '\'')) {		//if it isn't a letter or apostrophe
			    if(!startOfWord) {														//...and if it's not at the start of the word, we're at the end of a word
			    	wordStrings.add(lineText.substring(startIndex, endIndex));			//add the word before char c to our list of words
			    	startOfWord = true;														//next character will be at start of word, record
					System.out.println("word is: "+lineText.substring(startIndex, endIndex));
			    }
		    	startIndex = endIndex + 1;										//move the index starting the word to the char after
		    }
		    else {																//if it's a letter or apostrophe, we're in the middle of a word, record that
		    	startOfWord = false;
		    }
		}
	    words = new Word[wordStrings.size()];
	    for(int i = 0; i < words.length; i++) {
	    	words[i] = new Word(wordStrings.get(i));
	    }
	}
	
	
	public Word[] getWords() {
		return words;
	}

	public void setWords(Word[] words) {
		this.words = words;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	

	public DeviceList getDevices() {
		return devices;
	}

	public void setDevices(DeviceList devices) {
		this.devices = devices;
	}
	
}