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

	images:Observable<string[]>;
	private privImages: Subject<string[]>;

	private userID = 1; //TODO: make this use the logged in user id

	constructor(private http: Http, private router: Router, private server: Server) {
		this.privCollections = new Subject<Collection[]>();
        this.collections = this.privCollections.asObservable();
		
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

	getCollection(collectionID: number, callback: (collID: number, images: any[]) => void, thisHandle: any): void {
		var url = (this.server.getUrl() + '/collection/'+collectionID);
		console.log("Request collection: "+url);
		
		this.http.get(url)
			.toPromise()
			.then(response => {
				console.log("collection request http GET response:");
				console.log(response.json());
				let images = [];
				for (let image of response.json()) {
					images.push(image);
					
				}
				console.log("IMAGES:");
				console.log(images);
				
				callback.call(thisHandle, collectionID, images);
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});	
	}

	addImage(examinationID: number, imageIndex: number, collID: number): void {
		var url = (this.server.getUrl() + '/collection/' + collID);
		console.log("Add image to collection, url: "+url);

		let payload = {examinationID: examinationID, index: imageIndex};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers });

		this.http.post(url, JSON.stringify(payload), options)
			.toPromise()
			.then(response => {
				console.log(response);
			})
			.catch(e => {
				console.log("POST error:");
				console.log(e);
				
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	removeImage(image:any, imageIndex: number, collId: number): void {
		/*var str = 'examinationID='+image.examinationID+'&imageIndex='+imageIndex;

		var url = ('http://localhost:8080/ExaminationServer/examData/api/collection/'+collId+'?'+str);
		
		this.http.delete(url)
			.toPromise()
			.then(response => {
				if (response) {
					
				}
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});*/
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
		var url = (this.server.getUrl()  + '/collection/');

		let payload = {collectionID: collId};

		let headers = new Headers({ 'Content-Type': 'application/json'});
  		let options = new RequestOptions({ headers: headers, body: payload });

		this.http.delete(url, options)
			.toPromise()
			.then(response => {
				if (response) {
					this.getCollectionList();
					console.log("delete collection:" + collId);
					
				}
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}
	

}