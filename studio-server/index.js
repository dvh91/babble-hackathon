var express = require('express');
var bodyParser = require('body-parser')
var app = express();
var http = require('http');
var fs = require('fs');
var request = require('request');
var ffmpeg = require('fluent-ffmpeg');
var spawn = require('child_process').spawn;


var tempFileName = "temp.mp3";
var tempTrimmedFileName = "temp4.mp3";



app.use(bodyParser.json());
app.get('/job/get',function (req,res){
    console.log(req.query);
    var jobId = req.query.jobid;
    var downloadJob = function(){
        var ls2 = spawn('node', ['speechmatics.js', 'download' ,'-j', jobId, '-i' ,'14808' ,'-t' ,'Y2Y0M2NjZjMtNWUyNS00NDYyLWE0YWEtOTUzNTBkOTRhY2Y2']);
        ls2.stdout.on('data',function(data){
            console.log("downloadJob data:"+data.toString());
            res.send(data);
        });
        ls2.on('close', function( code )  {
            console.log("downloadJob child process exited with code:"+code);
        });
    }
    downloadJob();
})
app.get('/audio/alignment', function (req, res) {
    console.log(req.query);
    var downloadUrl = req.query["downloadUrl"];
    var start = req.query.start;
    var duration = req.query.duration;
    var jobId = 1379921;

    var sendToSpeech = function(){
       // downloadJob();return;
        var ls = spawn('node', ['speechmatics.js', 'upload' ,'-f', 'temp4.mp3', '-l', 'en-US', '-i' ,'14808' ,'-t' ,'Y2Y0M2NjZjMtNWUyNS00NDYyLWE0YWEtOTUzNTBkOTRhY2Y2', '-c' ,'http://www.google.com']);
        ls.stdout.on('data', function( data )  {
            console.log("sendToSpeech stdout:" + data );
            jobId = data.toString().match(/[0-9].*/)[0];
            res.send("{jobid:" + jobId +"}");
            console.log(jobId);
        });
        ls.on('close', function( code )  {
            console.log("sendToSpeech child process exited with code:"+code);
            if (jobId > 0){
                //downloadJob();
            }
        });
    };
    var startTrim = function() {
        ffmpeg( tempFileName )
            .setStartTime( start )
            .setDuration( duration )
            .output( tempTrimmedFileName )
            .on( 'end' , function ( err ) {
                if ( !err ) {
                    console.log( 'conversion Done' );
                    sendToSpeech();
                }
            } )
            .on( 'error' , function ( err ) {
                console.log( 'error: ' , +err );
            } ).run();
    };
    request
        .get(downloadUrl)
        .on('error', function(err) {
            console.log(err);
        } )
        .pipe(fs.createWriteStream(tempFileName).on('close', startTrim)
    );

});
 
app.listen(3030);