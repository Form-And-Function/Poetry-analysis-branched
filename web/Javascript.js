$(document).ready(function() {

	var devices;
	var Poem;

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
		Poem = poem;
		devices = Object.entries(poem.deviceList);
        var output = $('<div></div>');

		var poemNode = poem.lines.map(function(line){
		    var lineList =  [];
		    console.log(line);
		  line.words.forEach( word=>lineList.push(word.text));
		    console.log(lineList);
            return lineList;
        });
		console.log(poemNode);
		addDevices(poemNode);


		var output = poemNode.map( function (line) {
		    console.log(line);
            var item = $('<p></p>');
		    line.forEach(word=>item.append(word));

            return item;
        });
        $('#output').append(output);

        addDeviceSelection();
	}


	function addDeviceSelection() {
	    devices.forEach(function([name, value], typeNum){
            if (value != null){
                var element = $('<button>');
                element.click(showDevices, typeNum);
                element.html(name);
                $(body).append(element);
            }
        });

    }

    function showDevices(typeNum) {
$('.device').removeClass('.currentDisplay');
        $.each(devices[1][typeNum], function (i, instance) {
            $.each(instance[0], function(j, line,){
                $.each(this[1], function(k, word){
                    $('#d'+line+'-'+word).addClass('currentDisplay');
                });
            });
        });
    }

function addDevices(lines){
	    console.log(devices);
	    devices.forEach(function ([typeName, type], TypeId) {
            console.log(type);
            console.log(typeName);
            if(type!= null) {

                $.each(type, function (InstanceId, device) {
                    console.log(device);

                    if (device != null && typeof device.indices != "undefined") {

                        console.log('hi' + device);
                        var deviceLines = device.indices[0];
                        var deviceWords = device.indices[1];
                        $.each(deviceLines, function (i, lineNum) {//find each line
                            var line = lines[lineNum];
                            console.log('line is ' + line);
                            if(line != null) {


                                $.each(deviceWords, function (j, deviceNum) {
                                    console.log(this + " and j is " + j);
                                    var word = line[deviceNum];
                                    var span = $('<span class=".device" id="d' + lineNum + '-' + deviceNum + '"></span>');
                                    if ((typeof word) == "string") {
                                        var typeData = new Set([[TypeId, InstanceId]]);
                                        word = span.html(word);
                                        span.data("deviceIds", typeData)//TODO
                                            .tooltipster(word.data); //make sure this evaluates dynamically!
                                    }
                                    var data = word.data("deviceIds");
                                    data.add([TypeId, InstanceId]);

                                    word.data("deviceIds", data);
                                })
                            }

                        });

                    }

                });
            }
        });
}

$('.device').hover(showSharedDevices, hideSharedDevices);

	function showSharedDevices(){

	    var ids = this.data(deviceIds);
	    var colors = getColors(ids.length());


	    ids.each(function (index, id) {
            var deviceData = devices[id[0]][id[1]];
                devices[id].indices[0].each(function (index, lineNum) {
                    devices[id].indices[1].each(function () {

                            var word = $('#d'+lineNum+'-'+this);
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
            colors.push('hsl('+seperation*num+'70%, 70%)');
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
            console.log('hi');
            var displayedContent = "";
            devices.each(function(){
                displayedContent += 'Device: '+this.Name+
                    '\nStrength: '+this.Intensity;
            });
            return displayedContent;
        }
    });
});