$(document).ready(function() {
	$('.tooltip').tooltipster();
	function submit(){
	    var input = document.getElementById("inputBox");
	    var txt = $("#inputBox").val();
		$.ajax("rest/poem",{data: {text: txt}}).done(function(result){
	      $("#output").html(processInput(result));
	    });

	    input.style.display="none";
	    var button = document.getElementById("submitButton");
	    button.style.display="none";
	}

	var submitButton = document.getElementById("submitButton");
	submitButton.onclick = submit;
	
	function processInput(poem) {
		console.log(poem);
	    for(var i = 0; i < poem.length(); i ++) {
	        var html = processLine(poem[i]);
	        addLine(html);
	    }
	}

	function addLine(line) {
	    var output = document.getElementById("output");
	    line = "<br>" + line;
	    output.appendChild(line);
	}

	function processLine(line){
	    var lineHTML = "";
	    for(var i = 0; i < line.length; i ++){ //i is which word we're on
	        var currentDevices = line[i].devices;
	        if(currentDevices.length <= 0) {
	            lineHTML = lineHTML + line[i].text;
	        } else {
	            for (var j = 0; j < currentDevices.length; j ++) { //j is which device we're on
	            }
	            lineHTML = lineHTML + "<br>" + '<span class = "devices tooltip">' + line[i].text + "</span>";            
	        }
	    }
	}
});