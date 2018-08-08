$(document).ready(function() {
	$('.tooltip').tooltipster();

	words = [];

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
        poem.lines.each(function () {
            var html = processLine(poem.lines);
            addLine(html);
        });
	}

	function addDevice(device, words, poem){
	    poem.lines.devices.
    }

	function addLine(line) {
	    var output =$("#output");
	    line = "<br>" + line;
	    output.appendChild(line);
	}

	function processLine(line){
	    var lineHTML = "";
	    line.words.each(function () {
            var currentDevices = this.devices.map(i => poem.deviceList.ExtantDevices.i);
            if(currentDevices.length <= 0) {
                lineHTML += line[i].text;
            } else {
                curretDevices.each(function () {
                    var tooltipTxt = 'text="Device: '+this.Name+
                        '\nStrength: '+this.intensity+'"';
                    lineHTML = lineHTML + "<br>" + '<span class = "devices tooltip"'+tooltipTxt+'>' + line[i].text + "</span>"
                });
            }
        });
	    return lineHTML;
	}
});