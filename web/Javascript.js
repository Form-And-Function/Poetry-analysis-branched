$(document).ready(function () {

    var devices;
    let Poem;
    const key = $('#key');
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
                deviceInstances.forEach((deviceInstance) => {

                    if (deviceInstance != null && typeof deviceInstance.indices != "undefined") {
                        var intensity = deviceInstance.intensity;
                        console.log(intensity);
                        console.log(sliderVal);
                        if(sliderVal == 0 || intensity === null)
                        if ((sliderVal > 0) && intensity > (100 - sliderVal)) {
                            deviceInstance.indices.forEach(function (deviceTriplet) {

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
                                        .data({"deviceIds": typeData, "tempcolor": new Set()});

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

    function meetsIntensity(deviceInstance, deviceName) {
        var sliderVal = $('#' + deviceName).val();
        var intensity = deviceInstance.intensity;
        console.log(deviceInstance);
        return ((sliderVal > 0) && ((intensity) || intensity >= 100 - sliderVal));
    }

    function addKey(deviceInfo, colors) {

        deviceInfo.forEach((d, i) => {
            var name = d.name;
            var extra = d.text;
            var devColor = colors[i];
            var text = name;
            if(extra !=null){
                text+='('+extra+')';
            }
            $('<p>').text(text)
                .css('background', devColor)
                .addClass('keyContent')
                .appendTo(key);

        });
        key.css('display', 'flex');

    }

    function showSharedDevices() {

        if (!$(this).hasClass('currentDisplay')) {
            return;
        }
        hideSharedDevices();
        var ids = $(this).data("deviceIds");
        console.log(ids);
        var instancesToAddToKey = [];
        var count = 0;
        ids.forEach(function (id) { //for every instance of a device that the highlighted word has

            var deviceFullData = devices[id[0]];
            var deviceNameOriginal = deviceFullData[0];

            console.log(deviceFullData);
            var deviceName = fromCamel(deviceNameOriginal);
            var deviceInstance = deviceFullData[1][id[1]];
            if (meetsIntensity(deviceInstance, deviceNameOriginal)) {
                instancesToAddToKey.push({name:deviceName, text: deviceInstance.text});
                deviceInstance.indices.forEach(function (index) { //find all words associated with that device instance

                    var lineNum = index[0];
                    var wordNum = index[1];


                        let word = $('#d' + lineNum + '-' + wordNum);
                        if (word.hasClass('currentDisplay')) {
                            console.log(index);

                            let currentData = new Set(word.data('tempcolor'));

                            console.log(currentData);
                            console.log(currentData.size);
                            console.log(count);
                            currentData.add(count);

                            console.log(word);
                            console.log(currentData);
                            word.data('tempcolor', currentData);
                            word.addClass('colored');

                        }

                });
                count++;
            }
        });

        var scroll = $('body').scrollTop();
        var colors = getColors(count);
        colorWords(colors);
        addKey(instancesToAddToKey, colors);
        $('body').scrollTop(scroll);
        fixHeights();
        $(this).css({'box-shadow': '0 0 0 3px #CCC', 'background': 'none'});
    }

    function colorWords(colors) {
        console.log('coloring...');
        $('.colored').each((idx, word) => {
            console.log($(word).data());
            color($(word), colors);
        });
    }

    function hideSharedDevices() {
        $('.device')
            .css({'box-shadow': 'none', 'background': 'none'})
            .data('tempcolor', new Set())
            .removeClass('colored');
        $('.deviceName').remove();
        key.hide();
        $('.keyContent').remove();
        fixHeights();
    }

    function getColors(num) {

        var colors = [];
        var numInSet = 9;
        var separationDefault = 359 / (numInSet + 1);
        var numSets = Math.floor(num / numInSet);
        var numExtra = num % numInSet;
        for (var i = 0; i < numSets; i++) {
            for (let j = 0; j < (numInSet); j++) {
                colors.push(getColor(j, i, separationDefault));
            }
        }
        var separation = 359 / (numExtra + 1);
        for (let j = 0; j < numExtra; j++) {
            colors.push(getColor(j, numSets, separation));
        }
        console.log(colors);
        return colors;
    }

    function getColor(colorIdx, brightnessIdx, separation) {
        return 'hsl(' + (separation * colorIdx) + ',70%, ' + (70 - 10 * brightnessIdx) + '%)';
    }

    //color a word's background to display a series of colors. Each color corresponds ro a device instance.
    //allColors is all the colors available. The actual colors are in the data of the element
    function color(word, allColors) {
        console.log(word);
        const stripeWidth = 8;
        var colors = word.data('tempcolor');
        var value = "repeating-linear-gradient(45deg";
        console.log(colors);
        console.log(allColors);
        console.log("all:   " + colors);
        var i=0;
        colors.forEach(function (colorNum) {
            console.log(i);
            console.log(allColors[colorNum]);
            var color = allColors[colorNum];

            value += ',' + color + ' ' + i * stripeWidth + 'px,' + color + ' ' + (i + 1) * stripeWidth + 'px';
            i++;
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