$(document).ready(function () {

    var devices;
    var Poem;
    var key = $('#key');
    const stressed = $('<div class="stressMark">/</div>');
    const unstressed = $('<div class="stressMark">x</div>');
    const footer = $('.footer');
    const bufferSpace = $('.bufferSpace');
    var $loading = $('.loader');

    function fixHeights() {
        bufferSpace.height(footer.height());
    }

    function submit() {
        var txt = $("#inputBox").val();
        $.ajax("rest/poem", {data: {text: txt}}).done(processInput);

        $('.pg1').hide();
        $('.pg2').show();
    }

    var submitButton = document.getElementById("submitButton");
    submitButton.onclick = submit;

    function processInput(poem) {

        console.log(poem);
        Poem = poem;
        devices = Object.entries(poem.deviceList);


        var poemNode = poem.lines.map(function (line) {
            var lineList = [];
            console.log(line);
            line.words.forEach(word => lineList.push(word));
            console.log(lineList);
            return (lineList);
        });
        console.log(poemNode);
        addDevices(poemNode);

        var maxRhymeIndex = Math.max.apply(null, poem.rhymeScheme);
        var colors = getColors(Math.max(maxRhymeIndex));
        console.log(colors);

        var output = poemNode.map(function (line, i) {


            console.log(line);
            var item = $('<div class="line"></div>');
            var colorNum = poem.rhymeScheme[i] - 1;
            if (colorNum >= 0) {
                item.css('border-right-color', colors[colorNum]);
            }


            line.forEach(function (wordObj) {

                if (wordObj.element) {
                    item.append(wordObj.element);
                }
                else {
                    var word = makeWordElement(wordObj);
                    item.append(word);

                }
            });

            return item;
        });
        $loading.hide();
        $('#output').append(output);

        addDeviceSelection();
        $('.device').mouseenter(showSharedDevices);
        updateBold();
        fixHeights();
    }


    function addDeviceSelection() {
        devices.forEach(function ([name, value]) {
            if (value != null && name != 'allDevices') {

                var slider = $('<input type="range" max="100" value="50"  id="' + name + '">').addClass('slider')
                    .change(function () {
                        updateBold();
                    });
                var displayName = fromCamel(name);
                var count = $('<span class="count">').text('(' + value.length + ')');
                var label = $('<div>').addClass('sliderLabel').text(displayName).append(count, slider);
                console.log(value);


                $('.btnBar').append(label);

            }
        });


    }

    function updateBold() {
        $('.device').removeClass('currentDisplay');
        devices.forEach(([typeName, deviceInstances]) => {
            console.log(typeName);
            var sliderVal = $('#' + typeName).val();
            if (deviceInstances != null) {
                deviceInstances.forEach((device) => {

                    if (device != null && typeof device.indices != "undefined") {
                        var intensity = device.intensity;
                        console.log(intensity);
                        console.log(sliderVal);
                        if ((sliderVal > 0) && ((intensity) || intensity >= sliderVal)) {
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

    function makeWordElement(word) {
        console.log(word);
        console.log(unstressed);
        var element = $('<div class="wordWrapper">').append('<span>' + word.originalText + '</span>');
        var scansion = $('<span>').addClass('scansion');
        if (word.stressBool) {
            word.stressBool.forEach(stress => {
                console.log(stress);
                if (stress) {
                    scansion.append(stressed.clone());
                }
                else {
                    scansion.append(unstressed.clone());
                }


            });
        }

        element.append(scansion);
        return element;
    }

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

                                if (!word.element) {
                                    var typeData = [[TypeId, InstanceId]];
                                    var elem = makeWordElement(word)
                                        .addClass('device')
                                        .attr('id', 'd' + lineNum + '-' + deviceNum)
                                        .data("deviceIds", typeData)
                                        .data("tempcolor", new Set()); //make sure this evaluates dynamically!

                                    line[deviceNum].element = elem;
                                }
                                else {
                                    var data = word.element.data("deviceIds");
                                    data.push([TypeId, InstanceId]);

                                    word.element.data("deviceIds", data);
                                }

                            }

                        });

                    }

                });
            }
        });


    }


    function showSharedDevices() {

        if (!$(this).hasClass('currentDisplay')) {
            return;
        }
        hideSharedDevices();
        var ids = $(this).data("deviceIds");
        var colors = getColors(ids.length);
        console.log(ids);
        ids.forEach(function (id, i) {
            console.log(id);
            var deviceFullData = devices[id[0]];
            var deviceName = fromCamel(deviceFullData[0]);
            var deviceInstance = deviceFullData[1][id[1]];
            var devColor = colors[i]
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
                    if (word.hasClass('currentDisplay')) {
                        var currentData = word.data('tempcolor');
                        currentData.add(i);
                        word.data('tempcolor', currentData);
                        color(word, colors);
                        used.push(elem);
                    }

                }

            });


        });
        $(this).css({'box-shadow': '0 0 0 3px #CCC', 'background': 'none'});
        var scroll = $('body').scrollTop();
        key.css('display', 'flex');
        $('body').scrollTop(scroll);
        fixHeights();
    }

    function hideSharedDevices() {
        $('.device').css({'box-shadow': 'none', 'background': 'none'}).data('tempcolor', new Set());
        $('.deviceName').remove();
        key.hide();
        $('.keyContent').remove();
        fixHeights();
    }

    function getColors(num) {
        var separation = 359 / (num + 1);

        var colors = [];
        var numInSet = 8;
        var numSets = Math.ceil(num / numInSet);
        for (var i = 0; i < numSets; i++) {
            for (let j = 0; j < (numInSet); j++) {

                colors.push(getColor(j, i, separation));

            }
        }

        return colors;
    }

    function getColor(colorIdx, brightnessIdx, separation) {
        return 'hsl(' + (separation * colorIdx) + ',70%, ' + (80 - 10 * brightnessIdx) + '%)';
    }

    //color a word's background to display a series of colors. Each color corresponds ro a device instance.
    //allColors is all the colors available. The actual colors are in the data of the element
    function color(word, allColors) {
        const stripeWidth = 8;
        var colors = word.data('tempcolor');
        var value = "repeating-linear-gradient(45deg";
        console.log(colors);
        console.log(allColors);
        console.log("all:   " + colors);
        colors.forEach(function (colorNum, i) {
            var color = allColors[colorNum];
            value += ',' + color + ' ' + i * stripeWidth + 'px,' + color + ' ' + (i + 1) * stripeWidth + 'px';
        });
        value += ')';
        console.log(value);
        console.log(word[0]);
        word[0].style.background = value;
    }

    function fromCamel(str) {
        return str.replace(/([A-Z])/g, ' $1')
            .replace(/^./, function (str) {
                return str.toUpperCase();
            })
    }

});