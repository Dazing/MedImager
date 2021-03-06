import { Component, OnInit, Input } from '@angular/core';
import { ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
import { Server } from '../model/server';
import { SearchService } from '../service/search.service';
import { PopupService } from '../service/popup.service';


@Component({
	selector: 'thumbnail',
	templateUrl: '../template/thumbnail.component.html'
})
export class ThumbnailComponent implements OnInit {

	public searchresults;
	public url;
	public searchMade:boolean = false;

	//note edit helper functions:
	public editMode: boolean = false;
	public editi: number;
	public editj: number;
	public editText: string;

	constructor(
		private searchService: SearchService,
		private popupService: PopupService,
		public ref: ChangeDetectorRef, 
		private server: Server
	){}

	ngOnInit(): void {
		this.url = this.server.getUrl();
		

		this.searchService.images.subscribe(images => {
			this.searchresults = images;
			this.searchMade = true;
			
		})
		//this.searchService.getSearch();

	}

	showNextImage(examinationIndex: number, imageIndex: number): void {
		let newImageIndex = (imageIndex + 1) % this.searchresults[examinationIndex].imagePaths.length;
		
		if (newImageIndex == 0) {
			do {
				examinationIndex = (examinationIndex + 1) % this.searchresults.length;
			} while (this.searchresults[examinationIndex].imagePaths.length < 1)
		}
		this.onImageClick(examinationIndex, newImageIndex);
	}

	showPreviousImage(examinationIndex: number, imageIndex: number): void {
		let newImageIndex;
		
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
		this.onImageClick(examinationIndex, newImageIndex);
	}

	onImageClick(examinationIndex: number, imageIndex: number):void{
		let thisHandle = this;
		this.popupService.setPopupWithSearchIndex(this.searchresults[examinationIndex], imageIndex, examinationIndex, 
		(examinationIndex, imageIndex)=>{
			//prev image
			thisHandle.showPreviousImage(examinationIndex, imageIndex);
		}, (examinationIndex, imageIndex)=>{
			//next image
			thisHandle.showNextImage(examinationIndex, imageIndex);
		});
	}

	getDiagDef(index: number): string {
		var diagnoses = "";
		for (let diag of this.searchresults[index].diagDef) { 
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
		for (let diag of this.searchresults[index].diagTent) { 
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
		for (let diag of this.searchresults[index].diagHist) { 
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
		return this.searchresults[index].age;
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

	constructInnerText(i: number): string {
		let innerText = "";
		if (this.searchresults[i].diagDef.length > 0) {
			innerText += "<b>Diagnos: </br>" 
			+ this.getDiagListAsString(this.searchresults[i].diagDef)
			+ "<br>";
		}

		if (this.searchresults[i].diagTent.length > 0) {
			innerText += "<b>Diagnos: </br>" 
			+ this.getDiagListAsString(this.searchresults[i].diagTent)
			+ "<br>";
		}

		return innerText;

	}


	getNote(i:number, j: number): string {
		return this.searchresults[i]["note"+j];
	}

	setNote(i:number, j: number, note?: string): void {
		let noteEdit: any  = document.getElementById('note-edit-' + i + '-' + j);
		this.searchresults[i]["note"+j] = noteEdit.value;
		this.editMode = false;
	}

	editNote(i:number,j:number): void {
		this.editMode = true;
		this.editi = i;
		this.editj = j;
		this.editText = this.searchresults[i]["note"+j] ? this.searchresults[i]["note"+j] : "";
	}

	getNumberOfResults(): number {
		var countResults:number = 0;
		for(var i=0; i<this.searchresults.length; i++){
				countResults = countResults + this.searchresults[i].imagePaths.length;
		}
		return countResults;
	}

	generateId(examId: number, index: number):string {
		return "result-image-"+examId+"-"+index; 
	}

	
}
