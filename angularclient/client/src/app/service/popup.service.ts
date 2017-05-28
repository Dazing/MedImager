import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

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

	dialogs:Observable<string>;
	private privDialogs: Subject<string>;
	private booleanReturnSubject: Subject<boolean>;


	constructor(private http: Http, private router: Router, ) {
		this.privPopup = new Subject<JSON>();
        this.popup = this.privPopup.asObservable();
		this.privSearchResult = new Subject<PopupNavArgs>();
        this.searchResult = this.privSearchResult.asObservable();
		this.privDialogs = new Subject<string>();
        this.dialogs = this.privDialogs.asObservable();
	}

	ngOnInit(): void {
		
	}

	setNextImage(examinationIndex: number, imageIndex: number): void {
		let arg = new PopupNavArgs();
		arg.direction = 1;
		arg.examinationIndex = examinationIndex;
		arg.imageIndex = imageIndex;
		
		this.privSearchResult.next(arg);
	}

	setPreviousImage(examinationIndex: number, imageIndex: number): void {
		let arg = new PopupNavArgs();
		arg.direction = -1;
		arg.examinationIndex = examinationIndex;
		arg.imageIndex = imageIndex;
		
		this.privSearchResult.next(arg);
	}
	
	setPopup(examination: JSON, imageIndex: number):void {
		examination['imageIndex'] = imageIndex;
		this.privPopup.next(examination);
	}

	setPopupWithSearchIndex(examination: JSON, imageIndex: number, searchIndex: number, callbackPrev?:(examinationIndex:number, imageIndex: number)=>void, callbackNext?:(examinationIndex:number, imageIndex: number)=>void):void {
		examination['imageIndex'] = imageIndex;
		examination['searchIndex'] = searchIndex;
		examination['callbackNext'] = callbackNext;
		examination['callbackPrev'] = callbackPrev;
		this.privPopup.next(examination);
	}

	returnDialog(answer: boolean): void {
		if (this.booleanReturnSubject != undefined) {
			this.booleanReturnSubject.next(answer);
			this.booleanReturnSubject = undefined;
		}
	}

	collectionDeletionDialog(): Observable<boolean> {
		this.privDialogs.next('collectionDeletion');
		this.booleanReturnSubject = new Subject<boolean>();
		return this.booleanReturnSubject.asObservable();
	}


}
