//
//  ViewController.swift
//  Babble
//
//  Created by Rivka Peleg on 01/01/2017.
//  Copyright © 2017 Rivka Peleg. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        let recomendationEngine = RecommendationEngine().generate(sourceFileName: "en", destinationFileName: "fr")
        print(recomendationEngine)
        
        

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}




//guard let path = Bundle.main.path(forResource: "sample", ofType: "srt") else {
//    return
//}
//do {
//    var error: NSError?
//    let content = try String(contentsOfFile: path, encoding: String.Encoding.utf8)
//    let parser = SDSRTParser()
//    parser.load(from: content, error: &error)
//}
//catch{
//    
//    print(error)
//}

//let string = srtGenerator.srtString(subtitle: parser.subtitles as! [SDSubtitle], shift: 20)
//
//let file = "file.srt" //this is the file. we will write to and read from it
//
//if let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
//    
//    let path = dir.appendingPathComponent(file)
//    
//    //writing
//    do {
//        try string.write(to: path, atomically: false, encoding: String.Encoding.utf8)
//    }
//    catch {/* error handling here */}
//    
//    //reading
//    do {
//        let text2 = try String(contentsOf: path, encoding: String.Encoding.utf8)
//        print(text2)
//    }
//    catch {/* error handling here */}
//}
