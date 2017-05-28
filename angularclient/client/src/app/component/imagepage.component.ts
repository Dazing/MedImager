import { Component, OnInit, ViewChild, Input, SecurityContext } from '@angular/core';
import { Http, RequestOptions, Headers } from '@angular/http';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
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

	private examinationIn: string;
	private imageIn: string;

	private collectionsMenuVisible = false;
	
	private imageData:any;
	private display: boolean = false;
	private imageDataLoaded: boolean = false;
	private error: boolean = false;	
	private examination404 = false;
	private otherImages:string[];
	private otherExams:string[] = [];

	private patient = [];
	private selectedExamIndex = -1;
	private imageLoaded = false;
	public imageSrc: SafeResourceUrl;


	constructor(
		private server: Server, 
		private http: Http,
		private imagePageServiceOld: ImagePageService, 
		private location: Location, 
		private route:ActivatedRoute,
		private sanitizer: DomSanitizer
	) 
	{

	}

	ngOnInit(): void {
		this.route.url.subscribe(url => {
			if (url.length == 3) {
				if (url[0].path == 'image') {
					if (!/[0-9]+\/[0-9]+/.test(url[1].path + '/' + url[2].path)) {
						console.warn('BAD IMAGE ROUTE');
					}
					this.setCurrentImage(url[1].path, +url[2].path);
				}
			}
		});
		/*
		this.imagePageService.otherExams.subscribe(otherExams => {
			this.otherExams = otherExams.sort(this.sortAlgorithm);
		});
		this.imagePageService.getImageData(this.examinationIn);
		this.imagePageService.getOtherExaminations(this.examinationIn);
		*/
	}

	loadPatient() {
		this.patient = [];
		this.display = false;
		let headers = new Headers();
		headers.append('Authorization', sessionStorage.getItem("currentUser"));
		let options = new RequestOptions({ headers: headers });
		let url = this.server.getUrl() + '/patient/' + this.examinationIn;
		this.http.get(url, options)
		.toPromise()
		.then(res => {
			let patient = res.json();
			let found = false;
			patient.forEach(exam => {
				exam.thumbnailUrls = [];
				for (let i=0; i < exam.imagePaths.length; i++) {
					exam.thumbnailUrls.push('');
					this.http.get(this.server.getUrl() + '/thumbnail/' + exam.examinationID + '/' + i, options)
					.toPromise()
					.then(res => {
						exam.thumbnailUrls[i] = window.URL.createObjectURL(res.blob());
					});
				}
				
			});
			for(let i=0;i < patient.length; i++){
				if (patient[i]['examinationID'] == this.examinationIn) {
					this.selectedExamIndex = i;
				}
			}
			if (!found) {
				console.warn('requested patient does not contain selected exam id, this should not happen');
			}
			this.patient = patient;
			this.error = false;
			this.loadImage();
		})
		.then(e => {
			this.error = true;
		});
	}

	setCurrentImage(examinationID: string, imageIndex: number) {
		let examChanged = this.examinationIn != examinationID;
		let imageChanged = this.imageIn != ''+imageIndex;
		let patientFound = !examChanged;
		this.examinationIn = examinationID;
		this.imageIn = ''+imageIndex;
		if (examChanged) {
			for (let i=0; i < this.patient.length; i++) {
				if (this.patient[i]['examinationID'] == this.examinationIn) {
					patientFound = true;
					this.selectedExamIndex = i;
				}
			}
		}
		if (patientFound) {
			this.loadImage();
		} else {
			this.loadPatient();
		}
	}

	loadImage() {
		let headers = new Headers();
		headers.append('Authorization', sessionStorage.getItem("currentUser"));
		let options = new RequestOptions({ headers: headers });
		let url = this.server.getUrl() + '/patient/' + this.examinationIn;
		this.imageLoaded = false;
		this.http.get(url, options)
		.toPromise()
		.then(res => {
			this.imageSrc = this.sanitizer.bypassSecurityTrustResourceUrl(
				window.URL.createObjectURL(res.blob())
			)
			this.imageLoaded = true;
			this.display = true;
		});
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

	private getImageIn(): string {
        return this.imageIn;
    }

	private onRelatedClick(imageId): void {
		this.location.replaceState("/image?examination=" + this.examinationIn + "&image=" + imageId);
		this.imageIn = imageId;
	}

	private changeExamination(examinationId): void {
		//this.router.navigateByUrl("/image?examination=" + examinationId + "&image=0");
		this.examinationIn = examinationId;
		//this.imageIn = 0;
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
