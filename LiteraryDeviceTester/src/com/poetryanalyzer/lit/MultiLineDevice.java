package com.poetryanalyzer.lit;

import java.util.ArrayList;

public class MultiLineDevice extends Device {
	
	//ANAPHORA
	private static final int minLineSpacingToBeDistinctAnaphora = 3;
	
	//POLYSYNDETON
	private static final String[] conjuncs = {"and", "or", "but", "nor", "for", "yet", "so"}; //common conjunctions
	private static final int minConjuncsToBePolysyndeton = 3;
	
	//ASYNDETON
	
	public MultiLineDevice () {
		
	}
	
	public static ArrayList<MultiLineDevice> checkAnaphora (Line[] lines) {
		
		ArrayList<MultiLineDevice> anaphoraInstances = new ArrayList<MultiLineDevice>();
		ArrayList<String> anaphoricWords = new ArrayList<String>();
		
		for (int i = 0; i < lines.length; i++) {
			String firstWord = lines[i].getWords()[0].getText();
					
			if (!anaphoricWords.contains(firstWord) || 
			    i - anaphoraInstances.get(anaphoricWords.indexOf(firstWord)).getIndices()
			    .get(anaphoraInstances.get(anaphoricWords.indexOf(firstWord)).getIndices().size() - 1)[0]
			    > minLineSpacingToBeDistinctAnaphora) {
				
					anaphoricWords.add(firstWord);
					anaphoraInstances.add(new MultiLineDevice());
					anaphoraInstances.get(anaphoraInstances.size() - 1).setText(firstWord);
					anaphoraInstances.get(anaphoraInstances.size() - 1).getIndices().add(new int[]{i});
					
			} else {
				anaphoraInstances.get(anaphoricWords.indexOf(firstWord)).getIndices().add(new int[]{i});
			}
		}
		
		for (int i = anaphoraInstances.size() - 1; i >= 0; i--) {
			if (anaphoraInstances.get(i).getIndices().size() == 1) {
				anaphoraInstances.remove(i);
			}
		}
		
		for (MultiLineDevice a : anaphoraInstances) {
			int minWords = Integer.MAX_VALUE;
			
			for (int i = 0; i < a.getIndices().size(); i++) {
				int val = a.getIndices().get(i)[0];
				
				if (lines[val].getWords().length < minWords)
					minWords = lines[val].getWords().length;
			}
			
			for (int w = 1; w < minWords; w++) {
				boolean escape = false;
				String nextWord = lines[a.getIndices().get(0)[0]].getWords()[w].getText();
				for (int i = 0; i < a.getIndices().size(); i++) {
					int val = a.getIndices().get(i)[0];
					
					if (!lines[val].getWords()[w].getText().equals(nextWord)) {
						escape = true;
						break;
					}
					if (escape)
						break;
					
					if (i == a.getIndices().size() - 1)
						a.setText(a.getText() + " " + nextWord);
				}
			}
		}
		
		return anaphoraInstances;
	}
	
	/*public static ArrayList<MultiLineDevice> checkAnaphora (Line[] lines) {		//looks for anaphora (words/phrases repeated at the beginning of each line for poetic effect)
																																//>for poetic effect)
		for (MultiLineDevice a : anaphoraInstances) { //for each instance of anaphora
			int minWords = Integer.MAX_VALUE;				//iterates through each line that contains an instance of this anaphora and
															//-finds the shortest line by words, and stores that number
			for (Double i : a.getIndices())					//
				if (lines[i.intValue()].getWords().length < minWords)	//
					minWords = lines[i.intValue()].getWords().length;	//
			
			for (int w = 1; w < minWords; w++) {			//checks word by word "deeper", or to the right in the poem
				boolean escape = false;														//-until there is no longer an identical 
				String nextWord = lines[a.getIndices().get(0).intValue()].getWords()[w].getText();		//-anaphora on all indices
				for (Double i : a.getIndices()) {
					if (!lines[i.intValue()].getWords()[w].getText().equals(nextWord)) {	//if the words on the ith word column do not match, break
						escape = true;
						break;
					}
				}
				if (escape)
					break;
				else
					a.setText(a.getText() + " " + nextWord); //if the words on the i-th word column do match, continue checking to the right
			}																								
		}
		
		return anaphoraInstances; //return an ArrayList of MultiLineDevices, where each MultiLineDevice is a unique anaphora or word phrase
	}*/
 	
