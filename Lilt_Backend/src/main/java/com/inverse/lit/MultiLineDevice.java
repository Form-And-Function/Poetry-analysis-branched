package com.inverse.lit;

import java.util.ArrayList;

public class MultiLineDevice extends Device {
	
	//MULTIPLE
	private static final String[] conjuncs = {"and", "or", "but", "nor", "for", "yet", "so"}; //common conjunctions
	
	//ANAPHORA
	private static final int minLineSpacingToBeDistinctAnaphora = 3;
	
	//POLYSYNDETON
	private static final int minConjuncsToBePolysyndeton = 3;
	
	//ASYNDETON
	private static final int minAbsentConjuncsToBeAsyndeton = 2;
	
	public MultiLineDevice () {
		
	}
	
	public static ArrayList<MultiLineDevice> checkRepetition   (Line[] lines) {
		
		ArrayList<MultiLineDevice> reps = new ArrayList<MultiLineDevice>();
		ArrayList<String> 	 	  words = new ArrayList<String>();
		
		for (int x = 0; x < lines.length; x++) {
			for (int w = 0; w < lines[x].getWords().length; w++) {
		
				String word = lines[x].getWords()[w].getText();
				
				if (!words.contains(word)) {
					words.add(word);
					reps.add(new MultiLineDevice());
					reps.get(reps.size() - 1).setText(word);
					reps.get(reps.size() - 1).getIndices().add(new int[]{x,w});
				} else
					reps.get(words.indexOf(word)).getIndices().add(new int[]{x,w});
				
			}
		}
		
		for (int r = reps.size() - 1; r >= 0; r--)
			if (reps.get(r).getIndices().size() == 1) 
				reps.remove(r);
		
		return reps;
	}
	
	public static ArrayList<MultiLineDevice> checkAnaphora     (Line[] lines) {
		
		ArrayList<MultiLineDevice> anaInstances = new ArrayList<MultiLineDevice>();
		ArrayList<String> 		 anaphoricWords = new ArrayList<String>();
		
		for (int x = 0; x < lines.length; x++) {
			String firstWord = lines[x].getWords()[0].getText();
					
			if (!anaphoricWords.contains(firstWord) || 
			    x - anaInstances.get(anaphoricWords.indexOf(firstWord)).getIndices()
			    .get(anaInstances.get(anaphoricWords.indexOf(firstWord)).getIndices().size() - 1)[0]
			    > minLineSpacingToBeDistinctAnaphora) {
				
					anaphoricWords.add(firstWord);
					anaInstances.add(new MultiLineDevice());
					anaInstances.get(anaInstances.size() - 1).setText(firstWord);
					anaInstances.get(anaInstances.size() - 1).getIndices().add(new int[]{x});
					
			} else {
				anaInstances.get(anaphoricWords.indexOf(firstWord)).getIndices().add(new int[]{x});
			}
		}
		
		for (int i = anaInstances.size() - 1; i >= 0; i--) {
			if (anaInstances.get(i).getIndices().size() == 1) {
				anaInstances.remove(i);
			}
		}
		
		for (MultiLineDevice a : anaInstances) {
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
		
		return anaInstances;
	}
	
	public static ArrayList<MultiLineDevice> checkPolysyndeton (Line[] lines) {
		
		ArrayList<MultiLineDevice> polyInstances = new ArrayList<MultiLineDevice>();
		
		String conjuncBuscar = "";
		int conjuncInstances = 0;
		
		for (int x = 0; x < lines.length; x++) {
			for (int w = 0; w < lines[x].getWords().length; w++) {
				String text = lines[x].getWords()[w].getText().toLowerCase();
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
						polyInstances.add(new MultiLineDevice());
			    		polyInstances.get(polyInstances.size() - 1).setText(conjuncBuscar);
			    		ArrayList<int[]> indices = polyInstances.get(polyInstances.size() - 1).getIndices();
			    		
			    		for (int h = x; h >= 0; h--) {
			    			for (int v = w; v >= 0; v--) {
			    				String word = lines[h].getWords()[v].getText();
			    				
			    				if (word.equals(conjuncBuscar)) {
			    					indices.add(0, new int[]{h,v});
			    					
			    				} else {
			    					for (String c : conjuncs) {
			    						if (word.equals(c)) {
			    							v = -1;
			    							h = -1;
			    						}
			    					}
			    				} 
			    			}
			    		}
			    		
					} else if (conjuncInstances > minConjuncsToBePolysyndeton) {
						polyInstances.get(polyInstances.size() - 1).getIndices().add(new int[]{x,w});
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
		
		return polyInstances;
		
	}

	public static ArrayList<MultiLineDevice> checkAsyndeton    (Line[] lines) {
		
		ArrayList<MultiLineDevice> asynInstances = new ArrayList<MultiLineDevice>();
		
		boolean carryOver = false;
		int count = 0;
		int v = 0;
		int b = 0;
		
		for (int x = 0; x < lines.length; x++) {
			
			String lineText = lines[x].getText();
			String word = "";
			
			for (int c = 0; c < lineText.length(); c++) {
				
				if (lineText.substring(c, c + 1).equals(",") || carryOver) {
					
					if (c != lineText.length() - 1) {
						
						if (!carryOver) {
							word = lineText.substring(c + 2, lineText.indexOf(" ", c + 2));
						} else {
							int nonLet;
							
							for (nonLet = 0; nonLet < lineText.length(); nonLet++) {
								if (Character.isLetter(lineText.charAt(nonLet))) {
									break;
								}
							}
							word = lineText.substring(0, nonLet);
						}
						
						boolean isConjunc = false;
						
						for (String conjunc : conjuncs) {
							if (word.equals(conjunc)) {
								isConjunc = true;
								break;
							}
						}
						
						if (!isConjunc) {
							count++;
							
							if (count == 1) {
								v = x;
								b = c;
							} else if (count >= 2) {
								if (count == 2) {
									asynInstances.add(new MultiLineDevice());
									asynInstances.get(asynInstances.size() - 1).getIndices().add(new int[]{v,b});
								}
								asynInstances.get(asynInstances.size() - 1).getIndices().add(new int[]{x,c});
							}
						} else {
							count = 0;
						}
						
						if (carryOver)
							carryOver = false;
						
					} else {
						carryOver = true;
					}
				}
			}
		}
		
		return asynInstances;
		
	}
}