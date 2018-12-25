$(document).ready(function () {

    var devices;
    var Poem;
    var key = $('#key');

    function submit() {
        var input = document.getElementById("inputBox");
        var txt = $("#inputBox").val();
        $.ajax("rest/poem", {data: {text: txt}}).done(processInput);

        input.style.display = "none";
        var button = document.getElementById("submitButton");
        button.style.display = "none";
    }

    var submitButton = document.getElementById("submitButton");
    submitButton.onclick = submit;

    function processInput(poem) {
        console.log(poem);
        Poem = poem;
        devices = Object.entries(poem.deviceList);

        var output = $('<div></div>');

        var poemNode = poem.lines.map(function (line) {
            var lineList = [];
            console.log(line);
            line.words.forEach(word => lineList.push(word.text));
            console.log(lineList);
            return lineList;
        });
        console.log(poemNode);
        addDevices(poemNode);

        var maxRhymeIndex = Math.max.apply(null, poem.rhymeScheme);
        var colors = getColors(Math.max(maxRhymeIndex));
        console.log(colors);

        var output = poemNode.map(function (line, i) {
            console.log(line);
            var item = $('<p class="line"></p>');
            item.css('border-right-color', colors[poem.rhymeScheme[i] - 1]);
            var itemRun = "";
            line.forEach(function (word) {
                console.log(word);
                if (typeof word == "string") {
                    console.log(word);
                    itemRun += " " + word;
                }
                else {
                    if (itemRun != null) {
                        console.log(itemRun);
                        item.append($('<span>' + itemRun + '</span>'));
                        itemRun = "";
                    }
                    item.append(word);

                }
            });
            if (itemRun != null) {
                item.append($('<span>' + itemRun + '</span>'));
            }

            return item;
        });
        $('#output').append(output);

        addDeviceSelection();
        $('.device').hover(showSharedDevices, hideSharedDevices);
        updateBold();
    }


    function addDeviceSelection() {
        devices.forEach(function ([name, value]) {
            if (value != null && name != 'allDevices') {

                var slider = $('<input type="range" max="100" value="50"  id="' + name + '">').addClass('slider')
                    .change(function () {
                        updateBold();
                    });
                var label = $('<div>').addClass('sliderLabel').text(name).append(slider);
                console.log(value);


                $('.btnBar').append(label);

            }
        });


    }

    function updateBold() {
        $('.device').removeClass('currentDisplay');
        devices.forEach(([typeName, deviceInstances]) => {
            console.log(typeName);
            var sliderVal = $('#'+typeName).val();
            if(deviceInstances != null) {
                deviceInstances.forEach((device) => {

                    if (device != null && typeof device.indices != "undefined") {
                        var intensity = device.intensity;
                        console.log(intensity);
                        console.log(sliderVal);
                        if ((sliderVal>0) && ((intensity) || intensity >= sliderVal)) {
                            device.indices.forEach(function (deviceTriplet) {

                                console.log(deviceTriplet);
                                var lineNum = deviceTriplet[0];
                                var deviceNum = deviceTriplet[1];

                                    $('#d' + lineNum + '-' + deviceNum).addClass('currentDisplay');

                            });
                        }
                    }

                });
            }
        });
    };


    function addDevices(lines) {
        console.log(devices);
        devices.forEach(function ([typeName, type], TypeId) {
            console.log(type);
            console.log(typeName);

            if (type != null) {

                $.each(type, function (InstanceId, device) {
                    console.log(device);

                    if (device != null && typeof device.indices != "undefined") {

                        device.indices.forEach(function (deviceTriplet) {
                            var lineNum = deviceTriplet[0];
                            var deviceNum = deviceTriplet[1];

                            if (deviceTriplet != null) {
                                var line = lines[lineNum];

                                var word = line[deviceNum];

                                console.log(word);

                                if ((typeof word) == "string") {
                                    var span = $('<span class="device" id="d' + lineNum + '-' + deviceNum + '"></span>');
                                    var typeData = [[TypeId, InstanceId]];
                                    word = span.html(" " + word + " ");
                                    span.data("deviceIds", typeData)
                                        .data("tempcolor", new Set()); //make sure this evaluates dynamically!
                                }
                                else {
                                    var data = word.data("deviceIds");
                                    data.push([TypeId, InstanceId]);

                                    word.data("deviceIds", data);
                                }
                                line[deviceNum] = word;
                                console.log('the word is');
                                console.log(word);

                            }

                        });

                    }

                });
            }
        });


    }


    function showSharedDevices() {
        if(! $(this).hasClass('currentDisplay')){
            return;
        }
        var ids = $(this).data("deviceIds");
        var colors = getColors(ids.length);
        console.log(ids);
        ids.forEach(function (id, i) {
            console.log(id);
            var deviceFullData = devices[id[0]];
            var deviceName = deviceFullData[0];
            var deviceInstance = deviceFullData[1][id[1]];
            var devColor = colors[i];
            console.log(deviceFullData);
            $('<p>').text(deviceName)
                .css('background', devColor)
                .addClass('keyContent')
                .appendTo(key);
            var used = [];
            deviceInstance.indices.forEach(function (index) {

                var lineNum = index[0];
                var wordNum = index[1];
                var elem = [lineNum, wordNum];

                if (!used.includes(elem)) {//TODO
                    var word = $('#d' + lineNum + '-' + wordNum);
                    if(word.hasClass('currentDisplay')){
                        var currentData = word.data('tempcolor');
                        currentData.add(i);
                        word.data('tempcolor', currentData);
                        color(word, colors);
                        used.push(elem);
                    }

                }

            });


            var textWrapper = $('<span>' + deviceName + '</span>').css('color', colors[i]).addClass('deviceName');
            $('devices').append(textWrapper);
        });
        $(this).css({'border': '5px solid #CCC', 'background': 'none'});
        key.css('display', 'flex');
    }

    function hideSharedDevices() {
        $('.device').css({'border': "none", 'background': 'none'}).data('tempcolor', new Set());
        $('.deviceName').remove();
        key.hide();
        $('.keyContent').remove();
    }

    function getColors(num) {
        var separation = 359 / (num + 1);

        var colors = [];
        for (let i = 0; i < num; i++) { //TODO: if these are too close, change the lightness
            colors.push('hsl(' + (separation * i) + ',70%, 70%)');
            console.log(i);
        }
        console.log(colors);
        return colors;
    }

    //color a word's background to display a series of colors. Each color corresponds ro a device instance.
    //allColors is all the colors available. The actual colors are in the data of the element
    function color(word, allColors) {
        var colors = word.data('tempcolor');
        var value = "repeating-linear-gradient(45deg";
        console.log(colors);
        console.log(allColors);
        console.log("all:   " + colors);
        colors.forEach(function (colorNum, i) {
            var color = allColors[colorNum];
            value += ',' + color + ' ' + i * 5 + 'px,' + color + ' ' + (i + 1) * 5 + 'px';
        });
        value += ')';
        console.log(value);
        console.log(word[0]);
        word[0].style.background = value;
    }


});