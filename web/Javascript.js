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

        var colors = getColors(Math.max(poem.rhymeScheme));
        console.log(colors);
		var output = poemNode.map( function (line, i) {
		    console.log(line);
            var item = $('<p class="line"></p>');
            item.css('border-right-color', colors[poem.rhymeScheme[i]]);
            var itemRun ="";
		    line.forEach(function (word) {
                if (typeof word == "string") {
                    itemRun += " " + word;
                }
                else {
                    if (itemRun != null){item.append($('<span>' + itemRun + '</span>'));

                        itemRun = null;}
                    item.append(word);

                }
            });
		    if(itemRun != null){
                item.append($('<span>'+itemRun+'</span>'));
            }

            return item;
		     });
        $('#output').append(output);

        addDeviceSelection();
        $('.device').hover(showSharedDevices, hideSharedDevices);
	}


	function addDeviceSelection() {
	    devices.forEach(function([name, value]){
            if (value != null && name != 'allDevices'){
                var element = $('<button></button>');
                console.log(value);
                element.click(function(){
                    showDevices(value);
                });
                element.html(name);
                $('body').append(element);
            }
        });

    }

    function showDevices(instances) {
$('.device').removeClass('currentDisplay');
        console.log('deviceInfo');

console.log(instances);
        instances.forEach(function(instance){
            instance.indices.forEach(function (index) {
                $('#d'+index[0]+'-'+index[1]).addClass('currentDisplay');
            });
        });
    }

function addDevices(lines){
	    console.log(devices);
	    devices.forEach(function ([typeName, type], TypeId) {
            console.log(type);
            console.log(typeName);

            if(type!= null ) {

                $.each(type, function (InstanceId, device) {
                    console.log(device);

                    if (device != null && typeof device.indices != "undefined") {

                        console.log('hi' + device);

                        device.indices.forEach( function (deviceTriplet) {
                           var lineNum = deviceTriplet[0];
                         var deviceNum = deviceTriplet[1];

                            if(deviceTriplet != null) {
                                var line = lines[lineNum];

                                var word = line[deviceNum];

                                console.log(word);

                                    if ((typeof word) == "string") {
                                        var span = $('<span class="device" id="d' + lineNum + '-' + deviceNum + '"></span>');
                                        var typeData = [[TypeId, InstanceId]];
                                        word = span.html(" "+word+" ");
                                        span.data("deviceIds", typeData)
                                            .data("tempcolor", [])
                                            .tooltipster(); //make sure this evaluates dynamically!
                                    }
                                    else{
                                        var data = word.data("deviceIds");
                                        data.push([TypeId, InstanceId]);

                                        word.data("deviceIds", data);
                                    }
                                    line[deviceNum]= word;
                                console.log('the word is');
                                console.log(word);

                            }

                        });

                    }

                });
            }
        });
}



	function showSharedDevices(){
	    var ids = $(this).data("deviceIds");
	    console.log(ids);
	    var colors = getColors(ids.length);
        console.log(colors);
	    ids.forEach(function (id, i) {
	        var deviceFullData = devices[id[0]];
	        var deviceName = deviceFullData[0];
            var deviceInstance = deviceFullData[1][id[1]];

            console.log('instance'+deviceInstance);
            deviceInstance.indices.forEach(function ([lineNum, wordNum], index) {
                var word = $('#d'+lineNum+'-'+wordNum);
                var currentData = word.data('tempcolor');
                console.log(currentData);
                currentData.push(index);
                console.log(currentData);
                word.data('tempcolor', currentData);
                console.log(word.data('tempcolor'));
                color(word, colors);
            });
            var textWrapper = $('<span>'+deviceName+'</span>').append(deviceName).css('color', colors[i]).addClass('deviceName');
            $('body').append(textWrapper);
        });
    }

    function hideSharedDevices(){
	    $('.device').css('background-image', "none").data('tempcolor', []);
        $('.deviceName').remove();
    }

    function getColors(num) {
        var separation = 0;
        if(num){
            var separation = 360/num;
        }
        var colors = [];
        for(let i=0;i<num;i++){
            colors.push('hsl('+separation*i+',70%, 70%)');
        }
        return colors;
    }

    //color a word's background to display a series of colors. Each color corresponds ro a device instance.
    //allColors is all the colors available. The actual colors are in the data of the element
    function color(word, allColors){
        var colors = word.data('tempcolor');
        var value = "repeating-linear-gradient(45deg";
console.log(colors);
        console.log("all:   "+colors);
        colors.forEach(function(colorNum, i ){
            var color = allColors[colorNum];
            value += ',' + color + ' ' + i * 5 + 'px,' + color + ' ' + (i + 1) * 5 + 'px';
        });
        value+=')';
        console.log(value);
        console.log(word[0]);
        word[0].style.background = value;
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