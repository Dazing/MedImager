import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions, ResponseContentType} from '@angular/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image } from '../model/image';
import { Filter } from '../model/tag';

import { Server } from '../model/server';

@Injectable()
export class SearchService {
	private headers = new Headers({'Content-Type': 'application/json'});
	private imgHeaders = new Headers({'Content-Type': 'image/jpg'});

    searchParameters:Observable<string[]>;
    privSearchParameters: Subject<string[]>;
	searchParameterList:string[];
	searchParameterListReceived = false;

	selectedParameters:Observable<Filter[]>;
    privSelectedParameters: Subject<Filter[]>;
	selectedParamsList: Filter[] = [];

	privFilterDeletion: Subject<Filter>;
	filterDeletion: Observable<Filter>;

	searchTerms: string[];
	
	images:Observable<string[]>;
	private privImages: Subject<string[]>;

	tags:Observable<string[]>;
	private privTags: Subject<string[]>;
	private currentTags: string[] = [];

	constructor(private http: Http, private router: Router, private server: Server) {
		this.privImages = new Subject<string[]>();
        this.images = this.privImages.asObservable();

		this.privSearchParameters = new Subject<string[]>();
        this.searchParameters = this.privSearchParameters.asObservable();

		this.privSelectedParameters = new Subject<Filter[]>();
        this.selectedParameters = this.privSelectedParameters.asObservable();

		this.privFilterDeletion = new Subject<Filter>();
		this.filterDeletion = this.privFilterDeletion.asObservable();

		this.privTags = new Subject<string[]>();
        this.tags = this.privTags.asObservable();

	}

	getSearch(): void {
		let searchParamNames;
		if(this.searchParameterList){ 
			searchParamNames = Object.getOwnPropertyNames(this.searchParameterList);
		}
		let searchParams = this.searchParameterList;

		var str = "";
		for (let filter of this.selectedParamsList) {
			if (str != "") {
				str += "&";
			}
			str += 
				searchParamNames[filter.parameter].replace(/([A-Z])/g, function(part) {return '-'+part.toLowerCase()}).charAt(0).toUpperCase() + 
				searchParamNames[filter.parameter].replace(/([A-Z])/g, function(part) {return '-'+part.toLowerCase()}).slice(1) +
				"=" + searchParams[searchParamNames[filter.parameter]][filter.value];
			//okay i know this looks complicated, but it changes from like disNow to Dis-now
			//it works, just don't worry about it, okie dokie? 
		}

		var url = (this.server.getUrl() + '/search?'+str);
		
		console.log("URL: "+url);

		// Set authorization header
		let headers = new Headers();
		headers.append('Authorization', sessionStorage.getItem("currentUser"));
		let options = new RequestOptions({ headers: headers });
		
		this.http.get(url, options)
			.toPromise()
			.then(response => {
				this.privImages.next(response.json());
				var responsejson = response.json();
				let count = 0;
				for (let exam of response.json()) {
					count += exam.imagePaths.length;
				}
				console.log("got response with " + count + " images from image search");
				//console.log("Images: "+JSON.stringify(this.images)+", privIm: "+JSON.stringify(this.privImages));
				
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	getThumbnail(id: number, index:number): any{
		console.log("RUnning SS getTHumb");

		let thumbnail = false;
		
		var url = (this.server.getUrl() + '/thumbnail/'+id+'/'+index);
		// Set authorization header
		let headers = new Headers();
		headers.append('Authorization', sessionStorage.getItem("currentUser"));
		headers.append('Content-Type', 'image/jpg');
		
		let options = new RequestOptions({ 
			headers: headers, 
			responseType: ResponseContentType.Blob
		});

		this.http.get(url, options)
			.map(response => {
				return response;
			})
			.catch(e => {
				return e;
			});
	}

	getSearchParameters(): void {
		if (!this.searchParameterListReceived) {
			var url = (this.server.getUrl()+'/initValues');

			// Set authorization header
			let headers = new Headers();
			headers.append('Authorization', sessionStorage.getItem("currentUser"));
			let options = new RequestOptions({ headers: headers });

			this.http.get(url, options)
				.toPromise()
				.then(response => {
					var responsejson = response.json();
					this.searchParameterList = responsejson;
					this.searchParameterListReceived = true;
					this.privSearchParameters.next(this.searchParameterList);
				})
				.catch(e => {
					this.privSearchParameters.next(null);
				});
		} else {
			this.privSearchParameters.next(this.searchParameterList);
		}
    }

	setSelectedSearchParameters(parameters: Array<number>, values: Array<number>) {
		let params: Filter[] = [];
		for (let i=0; i < parameters.length; i++) {
			if (parameters[i] >= 0 && values[i] >= 0) {
				params.push(new Filter(parameters[i], values[i]));
			}
		}
		this.selectedParamsList = params;
		this.privSelectedParameters.next(this.selectedParamsList);
	}

	getSelectedSearchParameters() {
		this.privSelectedParameters.next(this.selectedParamsList);
	}

	deleteFilter(filter: Filter): void {
		for (let i = 0; i < this.selectedParamsList.length; i++) {
			if (this.selectedParamsList[i].parameter == filter.parameter && this.selectedParamsList[i].value == filter.value) {
				this.selectedParamsList.splice(i, 1);
			}
		}
		this.privFilterDeletion.next(filter);
		this.privSelectedParameters.next(this.selectedParamsList);
		console.log("deleted filter: (" +filter.parameter+ "," +filter.value+ ")");
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
