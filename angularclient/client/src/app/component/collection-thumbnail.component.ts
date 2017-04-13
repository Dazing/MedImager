import { Component, OnInit, Input } from '@angular/core';
import { ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
import { Server } from '../model/server';
import { Image, Collection } from '../model/image';
import { CollectionService } from '../service/collection.service';
import { PopupService } from '../service/popup.service';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';



@Component({
	selector: 'collection-thumbnail',
	templateUrl: '../template/collection-thumbnail.component.html'
})
export class CollectionThumbnailComponent implements OnInit {

	public results;
	public url;

	private resultSubject: Subject<Image[]>;
	private resultObserver: Observable<Image[]>;

	//note edit helper functions:
	public editMode: boolean = false;
	public editi: number;
	public editj: number;
	public editText: string;

	constructor(
		private collectionService: CollectionService,
		private popupService: PopupService,
		public ref: ChangeDetectorRef, 
		private server: Server
	){}

	ngOnInit(): void {
		

		this.popupService.searchResult.subscribe(searchResult => {
			if (searchResult.direction > 0) {
				this.showNextImage(searchResult.examinationIndex, searchResult.imageIndex);
			} else if (searchResult.direction < 0) {
				this.showPreviousImage(searchResult.examinationIndex, searchResult.imageIndex);
			}
		})
	}

	setCollection(collection: Collection) {
		this.resultSubject = new Subject<Image[]>();
		this.resultObserver = this.resultSubject.asObservable();
		this.resultObserver.subscribe(images => {
			this.results = images;
		});
		this.collectionService.getCollection(collection.id, this.resultSubject);
	}

	showNextImage(examinationIndex: number, imageIndex: number): void {
		/*let newImageIndex = (imageIndex + 1) % this.searchresults[examinationIndex].imagePaths.length;
		if (newImageIndex == 0) {
			do {
				examinationIndex = (examinationIndex + 1) % this.searchresults.length;
			} while (this.searchresults[examinationIndex].imagePaths.length < 1)
		}
		this.popupService.setPopupWithSearchIndex(this.searchresults[examinationIndex], newImageIndex, examinationIndex);
		*/
	}

	showPreviousImage(examinationIndex: number, imageIndex: number): void {
		/*let newImageIndex;
		if (imageIndex < 1) {
			do {
				if (examinationIndex <= 0) {
					examinationIndex = this.searchresults.length -1;
				} else {
					examinationIndex -= 1;
				}
			} while (this.searchresults[examinationIndex].imagePaths.length < 1)
			newImageIndex = this.searchresults[examinationIndex].imagePaths.length -1;
		} else {
			newImageIndex = imageIndex - 1;
		}
		this.popupService.setPopupWithSearchIndex(this.searchresults[examinationIndex], newImageIndex, examinationIndex);
		*/
	}

	onImageClick(examinationIndex: number, imageIndex: number):void{
		//this.popupService.setPopupWithSearchIndex(this.searchresults[examinationIndex], imageIndex, examinationIndex);
	}



	getDiagDef(index: number): string {
		var diagnoses = "";
		for (let diag of this.results[index].diagDef) { 
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
		for (let diag of this.results[index].diagTent) { 
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
		for (let diag of this.results[index].diagHist) { 
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
		return this.results[index].age;
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


	editCollectionImageNote(examinationID: number, imageIndex: number, examinationIndex: number): void {
		console.log("examinationID: "+examinationID);
		console.log("imageIndex: "+imageIndex);
		this.results[examinationIndex]["note"] = "hej";
		console.log("note: " + this.results[examinationIndex]["note"]);
		
	}

	getNote(i:number, j: number): string {
		return this.results[i]["note"+j];
	}

	setNote(i:number, j: number, note?: string): void {
		let noteEdit: any  = document.getElementById('note-edit-' + i + '-' + j);
		this.results[i]["note"+j] = noteEdit.value;
		this.editMode = false;
	}

	editNote(i:number,j:number): void {
		this.editMode = true;
		this.editi = i;
		this.editj = j;
		this.editText = this.results[i]["note"+j] ? this.results[i]["note"+j] : "";
	}

}
