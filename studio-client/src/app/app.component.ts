import { Component, ViewEncapsulation, OnInit, ViewChild, Input, ChangeDetectorRef } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { KalturaServerClient, KalturaMultiRequest, KalturaHttpConfiguration } from '@kaltura-ng2/kaltura-api/dist';
import { BabbleConfig } from '../config/config';

import { UserLoginAction } from '@kaltura-ng2/kaltura-api/dist/services/user';
import { CaptionAssetListAction, CaptionAssetServeAction } from '@kaltura-ng2/kaltura-api/dist/services/caption-asset';
import { MediaGetAction, MediaUpdateAction } from '@kaltura-ng2/kaltura-api/dist/services/media';
import { FlavorAssetListAction, FlavorAssetGetUrlAction } from '@kaltura-ng2/kaltura-api/dist/services/flavor-asset';
import { MetadataListAction } from '@kaltura-ng2/kaltura-api/dist/services/metadata';
import { KalturaAssetFilter, KalturaMediaEntry, KalturaMetadataFilter} from '@kaltura-ng2/kaltura-api/dist/kaltura-types';
import { KalturaLanguageCode } from '@kaltura-ng2/kaltura-api/dist/kaltura-enums';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

declare var kWidget: any;

interface Word { id: number; word: string; start?: number; end?: number; }
interface Selection { words: Array<Word>; line: number; character: number; }
interface Character { id: number; name: string; thumb: string; }
interface Language { language: any; languageCode: any; isDefault: any; }
interface AppState { characters: Array<Character>, babbles: Array<Selection> }

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {
  @ViewChild('popover') popover: any;

  public activeLanguageCode: KalturaLanguageCode;
  public state: AppState;
  public filterState: string;
  public transcript: Array<any>;
  public currentTime: number;
  public selection: Selection;
  public media: KalturaMediaEntry;
  public languages: Array<Language>;
  public initialState: any;
  public wordsAlignment: Array<any>;
  private assetId: string;
  private downloadUrl: string;
  private kdp: any;
  private selectionStartXPosition: number;
  private selectionStartYPosition: number;
  private selectionEndXPosition: number;
  private selectionEndYPosition: number;
  private popoverActiveState: boolean;
  private toast: boolean;

  constructor(
    private http: Http,
    private ref: ChangeDetectorRef,
    private kalturaClient: KalturaServerClient,
    private httpConfiguration: KalturaHttpConfiguration) {

    this.assetId = '1_umer46fd';
    this.filterState = '*';
    this.state = { characters: [], babbles: [] };
    this.transcript = [];
    this.languages = [];
    this.currentTime = 0;
    this.selection = this.setInitialSelctionValue();
    this.popoverActiveState = false;
  }

  ngOnInit() {
    this.embedPlayer();
    this.login();
  }

  login(): void {
    const request = new UserLoginAction({
      partnerId: this.httpConfiguration.partnerId,
      userId: BabbleConfig.username,
      password: BabbleConfig.password
    });
    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.httpConfiguration.ks = res.result;

        this.getEntryCaptions();
        this.getMediaInfo(this.assetId);
        this.getDownloadUrl();
      });
  }

  getActiveLanguage(): string {
    return this.activeLanguageCode ? this.activeLanguageCode.toString() : null;
  }

  activateLanguage(l: Language): void {
    this.activeLanguageCode = l.languageCode;
  }

  seekTo(startTime) {
    let time = this.srtTimeToSeconds(startTime);
    this.kdp.sendNotification('doSeek', time);
    this.kdp.sendNotification('doPlay');
  }

  getEntryCaptions() {
    const request = new CaptionAssetListAction({
      filter: new KalturaAssetFilter().setData(data => {
        data.entryIdEqual = this.assetId;
       })
    });

    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.languages = res.result.objects.map(captionAsset => {
          return {
            language: captionAsset.language,
            languageCode: captionAsset.languageCode,
            isDefault: !!captionAsset.isDefault
          }
        });
        this.activeLanguageCode = this.languages.filter(l => l.isDefault)[0].languageCode;
        res.result.objects.forEach(captionAsset => {
          this.getCaptionContents(captionAsset.id, captionAsset.languageCode);
        })
      });
  }

  getCaptionContents(captionAssetId, languageCode: KalturaLanguageCode) {
    const request = new CaptionAssetServeAction({
      captionAssetId: captionAssetId
    });

    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.transcript[languageCode.toString()] = this.formatSRT(res.result);
      });
  }

  getMediaInfo(entryId) {
    const request = new MediaGetAction({
      entryId: entryId
    });
    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.media = res.result;
        let jobId = '1381090';
        let initialAppState: AppState = JSON.parse(res.result.description);
        this.state = initialAppState;
        this.getTranscriptAlignment(jobId);
      });
  }

  getTranscriptAlignment(jobId: string) {
    let wordsAlignment = [];
    this.http.get(`https://speechmatics.com/user/${BabbleConfig.smUserId}/jobs/${jobId}/alignment?auth_token=${BabbleConfig.smAuthToken}`)
      .map(res => res.text())
      .subscribe(res => {
        let text = res;
        let words = text.split(' ');
        
        words.forEach((w, index) => {
          let regex = /<time=[0-9]*\.[0-9]*>/ig;
          let results = w.match(regex);
          
          let word = w.match(/>(.*)</)[1];
          let startTimeString = results[0].match(/[0-9]*\.[0-9]*/)[0];
          let endTimeString = results[1].match(/[0-9]*\.[0-9]*/)[0];

          let wordObj = {
            word: word,
            start: parseFloat(startTimeString),
            end: parseFloat(endTimeString)
          }
          wordsAlignment.push(wordObj);
        });

        this.wordsAlignment = wordsAlignment;
        })
  }

  getDownloadUrl() {
    let filter = new KalturaAssetFilter();
    filter.entryIdEqual = this.assetId;

    const request = new KalturaMultiRequest(
      new FlavorAssetListAction({
        filter: filter
      }),
      new FlavorAssetGetUrlAction({
        id: '1_a5v02c9v'
      })
    );

    this.kalturaClient.multiRequest(request)
      .subscribe(results => {
        this.downloadUrl = results[1].result;
        console.log(this.downloadUrl);
      }
    );
  }

  embedPlayer() {
    kWidget.embed({
      'targetId': 'kaltura-player',
      'wid': '_1914121',
      'uiconf_id': 37800171,
      'flashvars': {
        'autoPlay': true,
        'EmbedPlayer': {
          'MonitorRate': 50
        }
      },
      'entry_id': this.assetId
    });

    kWidget.addReadyCallback((playerId) => {
      this.kdp = document.getElementById(playerId);

      this.kdp.kBind('playerUpdatePlayhead', (currentTime) => {
        this.updateCurrentTime(currentTime);
      });

      this.kdp.kBind('audioTracksReceived' ,(e,data) => {
        console.log(data);
      });
    });
  }

  updateCurrentTime(currentTime) {
    this.currentTime = currentTime;
    this.ref.detectChanges();
  }

  srtTimeToSeconds(t): number {
    let timeArray = t.split(':');
    let hours = timeArray[0];
    let minutes = timeArray[1];
    let secondsWithMSArray = timeArray[2].split(',');
    let seconds = secondsWithMSArray[0];
    let ms = secondsWithMSArray[1];

    return (hours * 60 * 60) + (minutes * 60) + (seconds * 1);
  }

  srtTimeDisplay(t): string {
    return t.split(',')[0];
  }

  isCurrentlyPlaying(startTime: number, endTime: number) {
    return this.srtTimeToSeconds(startTime) <= this.currentTime && this.srtTimeToSeconds(endTime) > this.currentTime;
  }

  onWordMouseDown(e, wordObj: any, lineId: number) {
    this.resetSelection();
    this.selectionStartXPosition = e.x;
    this.selectionStartYPosition = e.y;
    this.selection.words.push(wordObj);
    this.selection.line = lineId;
  }

  onWordMouseUp(e, wordObj: any, lineId: number, words: any) {
    /** support to word selection in one line only */
    if (lineId !== this.selection.line) {
      this.resetSelection();
      return;
    }

    this.selectionEndXPosition = e.x;
    this.selectionEndYPosition = e.y;
    let firstWordId = this.selection.words[0].id;
    this.selection.words = [];

    /** word selection support to both directions */
    if (wordObj.id > firstWordId) {
      for(let i = firstWordId; i < wordObj.id; i++) {
        this.selection.words.push(words[i]);
      }
      this.selection.words.push(wordObj);
    } else {
      this.selection.words.push(wordObj);
      for(let i = wordObj.id; i < firstWordId; i++) {
        this.selection.words.push(words[i])
      };
    }

    this.showBabblePopover();
  }

  showBabblePopover() {
    let lineTimes = this.getCaptionLineTimes();
    this.getWordsTime(this.selection.words, lineTimes)
    this.popoverActiveState = true;
    this.positionPopover();
  }

  getWordsTime(words: Array<Word>, lineTimes) {
    let lineStartTime = this.srtTimeToSeconds(lineTimes[0]);
    let lineEndTime = this.srtTimeToSeconds(lineTimes[1]);
    words.forEach(wordObj => {
      wordObj.word
      let wordsAlignment = this.wordsAlignment;
      let foundWord = wordsAlignment.filter(w => {
        return (w.start >= (lineStartTime - 0.5)) 
          && (w.end <= (lineEndTime + 0.5)) 
          && w.word.toLowerCase() === wordObj.word.toLowerCase();
      });
      if(foundWord.length > 0) {
        wordObj.start = foundWord[0].start;
        wordObj.end = foundWord[0].end;

        console.log(wordObj);
      }
    });
  }

  getMetadata(): void {
    const request = new MetadataListAction({
      filter: new KalturaMetadataFilter().setData(data => {
        data.objectIdEqual = this.assetId;
       })
    });
    this.kalturaClient.request(request)
      .subscribe((res) => {
        let data = res.result.objects.map(metaData => metaData.xml);
      });
  }

  getCaptionLineTimes(): Array<string> {
    let selectionLine = this.selection.line;
    let lineObj = this.transcript[this.activeLanguageCode.toString()].filter(line => line.id === selectionLine)[0];

    return [lineObj.startTime, lineObj.endTime];
  }

  getSelectionSentence(): string {
    let words = this.selection.words;
    return words.map(wordObj => wordObj.word).join(' ');
  }

  cancelPopover(): void {
    this.popoverActiveState = false;
    this.resetSelection();
  }

  addBabble() {
    let babble: Selection = {
      words: this.selection.words,
      line: this.selection.line,
      character: 1
    };
    this.state.babbles.push(babble);
    this.resetSelection();
    this.popoverActiveState = false;
  }

  popoverActive(): boolean {
    return this.popoverActiveState;
  }

  getCharacter(id) {
    return this.state.characters.filter(c => c.id === id)[0];
  }

  isBabbleSelectedOnWord(selection: Selection, wordId, lineId): boolean {
    return (selection.words.filter(wordObj => wordObj.id === wordId).length > 0) && (selection.line === lineId);
  }

  isBabbleActiveOnWord(wordId, lineId): boolean {
    return this.state.babbles.filter(selection => {
      return this.isBabbleSelectedOnWord(selection, wordId, lineId);
    }).length > 0
  }

  isStartBabbleActiveOnWord(wordId, lineId): boolean {
    return this.isBabbleActiveOnWord(wordId, lineId) && !this.isBabbleActiveOnWord(wordId - 1, lineId);
  }

  playSelectionAudio(languageCode) {
    this.kdp.sendNotification('switchAudioTrack', {index: languageCode });
    this.kdp.sendNotification('doSeek', this.selection.words[0].start);
    this.kdp.kBind('playerUpdatePlayhead.audio', (currentTime) => {
      if(currentTime >= this.selection.words[this.selection.words.length - 1].end + 0.1) {
        this.kdp.sendNotification('doPause');
        this.kdp.kUnbind('.audio')
      }
    });
    this.kdp.sendNotification('doPlay');
  }

  jumpToCurrentLine(): void {
    
  }

  filter(filter) {
    this.filterState = filter;
  }

  publishBabbles() {
    const request = new MediaUpdateAction({ 
      entryId: this.assetId,
      mediaEntry: new KalturaMediaEntry().setData(data => {
        data.description = JSON.stringify(this.state);
      }) 
    });
    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.state = JSON.parse(res.result.description);
        this.showToast();
      });
  }

  showToast() {
    this.toast = true;
    setTimeout(() => this.toast = false, 2000);
  }

  chooseCharacter(id) {
    this.selection.character = parseInt(id);
  }

  private setInitialSelctionValue() {
    let selection: Selection = {
      words: [],
      line: -1,
      character: 1
    };

    return this.selection = selection;
  }

  private resetSelection() {
    this.selection = this.setInitialSelctionValue();
  }

  private positionPopover() {
    let x = Math.abs((this.selectionEndXPosition - this.selectionStartXPosition) / 2);

    this.popover.nativeElement.style.left = `${x + Math.min(this.selectionEndXPosition, this.selectionStartXPosition) - 165}px`;
    this.popover.nativeElement.style.top = `${this.selectionStartYPosition + 15}px`;
  }

  private formatSRT(data) {
    let text = data.toString();
    let lines = text.split(/\r?\n/);
    let response = [];

    let output = [];
    let buffer: any = {
      content: []
    };

    lines.forEach(line => {
        if (!buffer.id) {
          buffer.id = parseInt(line);
        } else if(!buffer.startTime) {
            let range = line.split(' --> ');
            buffer.startTime = range[0];
            buffer.endTime = range[1];
        } else if(line !== '') {
            let strippedLine = line.replace(/(<([^>]+)>)/ig,""); // strip from markup tags
            let words = strippedLine.split(' ');
            let lineObj = [];
            words.forEach((word, index) => {
                let wordObj: Word = { id:  index, word: word}
                lineObj.push(wordObj);
            });
            buffer.content.push(lineObj);
        } else {
            output.push(buffer);
            buffer = {
                content: []
            };
        }
    });

    return output;
  }
}
