import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
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

	constructor(private http: Http, private router: Router, ) {
		this.privCollections = new Subject<Collection[]>();
        this.collections = this.privCollections.asObservable();
		
		this.privCollections.next(
			[
				new Collection(0,"Min Samling", {
					"age":["70"],
					"allergy":["Nej"],
					"biopsySite":[],
					"diagDef":["Atrofisk lichen planus"],
					"diagHist":[],
					"diagTent":[],
					"disNow":["Atopiskt eksem"],
					"disPast":["Nej"],
					"drug":["Nej"],
					"examinationID":"980122112626",
					"factorNeg":["Hård föda"],
					"factorPos":["Förbättrad munhygien"],
					"family":["Broder"],
					"gender":["1"],
					"imagePaths":[
						"TestData.mvd\\Pictures\\G0222\\g02223.jpg",
						"TestData.mvd\\Pictures\\G0222\\g02222.jpg",
						"TestData.mvd\\Pictures\\G0222\\g02221.jpg"
					],
					"lesnOn":[],
					"lesnSite":[],
					"skinPbl":["Torr hud","Eksem"],
					"smoke":["Nej"],
					"snuff":["Nej"],
					"symptNow":[],
					"symptSite":[],
					"treatType":["Information"],
					"vasNow":["3.2"],
					"imageId":["2"]
				}),
				new Collection(1,"Guardians of the Mouth", {
					"age":["70"],
					"allergy":["Nej"],
					"biopsySite":[],
					"diagDef":["Atrofisk lichen planus"],
					"diagHist":[],
					"diagTent":[],
					"disNow":["Atopiskt eksem"],
					"disPast":["Nej"],
					"drug":["Nej"],
					"examinationID":"980122112626",
					"factorNeg":["Hård föda"],
					"factorPos":["Förbättrad munhygien"],
					"family":["Broder"],
					"gender":["1"],
					"imagePaths":[
						"TestData.mvd\\Pictures\\G0222\\g02223.jpg",
						"TestData.mvd\\Pictures\\G0222\\g02222.jpg",
						"TestData.mvd\\Pictures\\G0222\\g02221.jpg"
					],
					"lesnOn":[],
					"lesnSite":[],
					"skinPbl":["Torr hud","Eksem"],
					"smoke":["Nej"],
					"snuff":["Nej"],
					"symptNow":[],
					"symptSite":[],
					"treatType":["Information"],
					"vasNow":["3.2"],
					"imageId":["1"]
				}),
				new Collection(2,"Tandväktarna", {
					"age":["70"],
					"allergy":["Nej"],
					"biopsySite":[],
					"diagDef":["Atrofisk lichen planus"],
					"diagHist":[],
					"diagTent":[],
					"disNow":["Atopiskt eksem"],
					"disPast":["Nej"],
					"drug":["Nej"],
					"examinationID":"980122112626",
					"factorNeg":["Hård föda"],
					"factorPos":["Förbättrad munhygien"],
					"family":["Broder"],
					"gender":["1"],
					"imagePaths":[
						"TestData.mvd\\Pictures\\G0222\\g02223.jpg",
						"TestData.mvd\\Pictures\\G0222\\g02222.jpg",
						"TestData.mvd\\Pictures\\G0222\\g02221.jpg"
					],
					"lesnOn":[],
					"lesnSite":[],
					"skinPbl":["Torr hud","Eksem"],
					"smoke":["Nej"],
					"snuff":["Nej"],
					"symptNow":[],
					"symptSite":[],
					"treatType":["Information"],
					"vasNow":["3.2"],
					"imageId":["0"]
				})
			]
		);
		
		
	}

	removeCollection(id: number): void {
		
	}

	addImage(image:any, imageIndex: number, collId: number): void {
		var data = {
			examinationID: image.examinationID,
			imageIndex: imageIndex,
			collId: collId
		}

		var url = ('http://localhost:8080/ExaminationServer/examData/api/collection/'+collId);
		
		this.http.post(url,data)
			.toPromise()
			.then(response => {
				if (response) {
					
				}
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	removeImage(image:any, imageIndex: number, collId: number): void {
		var data = {
			examinationID: image.examinationID,
			imageIndex: imageIndex,
			collId: collId
		}

		var url = ('http://localhost:8080/ExaminationServer/examData/api/collection/'+collId);
		
		this.http.post(url,data)
			.toPromise()
			.then(response => {
				if (response) {
					
				}
			})
			.catch(e => {
				console.log("Get search "+e);
				alert("Server unreachable, try again later!")
				//this.router.navigate(['/serverunreachable']);
			});
	}

	

}