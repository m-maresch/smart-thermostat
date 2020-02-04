import {Injectable, OnInit} from '@angular/core';
import {webSocket, WebSocketSubject} from 'rxjs/webSocket';
import {generate, interval, Observable, zip} from 'rxjs';
import {map} from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class ChartDataService {

  private _source: WebSocketSubject<any> = webSocket('ws://localhost:8080/eventEmitter');

  get source(): Observable<any> {
    return zip(this._source.asObservable(), interval(500))
      .pipe(map((item) => item[0]));
  }

  push(obj: any): void {
    this._source.next(obj);
  }
}
