var fs = require('fs');

fs.readFile('src/app/sample.srt', function(error, data) {  
    if(error)
        throw error;
        
    var text = data.toString();
    var lines = text.split('\n');

    var output = [];
    var buffer = {
        content: []
    };

    lines.forEach(line => {
        if(!buffer.id)
            buffer.id = parseInt(line);
        else if(!buffer.startTime) {
            var range = line.split(' --> ');
            buffer.startTime = range[0];
            buffer.endTime = range[1];
        }
        else if(line !== '') {
            var strippedLine = line.replace(/(<([^>]+)>)/ig,""); // strip from markup tags
            var words = strippedLine.split(' ');
            var line = [];
            words.forEach((word, index) => {
                var wordObj = { id:  index, word: word}
                line.push(wordObj);
            });
            buffer.content.push(line);
        }
        else {
            output.push(buffer);
            buffer = {
                content: []
            };
        }
    });
    
    fs.writeFile('src/app/sample.json', JSON.stringify(output));
});