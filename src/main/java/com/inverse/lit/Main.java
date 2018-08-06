package com.inverse.lit;

import java.util.ArrayList;
import java.util.Arrays;
import com.inverse.lit.*;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "processPoem" path)
 */
@Path("poem")
public class Main {
	//Main Class of Poetry Analyzer
	//GUI and Central Control
	//by: Alexander Dyall, Alex Gitteau, & Sydney Von Arx
	//23 July 2018
	
	@GET
	    @Produces(MediaType.APPLICATION_JSON)
		public Poem Analyze(@QueryParam("text") String poemLines) {

			Poem poem = null;
		System.out.println(poemLines);
			
			//poemLines = readFile(inputTF.getText());
			if(poemLines != null) {
				String[] poemList = poemLines.split("\n");
				System.out.println(poemList[0]);
				ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(poemList));
				StructuralAnalysis structural = new StructuralAnalysis(arrayList);		//initiate structural analysis of poem
				System.out.println(structural.getPoem().getLines()[1].getText());
				poem = structural.getPoem();
				System.out.println(poem.getLines()[0].getText());
			}
			return poem;
			
		}

}
