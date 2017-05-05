import { Component, OnInit, Input } from '@angular/core';
import { ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
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
		private server: Server
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

	showNextImage(examinationIndex: number, imageIndex: number): void {
		/*let newImageIndex = (imageIndex + 1) % this.images[examinationIndex].imagePaths.length;
		if (newImageIndex == 0) {
			do {
				examinationIndex = (examinationIndex + 1) % this.images.length;
			} while (this.images[examinationIndex].imagePaths.length < 1)
		}
		this.popupService.setPopupWithSearchIndex(this.images[examinationIndex], newImageIndex, examinationIndex);
		*/
	}

	showPreviousImage(examinationIndex: number, imageIndex: number): void {
		/*let newImageIndex;
		if (imageIndex < 1) {
			do {
				if (examinationIndex <= 0) {
					examinationIndex = this.images.length -1;
				} else {
					examinationIndex -= 1;
				}
			} while (this.images[examinationIndex].imagePaths.length < 1)
			newImageIndex = this.images[examinationIndex].imagePaths.length -1;
		} else {
			newImageIndex = imageIndex - 1;
		}
		this.popupService.setPopupWithSearchIndex(this.images[examinationIndex], newImageIndex, examinationIndex);
		*/
	}

	onImageClick(examinationIndex: number, imageIndex: number):void{
		//this.popupService.setPopupWithSearchIndex(this.images[examinationIndex], imageIndex, examinationIndex);
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

	setNote(i:number, note?: string): void {
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

	showNoteIcon(imageId, show): void {
		if(show)
			this.showNotes[imageId] = 1;
		else
			this.showNotes[imageId] = 0;
		console.log(imageId + ": " + this.showNotes[imageId]);
	}

	getOpacity(imageId): number {
		console.log("hejsan");
		return this.showNotes[imageId];
	}
}
