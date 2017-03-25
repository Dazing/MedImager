import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image } from '../model/image';
import { PopupNavArgs } from '../model/popupNavArgs';
import { Server } from '../model/server';



@Injectable()
export class PopupService{
	private headers = new Headers({'Content-Type': 'application/json'});
	popup:Observable<JSON>;
	private privPopup: Subject<JSON>;
	searchResult:Observable<PopupNavArgs>;
	private privSearchResult: Subject<PopupNavArgs>;

	constructor(private http: Http, private router: Router, ) {
		this.privPopup = new Subject<JSON>();
        this.popup = this.privPopup.asObservable();
		this.privSearchResult = new Subject<PopupNavArgs>();
        this.searchResult = this.privSearchResult.asObservable();

	}

	ngOnInit(): void {
		
	}

	setNextImage(examinationIndex: number, imageIndex: number): void {
		let arg = new PopupNavArgs();
		arg.direction = 1;
		arg.examinationIndex = examinationIndex;
		arg.imageIndex = imageIndex;
		console.log("navigating with arg: " + arg.examinationIndex + ", " + arg.imageIndex);
		
		this.privSearchResult.next(arg);
	}
	
	setPopup(examination: JSON, imageIndex: number):void {
		examination['imageIndex'] = imageIndex;
		this.privPopup.next(examination);
	}

	setPopupWithSearchIndex(examination: JSON, imageIndex: number, searchIndex: number):void {
		examination['imageIndex'] = imageIndex;
		examination['searchIndex'] = searchIndex;

		this.privPopup.next(examination);
	}



}
