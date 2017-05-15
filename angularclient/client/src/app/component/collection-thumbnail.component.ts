import { Component, OnInit, Input } from '@angular/core';
import { ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { Server } from '../model/server';
import { Image, Collection } from '../model/image';
import { CollectionService } from '../service/collection.service';
import { PopupService } from '../service/popup.service';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

declare var Materialize: any;

@Component({
	selector: 'collection-thumbnail',
	templateUrl: '../template/collection-thumbnail.component.html'
})
export class CollectionThumbnailComponent implements OnInit {

	private url;

	private loaded = false;
	private collection: Collection;
	private images;

	//note edit helper functions:
	private editMode: boolean = false;
	private deleteConfirmation: boolean = false;
	private deletei: number;
	private editi: number;
	private editText: string;
	private showNotes: Array<number>;

	constructor(
		private collectionService: CollectionService,
		private popupService: PopupService,
		public ref: ChangeDetectorRef, 
		private server: Server,
		private http: Http
	){}

	ngOnInit(): void {
		this.url = this.server.getUrl();

		this.popupService.searchResult.subscribe(image => {
			if (image.direction > 0) {
				this.showNextImage(image.examinationIndex, image.imageIndex);
			} else if (image.direction < 0) {
				this.showPreviousImage(image.examinationIndex, image.imageIndex);
			}
		})
	}

	setCollection(collection: Collection) {
		this.loaded = false;
		this.collection = collection;
		let thisHandle = this; //handle to 'this' because anonymous callback provides its own higher-scope 'this' object
		this.collectionService.getCollection(collection.id, (collID, images) => {
			if (this.collection.id == collID) {
				console.log("collection thumbnail got images:");
				console.log(images);
				thisHandle.images = images;
				thisHandle.loaded = true;
				this.showNotes = new Array<number>(thisHandle.images.length);
				for(var i = 0; i<this.showNotes.length; i++)
					this.showNotes[i] = 1;
			} else {
				console.log("some sort of weird overlapping bug thing probably happened here, you probably don't have to worry");
			}
		});
	}

	deleteImage(index: number): void {
		this.deleteConfirmation = false;
		let thisHandle = this;
		this.collectionService.removeImage(this.collection.id, this.images[index].collectionitemID, () => {
			thisHandle.setCollection(this.collection); //reload collection when item successfully removed
		});
		
	}

	confirmDelete(index: number): void {
		this.deletei = index;
		this.deleteConfirmation = true;
	}

	deleteConfirmed(conf: boolean): void {
		if (conf) {
			this.deleteImage(this.deletei);
		} else {
			this.deleteConfirmation = false;
		}
	}

	showNextImage(examinationIndex: number, imageIndex: number): void {
		console.log('exam index:'+examinationIndex);
		this.onImageClick((examinationIndex + 1) % this.images.length);
	}

	showPreviousImage(examinationIndex: number, imageIndex: number): void {
		console.log('exam index:'+examinationIndex);
		this.onImageClick((examinationIndex - 1) % this.images.length);
	}

	onImageClick(index: number):void{
		console.log('IMAGE CLICK:'+index);
		this.getImage(index, res => {
			console.log('SET IMAGE:');
			console.log(res);
			console.log(index);
			console.log(this.images[index].examinationIndex + '/' + this.images[index].index);
			this.popupService.setPopupWithSearchIndex(res, this.images[index].index, index);
		});
		
	}

	getImage(index: number, callback:(examination)=>void): void {
		// Set authorization header
		let headers = new Headers();
		headers.append('Authorization', sessionStorage.getItem("currentUser"));
		let options = new RequestOptions({ headers: headers });

		this.http.get(
			this.server.getUrl() + '/examination/'+this.images[index].examinationID,
			options
		).toPromise()
		.then(res => {
			callback(res.json());
		})
		.catch(e => {
			console.error(e);
		})
	}

	getDiagDef(index: number): string {
		var diagnoses = "";
		for (let diag of this.images[index].diagDef) { 
			if (diag != "") {
				if (diagnoses != "") {
					diagnoses += ", ";
				}
				diagnoses += diag;
			}
		}
		return diagnoses;
	}

	getDiagTent(index: number): string {
		var diagnoses = "";
		for (let diag of this.images[index].diagTent) { 
			if (diag != "") {
				if (diagnoses != "") {
					diagnoses += ", ";
				}
				diagnoses += diag;
			}
		}
		return diagnoses;
	}

	getDiagHist(index: number): string {
		var diagnoses = "";
		for (let diag of this.images[index].diagHist) { 
			if (diag != "") {
				if (diagnoses != "") {
					diagnoses += ", ";
				}
				diagnoses += diag;
			}
		}
		return diagnoses;
	}

	getAge(index: number): string {
		return this.images[index].age;
	}

	getDiagListAsString(diags: string[]): string {
		var diagnoses = "";
		for (let diag of diags) { 
			if (diag != "") {
				if (diagnoses != "") {
					diagnoses += ", ";
				}
				diagnoses += diag;
			}
		}
		return diagnoses;
	}

	getNote(i:number): string {
		return this.images[i]["note"];
	}

	setNote(i:number, confirmed?:boolean, note?: string): void {
		let noteEdit: any  = document.getElementById('note-edit-' + i);
		if (!note) {
			note = noteEdit.value.replace(/\n\s*\n/g, '\n'); //remove double line breaks and lines containing only spaces
		}
		Materialize.toast("set note text: "+note, 4000, "primary-colour black-text");
		this.collectionService.setNote(this.collection.id, this.images[i].collectionitemID, note, () => {
			this.images[i].note = note;
		});
		this.editMode = false;
	}

	editNote(i:number): void {
		this.editMode = true;
		this.editi = i;
		this.editText = this.images[i]["note"] ? this.images[i]["note"] : "";
	}

	

	getOpacity(imageId): number {
		console.log("hejsan");
		return this.showNotes[imageId];
	}
}
