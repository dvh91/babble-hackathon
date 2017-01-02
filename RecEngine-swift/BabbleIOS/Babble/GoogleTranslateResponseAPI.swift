//
//  GoogleTranslateResponseAPI.swift
//  Babble
//
//  Created by Rivka Peleg on 01/01/2017.
//  Copyright © 2017 Rivka Peleg. All rights reserved.
//

import UIKit

class GoogleTranslateResponseAPI: NSObject {

    
    var translationArray: [String] = [String]()
    
    init(dict:Any) {
        
        super.init()
        if let response = dict as? [String:Any],
            let data =  response["data"] as? [String:Any],
        let translations = data["translations"] as? [[String:String]]
            {
                translations.forEach({ (translate:[String : String]) in
                    
                    if let word = translate["translatedText"] {
                        self.translationArray.append(word)
                    }
                })
                
            
        }
    }
}
