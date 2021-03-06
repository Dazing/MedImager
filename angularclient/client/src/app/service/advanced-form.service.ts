import { Headers, Http, RequestOptions} from '@angular/http';
import { Router } from '@angular/router';
import { Injectable, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';
import { Image } from '../model/image';

import { Server } from '../model/server';

@Injectable()
export class AdvancedFormService {
    headers = new Headers({'Content-Type': 'application/json'});
    searchableStuff:Observable<string[]>;

    privSearchableStuff: Subject<string[]>;

    constructor(private http: Http, private router: Router, private server: Server) {
        this.privSearchableStuff = new Subject<string[]>();
        this.searchableStuff = this.privSearchableStuff.asObservable();
    }

    getSearchableStuff(): void {
        var url = (this.server.getUrl()+'/initValues');
        let headers = new Headers({ 'Content-Type': 'application/json'});
		headers.append('Authorization', sessionStorage.getItem("currentUser"));
		
  		let options = new RequestOptions({ headers: headers });
        this.http.get(url)
			.toPromise()
			.then(response => {
                   var responsejson = response.json();
                   this.privSearchableStuff.next(responsejson);
                   console.log(responsejson);
			})
			.catch(e => {
                this.privSearchableStuff.next(null);
			});
    }
}