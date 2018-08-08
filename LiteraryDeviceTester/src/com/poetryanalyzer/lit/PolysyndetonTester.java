package com.poetryanalyzer.lit;

import java.util.ArrayList;

public class PolysyndetonTester {

	public PolysyndetonTester (Line[] poemLines) {
		//display(MultiLineDevice.checkPolysyndeton(poemLines));
		//displayRepetition(MultiLineDevice.checkRepetition(poemLines));
		displayRepetition(MultiLineDevice.checkAsyndeton(poemLines));
	}
	
	public static void main (String[] args) {
		/*ArrayList<String> poem = new ArrayList<String>();
		
		poem.add("And God said, \"Let the earth bring forth the living creature after his kind, cattle, and creeping thing, and beast of the earth after his kind.\"");
		poem.add("And it was so.");
		poem.add("And God made the beast of the earth after his kind and cattle after their kind, and everything that creeps upon the earth to its kind. ");
		poem.add("And God saw that it was good.");
		
		poem.add("Sam ran and jumped and laughed and");
		poem.add("clapped yet played and sang and wrote and cried badly");
		
		Line[] poemLines = new Line[poem.size()];
		
		for (int x = 0; x < poemLines.length; x++) {
			poemLines[x] = new Line (poem.get(x));
		}
		
		for (Line x : poemLines) { System.out.println(x.getText()); } //TESTING
		System.out.println(); //TESTING
		
		PolysyndetonTester polysyndetonTester = new PolysyndetonTester (poemLines);*/
		
		ArrayList<String> poem = new ArrayList<String>();
		
		//poem.add("Alexander has has has a stut stut er");
		//poem.add("he can't fit everything he he has to say on one line because of his stut er");
		
		poem.add("Standing tall at the crowd he exclaimed");
		poem.add("I came, I saw, I conquered,");
		poem.add("I thought this might work");
		
		Line[] poemLines = new Line[poem.size()];
		
		for (int x = 0; x < poemLines.length; x++) {
			poemLines[x] = new Line (poem.get(x));
		}
		
		for (Line x : poemLines) { System.out.println(x.getText()); } //TESTING
		System.out.println(); //TESTING
		
		PolysyndetonTester polysyndetonTester = new PolysyndetonTester (poemLines);
	}
	
	public void display (ArrayList<MultiLineDevice> polysyndetonInstances) {
		for (MultiLineDevice a : polysyndetonInstances) {
			System.out.println("Text: " + a.getText());
			//System.out.println("Intensity: " + a.getIntensity());
			System.out.println();
			for (int i = 0; i < a.getIndices().size(); i++) {
				System.out.println("line: " + a.getIndices().get(i)[0] + " " 
			                     + "word: " + a.getIndices().get(i)[1]);
			}
			System.out.println(); 
		}
	}
	
	public void displayRepetition (ArrayList<MultiLineDevice> asyndetonInstances) {
		/*for (MultiLineDevice r : repetitionInstances) {
			System.out.println("repeating word: " + r.getText());
			for (int i = 0; i < r.getIndices().size(); i++) {
				System.out.println("line: " + r.getIndices().get(i)[0] + " " + "word: " + r.getIndices().get(i)[1]);
			}
			System.out.println();
		}*/
		
		for (MultiLineDevice asyn : asyndetonInstances) {
			for (int i = 0; i < asyn.getIndices().size(); i++) {
				System.out.println("line: " + asyn.getIndices().get(i)[0] + " " +  
						           "index: " + asyn.getIndices().get(i)[1]);
			}
			System.out.println();
		}
	}
}
