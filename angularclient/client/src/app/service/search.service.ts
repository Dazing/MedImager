import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image } from '../model/image';

import { Server } from '../model/server';

@Injectable()
export class SearchService {
	private headers = new Headers({'Content-Type': 'application/json'});

	searchTerms: string[];
	
	images:Observable<string[]>;
	private privImages: Subject<string[]>;

	tags:Observable<string[]>;
	private privTags: Subject<string[]>;
	private currentTags: string[] = [];

	constructor(private http: Http, private router: Router, private server: Server) {
		this.privImages = new Subject<string[]>();
        this.images = this.privImages.asObservable();

		this.privTags = new Subject<string[]>();
        this.tags = this.privTags.asObservable();

		this.searchTerms = ["planus", "tandtroll", "tandvärk", "tandsten", "tandpetare", "tandkött", "herpes", "tandlös", "blomkål", "tandkossa", "kossan säger mu", "karies", "baktus"];

	}

	getSearch(query:JSON): void {
		var str = "";

		for (var key in query) {
			if (query.hasOwnProperty(key) && query[key] != "") {
				if (key.toString() == "includeTentative") {

				}
				else if (key.toString() == "includeHist") {
					
				}
				else if (key.toString() == "includeDiseasePast") {
					str += "&term=Dis-past";
				}
				else {
					str += "&"+key.toString()+"="+query[key].toString();
				}
			}
		}
		if (str.charAt(0) === "&"){
			str = str.substr(1);
		}	

		var url = (this.server.getUrl() + '/search?'+str);
		console.log("URL: "+url+", Q: "+str);
		
		this.http.get(url)
			.toPromise()
			.then(response => {
				this.privImages.next(response.json());
				var responsejson = response.json();
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	autoComplete(term: string): string[]{
		return;
	}

	getImage(): Image {
		return new Image();
	}

	getSearchTerms(): string[] {
		return ["tandtroll","tandvärk","tandsten"];
	}

	addTag(tagToAdd: string): boolean {
		for (let tag of this.currentTags) {
			if (tag.toLowerCase() == tagToAdd.toLowerCase()) {
				return false;
			}
		}
		this.currentTags.push(tagToAdd);
		this.privTags.next(this.currentTags);
		return true;
	}

	removeTag(tagToRemove: string): boolean {
		for (let i = 0; i < this.currentTags.length;i++) {
			if (this.currentTags[i].toLowerCase() == tagToRemove.toLowerCase()) {
				this.currentTags.splice(i,1);
				this.privTags.next(this.currentTags);
				return true;
			}
		}
		return false;
	}

	private handleError(error: any): Promise<any> {
		console.error('An error occurred', error); // for demo purposes only
		return Promise.reject(error.message || error);
	}

}
