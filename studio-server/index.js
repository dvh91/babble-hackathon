var express = require('express');
var bodyParser = require('body-parser')
var app = express();
var http = require('http');
var fs = require('fs');
var request = require('request');

app.use(bodyParser.json())

app.post('/audio/alignment', function (req, res) {
    console.log(req.body);
    var downloadUrl = req.body.downloadUrl;
    var start = req.body.start;
    var end = req.body.end;

    request
        .get(downloadUrl)
        .on('error', function(err) {
            console.log(err);
        })
        .pipe(fs.createWriteStream('temp.mp3'));

    res.send(`yo`);
});
 
app.listen(3030);