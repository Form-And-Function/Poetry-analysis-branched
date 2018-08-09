$(document).ready(function() {

	var devices;
	var intensityThreshold = 0;

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
		devices = poem.deviceList;

        var p = $('<p></p>');
		var poemNode = poem.lines.map(function(line){
		    var lineList = line.words.map(word=>$(word.text));
		    return lineList.wrap(p);
        });
		addDevices(poemNode);
        $('#output').append(output);
	}

function addDevices(lines){
	    devices.each(function (idNum, device) {

	        var deviceLines = devices.indicies[0];
            var deviceWords = devices.indicies[1];
            deviceLines.each(function (i) {//find each line
                var line = lines[this];
                deviceWords.each(function (j) {
                    var word = line[j];
                    const span = $('<span></span>');
                    if(!word.is('span')){
                        word = word.wrap(span).data(idNum)
                            .tooltipster(word.data); //make sure this evaluates dynamically!
                    }
                    var data = word.data.ids;
                    data.add(idNum);
                    word.data(deviceIds, data );
                })
            });
            lines.each();
        })
}

$('.device').hover(showSharedDevices, hideSharedDevices);

	function showSharedDevices(){

	    var ids = this.data(deviceIds);
	    var colors = getColors(ids.length());


	    ids.each(function (index, id) {
            var deviceData = devices[id];
                devices[id].indicies[0].each(function (index) {
                var line = $('#output:nth-child('+this+')');
                devices[id].indicies[0].each(function () {
                    var word = line.eq(this);
                    var currentData = word.data(tempcolor);
                    currentData.add[index];
                    word.data(tempcolor, currentData);
                    color(word, colors);
                });
            });
        });
    }

    function hideSharedDevices(){
	        $('.device').css(background, "none").data(tempColor, null);
    }

    function getColors(num) {
        var seperation = 360/num;
        var colors = [];
        for(let i=0;i<num;i++){
            colors.add('hsl('+seperation*num+'70%, 70%)');
        }
        return colors;
    }

    function color(word, color){
        var colors = word.data(tempColors);
        var value = "repeating-linear-gradient(45deg,";

        for(let i=0; i<colors.length;i++){
            value+=colors[i]+' '+i*5+'px,';
            value+=colors[i]+' '+(i+1)*5+'px,';
        }
        value+=');';
        word.css(background, value);
    }

	////////////////tooltips////////////////////////
    $('.device').tooltipster({
        // this formats the content on the fly when it needs to be displayed but does not modify its value
        functionFormat: function(instance, helper, content){
            var displayedContent = "";
            devices.each(function(){
                displayedContent += 'Device: '+this.Name+
                    '\nStrength: '+this.Intensity;
            });
            return displayedContent;
        }
    });
});