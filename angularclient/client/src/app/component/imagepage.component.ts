import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Http } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { Server } from '../model/server';
import { Location } from '@angular/common';
import { ImagePageService } from '../service/imagepage.service';
import { Observable } from 'rxjs';
import { CollectionsMenu } from './collections-menu.component';
import { Subject } from 'rxjs/Subject';

// Observable class extensions
import 'rxjs/add/observable/of';
// Observable operators
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';

@Component({
	selector: 'imagepage',
	templateUrl: '../template/imagepage.component.html'
})
export class ImagePageComponent {
	@ViewChild('collectionsMenu') collectionsMenu: CollectionsMenu;

	private examinationIn: number;
	private imageIn: number;

	private collectionsMenuVisible = false;
	
	private imageData:any;
	private display: boolean = false;
	private imageDataLoaded: boolean = false;
	private error: boolean = false;	
	private examination404 = false;
	private otherImages:string[];
	private otherExams:string[] = [];
	private url;

	constructor(private server: Server, private imagePageServiceOld: ImagePageService, private location: Location, private route:ActivatedRoute) {
	}

	ngOnInit(): void {
		this.route.url.subscribe(url => {
			if (url.length == 3) {
				if (url[0].toString() == 'search') {
					
				}
				console.log(url[0]);
				console.log(url[1]);
				console.log(url[2]);
			}
		});
		/*
		this.imagePageService.imageData.subscribe(imageData => {
			if(imageData == null) {
				this.error = true;
				this.display = true;
			}
			else{
				this.imageData = imageData;
				this.otherImages = this.imageData.imagePaths;
				console.log(this.otherImages);
				this.imageDataLoaded = true;
				
				if(this.imageData.imagePaths.length-1 >= this.imageIn && this.imageIn >= 0 && this.imageIn)
					this.display = true;
				else {
					this.error = true;
					this.display = true;
				}
			}
		});
		this.imagePageService.otherExams.subscribe(otherExams => {
			this.otherExams = otherExams.sort(this.sortAlgorithm);
		});
		this.imagePageService.getImageData(this.examinationIn);
		this.imagePageService.getOtherExaminations(this.examinationIn);
		*/
	}

	notNull(value: any){
        if (value.length > 0){
            return true;
        }
        else {
            return false;
        }
    }

	listToText(items: string[]): string {
        let tot = "";
        for (let item of items) {
            if (tot != "") {
                tot += ", "
            }
            tot += item;
        }
        return tot;
    }

	private getPhoto(): string {
        return this.server.getUrl() + '/image/' + this.examinationIn +'/' + this.imageIn;
    }

	private getThumbnailUrl(imageId): string {
		var string = "'background-image': 'url(' + this.url +'/thumbnail/' + this.examinationIn + '/' + imageId + ')'";
		return string;
	}

	private getImageIn(): Number {
        return this.imageIn;
    }

	private onRelatedClick(imageId): void {
		this.location.replaceState("/image?examination=" + this.examinationIn + "&image=" + imageId);
		this.imageIn = imageId;
	}

	private changeExamination(examinationId): void {
		//this.router.navigateByUrl("/image?examination=" + examinationId + "&image=0");
		this.examinationIn = examinationId;
		this.imageIn = 0;
	}

	private examinationIdToDate(examinationId): string {
		var date = examinationId.substring(0,6);
		var year = date.substring(0,2);
		var month = date.substring(2,4);
		var day = date.substring(4,6);
		var monthString = "";
		switch(month){
			case "01":
				monthString = "januari";
				break;
			case "02":
				monthString = "februari";
				break;
			case "03":
				monthString = "mars";
				break;
			case "04":
				monthString = "april";
				break;
			case "05":
				monthString = "maj";
				break;
			case "06":
				monthString = "juni";
				break;
			case "07":
				monthString = "juli";
				break;
			case "08":
				monthString = "augusti";
				break;
			case "09":
				monthString = "september";
				break;
			case "10":
				monthString = "oktober";
				break;
			case "11":
				monthString = "november";
				break;
			case "12":
				monthString = "december";
				break;
			default:
				monthString = "??";
				break;
		}
		return day + " " + monthString + " '" + year;
	}

	private getNumberOfOtherExamImages(number): string {
		if(number == 1)
			return number + " bild";
		else
			return number + " bilder";
	}

	private sortAlgorithm(object1, object2): number {
		if (object1.examinationID < object2.examinationID)
			return -1;
		if (object1.examinationID > object2.examinationID)
			return 1;
		return 0;
	}

	toggleCollectionsMenu() {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
		this.collectionsMenu.show(this.collectionsMenuVisible);
	}
}
