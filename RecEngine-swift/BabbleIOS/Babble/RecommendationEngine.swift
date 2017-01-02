//
//  RecommendationEngine.swift
//  Babble
//
//  Created by Rivka Peleg on 01/01/2017.
//  Copyright © 2017 Rivka Peleg. All rights reserved.
//

import UIKit

struct RecomendationEntry  {
    
    var sourceStartTime: TimeInterval
    var sourceEndTime: TimeInterval
    var sourceLanguageText: String
    
    var translatedWord: String
    
    var targetStartTime: TimeInterval
    var targetEndTime: TimeInterval
    var targetLanguageText: String

    
}


struct TranslatedSpeechmaticsEntry  {
    
    var startTime: TimeInterval
    var endTime: TimeInterval
    var word: String
    
    var origin: SpeechmaticsEntry
    
}

struct SpeechmaticsEntry  {
    
    var startTime: TimeInterval
    var endTime: TimeInterval
    var word: String
    
}


struct EntryMapOptions  {
    
    var entry: SpeechmaticsEntry
    var mapOptions: [TranslatedSpeechmaticsEntry]
}




class RecommendationEngine: NSObject {
    
    var translatedOptionsBySourceWord =  [EntryMapOptions]()
    
    let group = DispatchGroup()
    
    func generate(sourceFileName:String, destinationFileName:String) -> [RecomendationEntry]? {
        
        guard let sourcePath = Bundle.main.path(forResource: sourceFileName, ofType: "txt"),
            let targetPath = Bundle.main.path(forResource: destinationFileName, ofType: "txt") else {
                return nil
        }
        do {
            let sourceContent = try String(contentsOfFile: sourcePath, encoding: String.Encoding.utf8)
            let targetContent = try String(contentsOfFile: targetPath, encoding: String.Encoding.utf8)
            
            let sourceSpeechmaticsEntries: [SpeechmaticsEntry] = self.parseSpeechmaticsFile(content: sourceContent)
            let targetSpeechmaticsEntries: [SpeechmaticsEntry] = self.parseSpeechmaticsFile(content: targetContent)
            
            self.loadTranslate(sourceSpeechmaticsEntries: targetSpeechmaticsEntries, completion: { (translatedEntries:[TranslatedSpeechmaticsEntry]) in
                
                sourceSpeechmaticsEntries.forEach({ (entry:SpeechmaticsEntry) in
                    
                    let nearByEntries = self.getNearByEntries(entry: entry, entries: translatedEntries, offset: 6)
                    let option = EntryMapOptions(entry: entry, mapOptions: nearByEntries)
                    self.translatedOptionsBySourceWord.append(option)
                })
                
                let recomendation = self.getRecomendation()
                print(recomendation)
                
             })
            
            
            print(sourceSpeechmaticsEntries)
            print(targetSpeechmaticsEntries)
            
        }
        catch{
        }
        
        return nil
    }
    
    func parseSpeechmaticsFile(content:String) -> [SpeechmaticsEntry] {
         
        let sourceWords = content.components(separatedBy: " ")
        var result = [SpeechmaticsEntry]()
        
        sourceWords.forEach { (text:String) in
            
            let entry: SpeechmaticsEntry? = self.speechmaticsEntry(text: text)
            if let en = entry {
                result.append(en)
            }
        }
        
        return result
    }
    
