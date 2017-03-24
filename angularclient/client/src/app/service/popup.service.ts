import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image } from '../model/image';

import { Server } from '../model/server';



@Injectable()
export class PopupService{
	private headers = new Headers({'Content-Type': 'application/json'});
	popup:Observable<JSON>;
	private privPopup: Subject<JSON>;

	constructor(private http: Http, private router: Router, ) {
		this.privPopup = new Subject<JSON>();
        this.popup = this.privPopup.asObservable();
	}

	ngOnInit(): void {
		
	}
	

	setPopup(examination: JSON, imageIndex: number):void {
		examination['imageIndex'] = imageIndex;

		this.privPopup.next(examination);
	}

}
