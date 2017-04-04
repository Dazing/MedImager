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

	private searchresults;
	private url;
	@Input() gridType: string; //'search' if list of search results, 'collection' if viewing collection

	constructor(
		private searchService: SearchService,
		private popupService: PopupService,
		public ref: ChangeDetectorRef, 
		private server: Server
	){}

	ngOnInit(): void {
		console.log("Thumbnai init");
		this.url = this.server.getUrl();
		
		var a = this.searchService.images;
		console.log(a);
		
		// if (this.gridType == "search") {
		// 	this.searchService.images.subscribe(images => {
		// 		this.searchresults = images;
		// 	})
		// } else if (this.gridType == "collection") {
		// 	this.searchService.images.subscribe(images => {
		// 		this.searchresults = images;
		// 	})
		// }
		this.searchService.images.subscribe(images => {
				this.searchresults = images;
		 	})
		

		this.popupService.searchResult.subscribe(searchResult => {
			if (searchResult.direction > 0) {
				this.showNextImage(searchResult.examinationIndex, searchResult.imageIndex);
			} else if (searchResult.direction < 0) {
				this.showPreviousImage(searchResult.examinationIndex, searchResult.imageIndex);
			}
		})
	}

	showNextImage(examinationIndex: number, imageIndex: number): void {
		let newImageIndex = (imageIndex + 1) % this.searchresults[examinationIndex].imagePaths.length;
		if (newImageIndex == 0) {
			do {
				examinationIndex = (examinationIndex + 1) % this.searchresults.length;
			} while (this.searchresults[examinationIndex].imagePaths.length < 1)
		}
		this.popupService.setPopupWithSearchIndex(this.searchresults[examinationIndex], newImageIndex, examinationIndex);
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
		this.popupService.setPopupWithSearchIndex(this.searchresults[examinationIndex], newImageIndex, examinationIndex);
	}

	onImageClick(examinationIndex: number, imageIndex: number):void{
		this.popupService.setPopupWithSearchIndex(this.searchresults[examinationIndex], imageIndex, examinationIndex);
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



}
