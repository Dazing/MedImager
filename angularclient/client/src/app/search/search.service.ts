import { Injectable } from '@angular/core';
import { Headers, Http }       from '@angular/http';
import { Observable } from 'rxjs';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image } from './image';



@Injectable()
export class SearchService {
	private headers = new Headers({'Content-Type': 'application/json'});


	constructor(private http: Http) {}

	getSearch(query:string):Promise<any[]> {
		console.log("running getsear");
		
		return this.http.get('http://localhost:3000/api/search')
			.toPromise()
			.then(response => response.json())
			.catch(this.handleError);
	}
	getImage(): Image {
		return new Image();
	}

	getSearchTerms(): string[] {
		return ["tandtroll","tandvärk","tandsten"];
	}

	private handleError(error: any): Promise<any> {
		console.error('An error occurred', error); // for demo purposes only
		return Promise.reject(error.message || error);
	}

}
