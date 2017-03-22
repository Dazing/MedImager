import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image } from '../model/image';

import { Server } from '../model/server';



@Injectable()
export class SearchService{
	private headers = new Headers({'Content-Type': 'application/json'});
	images:Observable<string[]>;
	private privImages: Subject<string[]>;

	constructor(private http: Http) {
		this.privImages = new Subject<string[]>();
        this.images = this.privImages.asObservable();
	}


	getSearch(query:JSON): void {
		var str = "";

		for (var key in query) {
			if (query.hasOwnProperty(key) && query[key].toString() != "") {
				str += "&"+key.toString()+"="+query[key].toString();
			}
		}
		if (str.charAt(0) === "&"){
			str = str.substr(1);
		}	

		var url = ('http://localhost:8080/ExaminationServer/examData/api/search?'+str);
		console.log("Q: "+str);
		
		this.http.get(url)
			.toPromise()
			.then(response => {
				this.privImages.next(response.json());
				
				console.log(this.images);
			})
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
