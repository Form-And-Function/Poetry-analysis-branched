package com.inverse.lit;

import java.util.ArrayList;

public class Scansion {
	
	//ATTRIBUTES / DYNAMIC VARIABLES
	private boolean[][] stress;						//(stores all the stresses in the poem according to this scansion)
	private ArrayList<ArrayList<Integer>> feet;		//(stores the feet of the poem);
	private int[] meterType;						//(stores the meter type (ex: loose iambic) of each line)
	private int[] footCount;						//(stores the number of feet (ex: 4) in each line)
	private int OverallMeterType;					//(stores type of meter, eg. loose iambic)
	private int OverallFootCount;					//(stores number of feet per line, eg. tetrameter)
	
	//STATIC CONSTANTS
	
	public static final byte AMBIGUOUS = -2;				//could be iambic or trochaic, base decision on characterization of other lines
	public static final byte OTHER = 0;
	public static final int LOOSE_IAMBIC = 280;
	public static final int LOOSE_TROCHAIC = 330;
	//Strict meters use the the value of the individual foot (ex: Strict Iambic == 14)
	
	//Foot ID's															  index
	public static final byte UNKNOWN = -1;				//     ?
	public static final byte UNSTRESS = 6;				//     U			0
	public static final byte STRESS = 8;				//     /			2
	
	public static final byte PYRRHUS = 12;				//    U U			6
	public static final byte IAMB = 14;					//    U /			8
	public static final byte TROCHEE = 15;				//    / U			9
	public static final byte SPONDEE = 17;				//    / /			11
	
	public static final byte TRIBRACH = 18;				//   U U U			12
	public static final byte ANAPEST = 20;				//   U U /			14
	//public static final byte AMPHIBRACH = 21;			//   U / U
	public static final byte DACTYL = 22;				//   / U U			16
	//public static final byte BACCHIUS = 23;			//   U / /
	//public static final byte CRETIC = 24;				//   / U /
	//public static final byte ANTIBACCHIUS = 25;		//   / / U
	//public static final byte MOLOSSUS = 27;			//   / / /
	
	//CONSTRUCTOR
	public Scansion(boolean[][] stress) {
		this.stress = stress;
		setFeet(new ArrayList<ArrayList<Integer>>());
		meterType = new int[stress.length];
	}
	
	//DYNAMIC METHODS---------------------------------------------------
	
	public void buildMeter() {
		ArrayList<Boolean> cFoot = new ArrayList<Boolean>();			//(stores the current foot being built/worked with)
		ArrayList<int[]> lineFeet;										//(stores the current line's feet and the count of each foot)
		int[] feetCount;												//(stores the count of each foot in line (stored by index)
		int cFootID = 0;												//(stores the ID of the current foot being worked with
		
		//ID each foot and characterize scansion of each line
		for(int a = 0; a < stress.length; a++) {					//go through each line
			lineFeet = new ArrayList<int[]>();
			feetCount = new int[17];
			for(int b = stress[a].length - 1; b > -1; b--) {			//go through all stresses in the line, back to front
				if(cFoot.size() < 2) {										//if the current foot is under two syllables, add the current syllable to it
					cFoot.add(stress[a][b]);
				}
				else {														//if the current foot is two or more syllables
					cFootID = 6*cFoot.size();									//calculate which foot it is
					for (int c = 0; c < cFoot.size(); c++) {
						cFootID += bti(cFoot.get(c))*(c+2);
					}
					if((stress[a][b] && cFootID > 12)							//if it's got a stress and we've hit a stressed syllable
							|| (!stress[a][b] && cFootID > 14)					//...or if it's not an iamb or pyrrhus and we've hit an unstressed syllable
							|| cFoot.size() == 3) {								//...or if the foot is three syllables long
						feet.get(a).add(cFootID);									//add the  current foot to the feet ArrayList
						feetCount[cFootID - 6]++;									//(record the number of each foot)
						cFoot.clear();												//and start a new foot with the current syllable
						cFoot.add(stress[a][b]);
					}
					else {														//otherwise, add the current syllable to the foot
						cFoot.add(stress[a][b]);
					}
				}
			}
			if(!cFoot.isEmpty()) {												//if the last foot of the line has not been loaded in
				cFootID = 6*cFoot.size();											//calculate which foot it is
				for (int c = 0; c < cFoot.size(); c++) {
					cFootID += bti(cFoot.get(c))*(c+2);
				}
				feet.get(a).add(cFootID);										//load it in to the feet array and reset current foot
				feetCount[cFootID - 6]++;										//(get the relevant count of it)
				cFoot.clear();
			}
			for(int b = 0; b < feetCount.length; b++) {							//Create an arraylist storing feet and their counts
				if(feetCount[b] > 0 && b > 5) {									//...including only 2-3 syllable feet actually present in the line
					int[] foot = {feetCount[b], b+6};
					lineFeet.add(foot);
				}
			}
			if(stress[a][0] && stress[a][stress[a].length-1]) {					//if both first and last syllable of line is stressed
				meterType[a] = -2;													//...mark meter of line as Trochaic/Anapestic ambiguous
			}
			else if(lineFeet.size() == 1) {											//if the line has only one type of foot
				meterType[a] = lineFeet.get(0)[1];									//...mark it as strictly that foot
			}
			else if(lineFeet.size() == 2) {										//if the line has two types of foot
				meterType[a] = lineFeet.get(0)[1]*lineFeet.get(1)[1];
				if(meterType[a] != LOOSE_IAMBIC && meterType[a] != LOOSE_TROCHAIC) {			//if it's a loose meter, mark as such
					meterType[a] = 0;															//otherwise mark as other
				}
			}
			else {																//if line has more than two types of foot, mark as other
				meterType[a] = 0;
			}
		}
		
		//Determine overall meter pattern of poem
		//TODO: look at individual sections/stanzas

			//(If poem has overall Trochaic pattern, switch trochaic/iambic ambiguous (1 ... 1) lines to iambic pattern)
			//(If poem has overall Iambic pattern, switch ambiguous lines to iambic) (**is it possible to need this?)
		
		
		
	}
	
	//returns the likelihood that this scansion is the actual scansion of the poem, as raw number
	public int getScore() {
		int score = 0;
		
		return score;
	}

	public boolean[][] getStress() {
		return stress;
	}

	public void setStress(boolean[][] stress) {
		this.stress = stress;
	}

	public int[] getMeterType() {
		return meterType;
	}

	public void setMeterType(int[] meterType) {
		this.meterType = meterType;
	}

	public int[] getFootCount() {
		return footCount;
	}

	public void setFootCount(int[] footCount) {
		this.footCount = footCount;
	}

	public int getOverallMeterType() {
		return OverallMeterType;
	}

	public void setOverallMeterType(int overallMeterType) {
		OverallMeterType = overallMeterType;
	}

	public int getOverallFootCount() {
		return OverallFootCount;
	}

	public void setOverallFootCount(int overallFootCount) {
		OverallFootCount = overallFootCount;
	}

	public ArrayList<ArrayList<Integer>> getFeet() {
		return feet;
	}

	public void setFeet(ArrayList<ArrayList<Integer>> feet) {
		this.feet = feet;
	}
	
	//STATIC METHODS---------------------------------------------------
	
	public static byte bti(boolean in) {
		byte out = 0;
		if(in == true) out = 1;
		return out;
	}
	
}
