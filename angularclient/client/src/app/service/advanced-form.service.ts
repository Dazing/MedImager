import { Headers, Http } from '@angular/http';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';
import { Image } from '../model/image';

@Injectable()
export class AdvancedFormService {
    private headers = new Headers({'Content-Type': 'application/json'});
    searchableStuff:Observable<string[]>;

    private privSearchableStuff: Subject<string[]>;

    constructor(private http: Http, private router: Router) {
        this.privSearchableStuff = new Subject<string[]>();
        this.searchableStuff = this.privSearchableStuff.asObservable();
    }

    getSearchableStuff(): void {
        var url = ('http://localhost:4200/assets/json/searchablestuff.json');

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