import { Component, ViewEncapsulation, OnInit, ViewChild, Input, ChangeDetectorRef } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { KalturaServerClient, KalturaMultiRequest, KalturaHttpConfiguration } from '@kaltura-ng2/kaltura-api/dist';

import { UserLoginAction } from '@kaltura-ng2/kaltura-api/dist/services/user';
import { CaptionAssetListAction, CaptionAssetServeAction } from '@kaltura-ng2/kaltura-api/dist/services/caption-asset';
import { MediaGetAction } from '@kaltura-ng2/kaltura-api/dist/services/media';
import { FlavorAssetListAction, FlavorAssetGetUrlAction } from '@kaltura-ng2/kaltura-api/dist/services/flavor-asset';
import { KalturaAssetFilter, KalturaMediaEntry } from '@kaltura-ng2/kaltura-api/dist/kaltura-types';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

declare var kWidget: any;

interface Word { id: number; word: string; start?: number; end?: number; }
interface Selection { words: Array<Word>; line: number; character: number; }
interface Character { id: number; name: string; thumb: string; }
interface Language { language: any; languageCode: any; isDefault: any; }

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {
  @ViewChild('popover') popover: any;

  private assetId: string;
  public babbles: Array<Selection>;
  public characters: Array<Character>;
  public transcript: Array<any>;
  public currentTime: number;
  public selection: Selection;
  public media: KalturaMediaEntry;
  public languages: Array<Language>;
  private downloadUrl: string;
  private kdp: any;
  private selectionStartXPosition: number;
  private selectionStartYPosition: number;
  private selectionEndXPosition: number;
  private selectionEndYPosition: number;
  private popoverActiveState: boolean;

  constructor(
    private http: Http,
    private ref: ChangeDetectorRef,
    private kalturaClient: KalturaServerClient,
    private httpConfiguration: KalturaHttpConfiguration) {

    this.assetId = '1_pc8wuugd';
    this.babbles = [];
    this.characters = [];
    this.transcript = [];
    this.languages = [];
    this.currentTime = 0;
    this.selection = this.setInitialSelctionValue();
    this.popoverActiveState = false;
  }

  ngOnInit() {
    this.embedPlayer();
    this.login();
    this.getCharacters();
  }

  getCharacters() {
    let character: Character = {
      id: 1,
      name: 'Pumba',
      thumb: 'http://vignette2.wikia.nocookie.net/lionking/images/0/02/Pumbaa_icons_by_shwz-d3fy8sf.png/revision/latest?cb=20120808023708'
    }
    for(let i = 0; i < 4; i++) {
      this.characters.push(character)
    };
  }

  login(): void {
    const request = new UserLoginAction({
      partnerId: this.httpConfiguration.partnerId,
      userId: 'USERNAME',
      password: 'PASSWORD'
    });
    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.httpConfiguration.ks = res.result;

        this.getEntryCaptions();
        this.getMediaInfo(this.assetId);
        this.getDownloadUrl();
      });
  }

  seekTo(startTime) {
    let time = this.srtTimeToSeconds(startTime);
    this.kdp.sendNotification('doSeek', time);
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
            language: captionAsset.language.toString(),
            languageCode: captionAsset.languageCode.toString(),
            isDefault: captionAsset.isDefault
          }
        });
        res.result.objects.forEach(captionAsset => {
          this.getCaptionContents(captionAsset.id);
        })
      });
  }

  getCaptionContents(captionAssetId) {
    const request = new CaptionAssetServeAction({
      captionAssetId: captionAssetId
    });

    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.transcript = this.formatSRT(res.result);
      });
  }

  getMediaInfo(entryId) {
    const request = new MediaGetAction({
      entryId: entryId
    });
    this.kalturaClient.request(request)
      .subscribe((res) => {
        this.media = res.result;
      });
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
      }
    );
  }

  embedPlayer() {
    kWidget.embed({
      'targetId': 'kaltura-player',
      'wid': '_1914121',
      'uiconf_id': 37800171,
      'flashvars': {
        'autoPlay': true
      },
      'entry_id': this.assetId
    });

    kWidget.addReadyCallback((playerId) => {
      this.kdp = document.getElementById(playerId);

      this.kdp.kBind('playerUpdatePlayhead', (currentTime) => {
        this.updateCurrentTime(currentTime);
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
    this.getWordsTimes(lineTimes[0], lineTimes[1]);
      // .subscribe(res => {
        // #TODO: map response to words objects.
      // })
    this.popoverActiveState = true;
    this.positionPopover();
  }

  // #TODO: nodejs server: get audio for selected times. 
  getWordsTimes(start: string, end: string) {
    console.log(start);
    console.log(end);
    console.log(this.downloadUrl);
  }

  getCaptionLineTimes() {
    let selectionLine = this.selection.line;
    let lineObj = this.transcript.filter(line => line.id === selectionLine)[0];

    return [lineObj.startTime, lineObj.endTime];
  }

  getSelectionSentence() {
    let words = this.selection.words;
    return words.map(wordObj => wordObj.word).join(' ');
  }

  cancelPopover() {
    this.popoverActiveState = false;
    this.resetSelection();
  }

  addBabble() {
    let babble: Selection = {
      words: this.selection.words,
      line: this.selection.line,
      character: 0
    };
    this.babbles.push(babble);
    this.resetSelection();
    this.popoverActiveState = false;
  }

  popoverActive(): boolean {
    return this.popoverActiveState;
  }

  isBabbleSelectedOnWord(selection: Selection, wordId, lineId): boolean {
    return (selection.words.filter(wordObj => wordObj.id === wordId).length > 0) && (selection.line === lineId);
  }

  isBabbleActiveOnWord(wordId, lineId): boolean {
    return this.babbles.filter(selection => {
      return this.isBabbleSelectedOnWord(selection, wordId, lineId);
    }).length > 0
  }

  private setInitialSelctionValue() {
    let selection: Selection = {
      words: [],
      line: -1,
      character: -1
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
    let lines = text.split('\n');
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
