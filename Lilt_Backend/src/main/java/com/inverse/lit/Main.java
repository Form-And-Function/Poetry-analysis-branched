package com.inverse.lit;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("{poem}")
public class Main {
	//Main Class of Poetry Analyzer
	//GUI and Central Control
	//by: Alexander Dyall, Alex Gitteau, & Sydney Von Arx
	//23 July 2018
	 @GET
	    @Produces(MediaType.APPLICATION_JSON)
		public Poem Analyze(@PathParam("poem") String poemLines) {
			Poem poem = null;
			
			
			//poemLines = readFile(inputTF.getText());
			if(poemLines != null) {
				var poemList = poemLines.split("\\n");
				ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(poemList));
				StructuralAnalysis structural = new StructuralAnalysis(arrayList);		//initiate structural analysis of poem
				poem = structural.getPoem();
										//record time taken
				
				
			}
			return poem;
			
		}

}
