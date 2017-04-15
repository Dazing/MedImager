import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image, Collection } from '../model/image';

import { Server } from '../model/server';

@Injectable()
export class CollectionService {
	private headers = new Headers({'Content-Type': 'application/json'});

	collections:Observable<Collection[]>;
	private privCollections: Subject<Collection[]>;
	private collectionsList: Collection[];

	sharedCollections:Observable<Collection[]>;
	private privSharedCollections: Subject<Collection[]>;
	private sharedCollectionsList: Collection[];

	images:Observable<string[]>;
	private privImages: Subject<string[]>;

	private userID = 1; //TODO: make this use the logged in user id

	constructor(private http: Http, private router: Router, private server: Server) {
		this.privCollections = new Subject<Collection[]>();
        this.collections = this.privCollections.asObservable();

		this.privSharedCollections = new Subject<Collection[]>();
        this.sharedCollections = this.privSharedCollections.asObservable();
		
		this.privImages = new Subject<string[]>();
        this.images = this.privImages.asObservable();
	}

	getCollectionList(): void {
		var url = (this.server.getUrl() + '/collection/');
		console.log("Fetch collections, url: "+url);
		
		this.http.get(url)
			.toPromise()
			.then(response => {
				this.collectionsList = [];
				for (let col of response.json()) {
					this.collectionsList.push(new Collection(col.collectionID, col.collectionName, col.collectionDescr));
				}
				console.log("collections: ");
				console.log(response.json());
				console.log(this.collectionsList);
				this.privCollections.next(this.collectionsList);
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});

	}

	getCollection(collectionID: number, callback: (collID: number, images: any[]) => void): void {
		var url = (this.server.getUrl() + '/collection/'+collectionID);
		console.log("Request collection: "+url);
		
		this.http.get(url)
			.toPromise()
			.then(response => {
				let images = [];
				for (let image of response.json()) {
					images.push(image);
				}
				callback.call(undefined, collectionID, images);
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});	
	}
	
	getSharedCollectionList(): void {
		var url = (this.server.getUrl() + '/collection/share');
		console.log("Fetch shared collections, url: "+url);
		
		this.http.get(url)
			.toPromise()
			.then(response => {
				this.sharedCollectionsList = [];
				for (let col of response.json()) {
					this.sharedCollectionsList.push(new Collection(col.collectionID, col.collectionName, col.collectionDescr));
				}
				console.log("collections: ");
				console.log(response.json());
				console.log(this.sharedCollectionsList);
				this.privSharedCollections.next(this.sharedCollectionsList);
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});

	}

	getSharedCollection(collectionID: number, callback: (collID: number, images: any[]) => void): void {
		var url = (this.server.getUrl() + '/collection/share/'+collectionID);
		console.log("Request shared collection: "+url);
		
		this.http.get(url)
			.toPromise()
			.then(response => {
				let images = [];
				for (let image of response.json()) {
					images.push(image);
				}
				callback.call(undefined, collectionID, images);
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});	
	}

	addImage(examinationID: number, imageIndex: number, collID: number, callback?:(success: boolean)=>void): void {
		var url = (this.server.getUrl() + '/collection/' + collID);
		console.log("Add image to collection, url: "+url);

		let payload = {examinationID: examinationID, index: imageIndex};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers });

		this.getCollection(collID, (collID, images)=>{
			let imageFound = false;
			//iterate through collection images to avoid adding duplicates
			images.forEach(element => {
				if (element.examinationID == examinationID && element.index == imageIndex) {
					imageFound = true;
				}
			});
			if (!imageFound) {
				//if collection didn't contain image to add, then post it to collection
				this.http.post(url, JSON.stringify(payload), options)
					.toPromise()
					.then(response => {
						console.log("addImage POST response:");
						console.log(response);
						callback.call(undefined, true);
					})
					.catch(e => {
						console.log("POST error:");
						console.log(e);
						alert("Server unreachable, try again later!")
						//this.router.navigate(['/serverunreachable']);
					});
			} else {
				callback.call(undefined, false);
			}
		});

		
	}

	removeImage(collId: number, collectionItemId: number, callback?: ()=>void): void {
		var url = (this.server.getUrl() + '/collection/' + collId);

		let payload = {collectionitemID: collectionItemId};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers, body: payload });
		
		this.http.delete(url, options)
			.toPromise()
			.then(response => {
				console.log("deleted collection item: " + collectionItemId + " from collection: " + collId);
				console.log(response);
				if (callback) {
					callback.call(undefined);
				}
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	setDescription(collID: number, description: string, callback?:()=>void): void {
		var url = (this.server.getUrl() + '/collection/description');
		console.log("attempt to set collection description, url: "+url);

		let payload = {collectionDescr: description, collectionID: collID};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers });

		this.http.put(url, JSON.stringify(payload), options)
			.toPromise()
			.then(response => {
				console.log("setting collection description was successful:");
				console.log(response);
				if (callback) {
					callback.call(undefined);
				}
			})
			.catch(e => {
				console.log("setting collection description error:");
				console.log(e);
				
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	setNote(collID: number, collectionitemID:string, note: string, callback?:()=>void): void {
		var url = (this.server.getUrl() + '/collection/note');
		console.log("attempt to set image note, collectionitemID: "+collectionitemID, ", collection id: "+collID + ", note: " + note);

		let payload = {note: note, collectionID: collID, collectionitemID: collectionitemID};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers });

		this.http.put(url, JSON.stringify(payload), options)
			.toPromise()
			.then(response => {
				console.log("setting collection description was successful:");
				console.log(response);
				if (callback){
					callback.call(undefined);
				}
			})
			.catch(e => {
				console.log("setting collection description error:");
				console.log(e);
				
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	createCollection(name: string, description?: string): void {
		var url = (this.server.getUrl() + '/collection/');
		console.log("Add image to collection, url: "+url);

		let payload = {userID: this.userID, collectionName: name, collectionDescr: (description==undefined ? "" : description)};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers });

		this.http.post(url, JSON.stringify(payload), options)
			.toPromise()
			.then(response => {
				console.log("http POST response:");
				console.log(response);
				this.getCollectionList();
			})
			.catch(e => {
				console.log("POST error:");
				console.log(e);
				
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
		
	}

	removeCollection(collId: number): void {
		console.log("attempt delete collection: " + collId);
		
		var url = (this.server.getUrl()  + '/collection/');

		let payload = {collectionID: collId};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers, body: payload });

		this.http.delete(url, options)
			.toPromise()
			.then(response => {
				if (response) {
					this.getCollectionList();
					console.log("deleted collection:" + collId);
					
				}
			})
			.catch(e => {
				console.log("couldn't delete collection: " + collId);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}
	


}