import { Pipe, PipeTransform, Injectable } from '@angular/core';

@Pipe({
    name: 'transcript',
    pure: false
})
@Injectable()
export class TranscriptPipe implements PipeTransform {
    transform(items: any[], babbles: any, state: string): any {
        if(!items) return [];
        
        switch (state) {
            case 'babbles':
                return items.filter(line => {
                    var babblesLinesIds = babbles.map(b => b.line);
                    return babblesLinesIds.indexOf(line.id) > -1
                });
            default:
                return items;
        }
    }
}