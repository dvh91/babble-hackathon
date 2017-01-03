import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {
  KalturaApiModule,
  KalturaHttpConfiguration,
  KalturaHttpPostClient,
  KalturaServerClient
} from '@kaltura-ng2/kaltura-api/dist';

import { AppComponent } from './app.component';
import { TranscriptPipe } from './filters/transcript-filter';

@NgModule({
  declarations: [
    AppComponent,
    TranscriptPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    KalturaApiModule
  ],
  providers: [
    { provide: KalturaServerClient, useClass: KalturaHttpPostClient },
    KalturaHttpConfiguration
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(private httpConfiguration: KalturaHttpConfiguration){
    // use the http configuration to setup the url to kaltura server
    this.httpConfiguration.endpointUrl = 'https://www.kaltura.com/api_v3/';
    this.httpConfiguration.clientTag = 'babble-studio';
    this.httpConfiguration.partnerId = 1914121;
  }
}