	/*public static ArrayList<MultiLineDevice> checkPolysyndeton (Line[] lines) {
		
		ArrayList<MultiLineDevice> polysyndetonInstances = new ArrayList<MultiLineDevice>();
		
		String conjuncBuscar = "";
		int    conjuncInstances = 0;
		
		for (int i = 0; i < lines.length; i++) {
			for (int w = 0; w < lines[i].getWords().length; w++) {
				String text = lines[i].getWords()[w].getText().toLowerCase();
			    if (conjuncBuscar.equals("")) {
			    	for (String conjunc : conjuncs) {
			    		if (text.equals(conjunc)) {
			    			conjuncBuscar = text;
			    			conjuncInstances++;
			    			break;
			    		}
			    	}
			    } else if (text.equals(conjuncBuscar)) {
			    	conjuncInstances++;
			    	if (conjuncInstances == minConjuncsToBePolysyndeton) {
			    		polysyndetonInstances.add(new MultiLineDevice());
			    		polysyndetonInstances.get(polysyndetonInstances.size() - 1).setText(conjuncBuscar);
			    		ArrayList<Double> indices = polysyndetonInstances.get(polysyndetonInstances.size() - 1).getIndices();
			    		
			    		for (int j = 0; j < i; j++) {
			    			for (int v = 0; v < w; v++) {
			    				String pastWord = lines[j].getWords()[v].getText();
			    				if (pastWord.equals(conjuncBuscar)) {
			    					//indices.add((double)i + Math.pow(v, b));
			    				}
			    			}
			    		}
			    		
			    		/*for (int x = 0; x <= w; x++) {
			    			String pastWord = lines[i].getWords()[x].getText();
			    			if (pastWord.equals(conjuncBuscar)) {
			    				indices.add(x);
			    			}
			    		
			    	} else if (conjuncInstances > 2) {
			    		polysyndetonInstances.get(polysyndetonInstances.size() - 1).getIndices().add(w);
			        }
			    } else if (!conjuncBuscar.equals("")) {
			    	for (String conjunc : conjuncs) {
			    		if (text.equals(conjunc)) {
			    			conjuncBuscar = "";
			    			conjuncInstances = 0;
			    		}
			    	}
			    }
		    } 
		}
	
		return polysyndetonInstances;
	}
	
	public static ArrayList<MultiLineDevice> checkAsyndeton (Line[] lines) {
		
		ArrayList<MultiLineDevice> asyndetonInstances = new ArrayList<MultiLineDevice>();
		
		boolean comma = false;
		
		for (int i = 0; i < lines.length; i++) {
			for (String s : lines[i].getText().split(" ")) {
				
				
				if (s.contains(",")) {
					
				} else {
					for (String c : conjuncs)
						if (s.equals(c)) {
							
						}
							
				}
			}
		}
		
		return asyndetonInstances;
	}
	
	/*public static ArrayList<MultiLineDevice> checkAnaphora (Line[] lines) {		//looks for anaphora (words/phrases repeated at the beginning of each line for poetic effect)
	//>for poetic effect)
ArrayList<MultiLineDevice> anaphoraInstances = new ArrayList<MultiLineDevice>(); //stores instances
ArrayList<String> anaphoricWords = new ArrayList<String>(); 					 //stores first words of instances

for (int i = 0; i < lines.length; i++) {					//for each line
String firstWord = lines[i].getWords()[0].getText();	//get the first word

if (!anaphoricWords.contains(firstWord) || 
(i - anaphoraInstances.get
(anaphoricWords.indexOf(firstWord)).getIndices().get
(anaphoraInstances.get
(anaphoricWords.indexOf(firstWord)).getIndices().size() - 1) > minLineSpacingToBeDistinctAnaphora) ) {

anaphoricWords.add(firstWord);						
anaphoraInstances.add(new MultiLineDevice());						      		 
anaphoraInstances.get(anaphoraInstances.size() - 1).setText(firstWord);		 
anaphoraInstances.get(anaphoraInstances.size() - 1).getIndices().add((double)i);

} else {	
anaphoraInstances.get(anaphoricWords.indexOf(firstWord)).getIndices().add((double)i); //add its index to the list of indices for that word
}
}

for (int p = anaphoraInstances.size() - 1; p >= 0; p--) {	//removes anaphora instances with only one index, i.e., non-anaphoras
if (anaphoraInstances.get(p).getIndices().size() == 1)	
anaphoraInstances.remove(p);
}

for (MultiLineDevice a : anaphoraInstances) { //for each instance of anaphora
int minWords = Integer.MAX_VALUE;				//iterates through each line that contains an instance of this anaphora and
//-finds the shortest line by words, and stores that number
for (Double i : a.getIndices())					//
if (lines[i.intValue()].getWords().length < minWords)	//
minWords = lines[i.intValue()].getWords().length;	//

for (int w = 1; w < minWords; w++) {			//checks word by word "deeper", or to the right in the poem
boolean escape = false;														//-until there is no longer an identical 
String nextWord = lines[a.getIndices().get(0).intValue()].getWords()[w].getText();		//-anaphora on all indices
for (Double i : a.getIndices()) {
if (!lines[i.intValue()].getWords()[w].getText().equals(nextWord)) {	//if the words on the ith word column do not match, break
escape = true;
break;
}
}
if (escape)
break;
else
a.setText(a.getText() + " " + nextWord); //if the words on the i-th word column do match, continue checking to the right
}																								
}

return anaphoraInstances; //return an ArrayList of MultiLineDevices, where each MultiLineDevice is a unique anaphora or word phrase
}*/

}
