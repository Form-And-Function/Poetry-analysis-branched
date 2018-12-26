package com.inverse.lit;

import java.util.ArrayList;

public class MultiLineDevice extends Device {
	
	//MULTIPLE
	private static final String[] conjuncs = {"and", "or", "but", "nor", "for", "yet", "so"}; //common conjunctions
	
	//ANAPHORA
	private static final int minLineSpacingToBeDistinctAnaphora = 3;
	
	//POLYSYNDETON
	private static final int minConjuncsToBePolysyndeton = 2;
	
	//ASYNDETON
	private static final int minAbsentConjuncsToBeAsyndeton = 2;
	
	public MultiLineDevice () {
		
	}
	
	public static void calcIntensities (Device instance, Line[] lines) {
		
		//FREQUENCY half
		double frequency = 0;
		
		for (int i = 1; i < instance.getIndices().size(); i++) {
			
			int[] currIndices = instance.getIndices().get(i);
			int[] pastIndices = instance.getIndices().get(i - 1);
			
			if (currIndices.length == 1) { //line number
				frequency += currIndices[0] - pastIndices[0];
				
			} /*else {				   //line number, character number
				frequency += indices[0] - indices[0];
				
			}*/	
		}
		
		frequency /= instance.getIndices().size() - 1;
		
		
		//VOLUME half
		int totalWords = 0;
		
		for (Line x : lines) {
			totalWords += x.getWords().length;
		}
		
		int instanceWords = instance.getIndices().size();
		
		
		//COMBINE
		double volumeMultiplier = ((double) instanceWords / lines.length);
		
		//SET INTENSITIES
		instance.setRawIntensity(100 * (volumeMultiplier / frequency));
		instance.setIntensity( (int) (100 * (volumeMultiplier / frequency))); 
	}
	
	public static ArrayList<Device> checkRepetition (Line[] lines) {
		
		ArrayList<Device> reps = new ArrayList<Device>();
		ArrayList<String> words = new ArrayList<String>();
		
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
	
	public static ArrayList<Device> checkAnaphora (Line[] lines) {
		
		ArrayList<Device> anaInstances = new ArrayList<Device>();
		ArrayList<String> anaphoricWords = new ArrayList<String>();
		
		for (int x = 0; x < lines.length; x++) {
			if(lines[x].getWords().length<=0){
				continue;
			}
			String firstWord = lines[x].getWords()[0].getText();
					
			if (!anaphoricWords.contains(firstWord) || 
			    x - anaInstances.get(anaphoricWords.indexOf(firstWord)).getIndices()
			    .get(anaInstances.get(anaphoricWords.indexOf(firstWord)).getIndices().size() - 1)[0]
			    > minLineSpacingToBeDistinctAnaphora) {
				
					anaphoricWords.add(firstWord);
					anaInstances.add(new MultiLineDevice());
					anaInstances.get(anaInstances.size() - 1).setText(firstWord);
					anaInstances.get(anaInstances.size() - 1).getIndices().add(new int[]{x,0});
					
			} else {
				anaInstances.get(anaphoricWords.indexOf(firstWord)).getIndices().add(new int[]{x,0});
			}
		}
		System.out.println("reached 71");
		for (int i = anaInstances.size() - 1; i >= 0; i--) {
			if (anaInstances.get(i).getIndices().size() == 1) {
				anaInstances.remove(i);
			}
		}
		System.out.println("reached 72");
		for (Device a : anaInstances) {
			int minWords = Integer.MAX_VALUE;
			
			for (int i = 0; i < a.getIndices().size(); i++) {
				int val = a.getIndices().get(i)[0];
				
				if (lines[val].getWords().length < minWords)
					minWords = lines[val].getWords().length;
			}
			
			boolean escape = false;
			
			for (int w = 1; w < minWords; w++) {
				if (escape)
					break;
				
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
			MultiLineDevice.calcIntensities(a, lines);
		}
		
		return anaInstances;
	}
	
	public static ArrayList<Device> checkPolysyndeton (Line[] lines) {
		
		ArrayList<Device> polyInstances = new ArrayList<Device>();
		
		String conjuncBuscar = "";
		int conjuncInstances = 0;
		
		for (int x = 0; x < lines.length; x++) {
			System.out.println(x);
			if(lines[x].getWords().length<=0){
				continue;
			}
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
								if(lines[h].getWords().length <= v){
									continue;
								}
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
		
		for (Device p : polyInstances) {
			MultiLineDevice.calcIntensities(p, lines);
		}
		
		return polyInstances;
		
	}

    public static boolean CheckLineAsyndeton(Line line){
	    var lineText = line.getText();
        int commaCount = lineText.length() - lineText.replace(".", "").length(); //count the commas
        if (commaCount < 3) {
            return false;
        }
        var words = line.getWords();
        for (var i = 0; i < words.length; i++) {
            var word = words[i];
            for (var conjunction : conjuncs) {
                if (word.equals(conjunction)) {
                    return false;
                }
            }
        }
        /*var strength = commaCount / words.length; //TODO: use this
        if (strength > 1) {
            strength = 1;
        }*/
	    return true;
    }


	public static ArrayList<Device> checkAsyndeton (Line[] lines) {

        ArrayList<Device> asynInstances = new ArrayList<Device>();
        var streak = false;
		/*boolean carryOver = false;
		int count = 0;
		int v = 0;
		int b = 0;*/

        for (int x = 0; x < lines.length; x++) {

            String lineText = lines[x].getText();


            var isAsyndeton = CheckLineAsyndeton(lines[x]);
            if (isAsyndeton){
                if(streak){
                    asynInstances.get(asynInstances.size() - 1).getIndices().add(new int[]{x,0}); //TODO: make words specific
                }
                else{
                    streak = true;
                    var device = new MultiLineDevice();
                    device.getIndices().add(new int[]{x,0});//TODO: make words specific
                    asynInstances.add(device);
                }
            }

            /*String word = "";

			for (int c = 0; c < lineText.length(); c++) {

				if (lineText.substring(c, c + 1).equals(",") || carryOver) { //if the line has a comma
					
					if (c != lineText.length() - 1) { //if the character index is less than the line length,
						
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
							} else if (count >= minAbsentConjuncsToBeAsyndeton) {
								if (count == minAbsentConjuncsToBeAsyndeton) {
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
		*/

        }

        return asynInstances;
    }
}