    func speechmaticsEntry(text:String) -> SpeechmaticsEntry? {
        
        do {
            let regex = try NSRegularExpression(pattern: "<time=[0-9]*.[0-9]*>")
            let nsString = text as NSString
            let results = regex.matches(in: text, range: NSRange(location: 0, length: nsString.length))
            if results.count == 2 {
    
                let startWord = text.index(text.startIndex, offsetBy: results[0].range.location + results[0].range.length)
                let endWord = text.index(text.startIndex, offsetBy: results[1].range.location)
                let word = text[startWord..<endWord]
                
                let ssTime = text.index(text.startIndex, offsetBy: results[0].range.location + 6)
                let esTime = text.index(text.startIndex, offsetBy: results[0].range.location + results[0].range.length - 1)
                let startTimeString = text[ssTime..<esTime]
                
                let seTime = text.index(text.startIndex, offsetBy: results[1].range.location + 6)
                let eeTime = text.index(text.startIndex, offsetBy: results[1].range.location + results[0].range.length - 1)
                let endTimeString = text[seTime..<eeTime]
                
                
                if let startTime = Double(startTimeString),
                    let endTime = Double(endTimeString)
                    {
                  return SpeechmaticsEntry(startTime: startTime, endTime: endTime, word: word)
                }else{
                    return nil
                }

                
                
                
            }else{
                return nil
            }
            
        } catch let error {
            print("invalid regex: \(error.localizedDescription)")
            return nil
        }
        
        
    }
    
    
    
    
    func loadTranslate(sourceSpeechmaticsEntries:[SpeechmaticsEntry], completion:@escaping (_ translatedWords:[TranslatedSpeechmaticsEntry])->Void) -> Void {
        
        
        
        
        let stringsToTranslate = sourceSpeechmaticsEntries.map { (entry:SpeechmaticsEntry) -> String in
            return entry.word
        }
        
        
        var urlArray = [String]()
        
        var requestNumber = stringsToTranslate.count/128
        if stringsToTranslate.count%128 != 0 {
            requestNumber = requestNumber + 1
        }
        
        var wordsCountPerRequest = 128
        var requestIndex = 0
        stringsToTranslate.forEach { (word:String) in
            
            
            if wordsCountPerRequest == 0 {
                wordsCountPerRequest = 128
                requestIndex = requestIndex + 1
            }
            
            if wordsCountPerRequest == 128 {
                urlArray.append("https://translation.googleapis.com/language/translate/v2?key=AIzaSyDlH3tp1UBwQoPLgZy7KIhJxg5tazAAgQE&source=fr&target=en")
            }
            let param = "&q=\(word)".addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)
            if let p = param {
              urlArray[requestIndex].append(p)
            }
            wordsCountPerRequest = wordsCountPerRequest - 1
        }
        
        
        var responses = [[TranslatedSpeechmaticsEntry]]()
        urlArray.forEach { (urlString:String) in
            
            let index = urlArray.index(of: urlString)
            guard let url = URL(string: urlString ) else {
                completion([TranslatedSpeechmaticsEntry]())
                return
            }
            
            let request = URLRequest(url: url)
            
            let session: URLSession!
            
            let configuration = URLSessionConfiguration.default
            configuration.requestCachePolicy = NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData
            session = URLSession(configuration: configuration)
            
            
            self.group.enter()
            var task: URLSessionDataTask? = nil
            // settings headers:
            task = session.dataTask(with: request) { (data, response, error) in
                
                if error == nil, let d = data {
                    
                    do {
                        let jsonObject = try JSONSerialization.jsonObject(with: d, options: JSONSerialization.ReadingOptions())
                        let response = GoogleTranslateResponseAPI(dict: jsonObject)
                        
                        
                        var translatedEntries = [TranslatedSpeechmaticsEntry]()
                        let startIndex = 128*index!
                        let endIndex = min(sourceSpeechmaticsEntries.count, 128*(startIndex + 1))
                        for (word, entry) in zip(response.translationArray , sourceSpeechmaticsEntries[startIndex..<endIndex] ) {
                            
                            let translatedEntry = TranslatedSpeechmaticsEntry(startTime: entry.startTime, endTime: entry.endTime, word: word, origin: entry)
                                //SpeechmaticsEntry(startTime: entry.startTime, endTime: entry.endTime, word: word)
                            translatedEntries.append(translatedEntry)
                        }
                        
                        responses.append(translatedEntries)
                        
                    } catch{
                        
                        
                    }
                    
                    
                }
                
                 self.group.leave()
                
            }
            
            if let tsk = task {
                tsk.resume()
            }

        }
        
        group.notify(queue: DispatchQueue.main) {
            let result = responses.flatMap { $0 }
            completion(result)
        }
    }
    
    
    func getNearByEntries(entry:SpeechmaticsEntry,entries:[TranslatedSpeechmaticsEntry],offset:TimeInterval) -> [TranslatedSpeechmaticsEntry] {
        
        let range = Range<TimeInterval>.init(uncheckedBounds: (lower: entry.startTime - offset, upper: entry.endTime + offset))
        let result = entries.filter { (ent:TranslatedSpeechmaticsEntry) -> Bool in
            
            return range.contains(ent.startTime) && range.contains(ent.endTime)
        }
        return result
        
        
    }
    
    func getRecomendation() -> [RecomendationEntry] {
        
        var recomendations = [RecomendationEntry]()
        self.translatedOptionsBySourceWord.forEach { (entryOptions:EntryMapOptions) in
            let matchesEntries = entryOptions.mapOptions.filter({ (entry:TranslatedSpeechmaticsEntry) -> Bool in
               return entryOptions.entry.word == entry.word
            })
            
            if let match = matchesEntries.last {
               let recomendation = RecomendationEntry(sourceStartTime: entryOptions.entry.startTime,
                                                      sourceEndTime: entryOptions.entry.endTime,
                                                      sourceLanguageText: entryOptions.entry.word,
                                                      translatedWord: match.word,
                                                      targetStartTime: match.origin.startTime,
                                                      targetEndTime: match.origin.endTime,
                                                      targetLanguageText: match.origin.word)
                recomendations.append(recomendation)
            }
        }
        
        return recomendations
    }
    
  
}

//    let parser = SDSRTParser()
//    parser.load(from: content, error: &error)

