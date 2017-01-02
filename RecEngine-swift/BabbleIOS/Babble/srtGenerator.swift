//
//  srtGenerator.swift
//  Babble
//
//  Created by Rivka Peleg on 01/01/2017.
//  Copyright © 2017 Rivka Peleg. All rights reserved.
//

import UIKit

class srtGenerator: NSObject {
    
    static func srtString(subtitle:[SDSubtitle], shift: TimeInterval) -> String {
        
        
//        26
//        00:01:43,312 --> 00:01:45,647
//        <i>There's more to see</i>
        
        var output:String = ""
        subtitle.forEach { (subtitle:SDSubtitle) in
            
            var startTime = subtitle.startTime + shift
            var endTime = subtitle.endTime + shift
            
            let shh = (startTime/(60*60)).rounded(FloatingPointRoundingRule.down)
            startTime = startTime - shh*60*60
            let smm = (startTime/60).rounded(FloatingPointRoundingRule.down)
            startTime = startTime - smm*60
            let sss = startTime.rounded(FloatingPointRoundingRule.down)
            startTime = startTime - sss
            let sms = (startTime*1000).rounded()
            
            let ehh = (endTime/(60*60)).rounded(FloatingPointRoundingRule.down)
            endTime = endTime - ehh*60*60
            let emm = (endTime/60).rounded(FloatingPointRoundingRule.down)
            endTime = endTime - emm*60
            let ess = endTime.rounded(FloatingPointRoundingRule.down)
            endTime = endTime - ess
            
            let ems = (endTime*1000).rounded()
            
            let index: String  = String(subtitle.index) + "\n"
            let time =  String(format:  "%02d:%02d:%02d,%03d --> %02d:%02d:%02d,%03d \n",Int(shh),Int(smm),Int(sss),Int(sms),Int(ehh),Int(emm),Int(ess),Int(ems) )
            //"\(shh):\(smm):\(sss),\(sms) --> \(ehh):\(emm):\(ess),\(ems) \n"
            let text : String  = "<i>" + subtitle.content + "<i> \n"
            
            let subtitleString = index + time + text
            output.append(subtitleString)
            //output.append("")
        }
        
        
        print(output)
        return output
        
    }

}
