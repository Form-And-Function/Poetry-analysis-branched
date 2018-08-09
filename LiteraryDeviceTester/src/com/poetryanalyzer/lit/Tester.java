package com.poetryanalyzer.lit;

import java.util.ArrayList;

public class Tester {

	public Tester (Line[] poemLines) {
		display(MultiLineDevice.checkRepetition(poemLines));
	}
	
	public static void main (String[] args) {
		
		ArrayList<String> poem = new ArrayList<String>();
		
		/*poem.add("Two roads diverged in a yellow wood,");
		poem.add("And sorry I could not travel both");
		poem.add("And be one traveler, long I stood");
		poem.add("And looked down one as far as I could");
		poem.add("To where it bent in the undergrowth;");*/
		
		
		//poem.add("I want to fish and hunt and eat little animals");
		
		/*poem.add("I came, I saw, I conquered.");
		
		Line[] poemLines = new Line[poem.size()];
		
		for (int x = 0; x < poemLines.length; x++) {
			poemLines[x] = new Line (poem.get(x));
		}
		
		for (Line x : poemLines) { System.out.println(x.getText()); } //TESTING
		System.out.println(); //TESTING
		
		Tester MedleyPoem = new Tester (poemLines);*/
		
		double test1 = 3;
		int test2 = 2;
		System.out.println(test1/test2);
		
		
	}
	
	public void display (ArrayList<MultiLineDevice> instances) {
		for (MultiLineDevice i : instances) {
			System.out.println(i.getText());
			System.out.println();
			for (int[] iArray : i.getIndices()) {
				for (int iNT : iArray) {
					System.out.println(iNT);
				}
			}
			System.out.println();
		}
	}
}
