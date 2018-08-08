$(document).ready(function() {
	$('.tooltip').tooltipster();

    function submit(){
	    var input = document.getElementById("inputBox");
	    var txt = $("#inputBox").val();
		$.ajax("rest/poem",{data: {text: txt}}).done(processInput);

	    input.style.display="none";
	    var button = document.getElementById("submitButton");
	    button.style.display="none";
	}

	var submitButton = document.getElementById("submitButton");
	submitButton.onclick = submit;
	
	function processInput(poem) {
		console.log(poem);
		var poemNode = poem.lines.map(processLine);
		addDevices(poemNode, poem.deviceList);
		var br = $('<br>');
        var output = poemNode.map(line=> line.add(br));
        $('#output').append(output);
	}

	function processLine(line){
	    var nodes = [];
	    var node;
	    line.words.each(function (index, word) {
            var currentDevices = this.devices.map(i => poem.deviceList.ExtantDevices.i);
            if(currentDevices.length <= 0) {
                node = word.text;
            } else {
                var node = $('<span class = "device"></span>')
                    .text(word)
                    .tooltipster()
                    .data(deviceInstance);
                curretDevices.each(function () {
                    //TODO:store less stuff here
                    node.data(deviceInstance, this.toString());
                });
            }
            nodes.add(node);
        });
	    return nodes;
	}



	////////////////tooltips////////////////////////
    $('.device').tooltipster({

        functionInit: function(instance, helper){

            var content = instance.content();

            instance.content(people);
        },
        // this formats the content on the fly when it needs to be displayed but does not modify its value
        functionFormat: function(instance, helper, content){
            var displayedContent = 'Device: '+this.Name+
                '\nStrength: '+this.intensity;
            return displayedContent;
        }
    });
});