import { Component, OnInit, ViewChild } from '@angular/core';
import { Http } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { Server } from '../model/server';
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
	templateUrl: '../template/imagepage.component.html',
	providers: []
})
export class ImagePageComponent {
	@ViewChild('collectionsMenu') collectionsMenu: CollectionsMenu;


	private collectionsMenuVisible = false;
	private examinationIn: Number;
	private imageIn: Number;
	private imageData:any;
	private display: boolean = false;
	private imageDataLoaded: boolean = false;
	private error: boolean = false;
	private examination404 = false;

	constructor(private router: Router, private server: Server, private imagePageService: ImagePageService) {
		this.router.routerState.root.queryParams.subscribe(params => {
			this.examinationIn = params['examination'];
			this.imageIn = params['image'];
		});	
	}

	ngOnInit(): void {
		this.imagePageService.imageData.subscribe(imageData => {
			if(imageData == null) {
				this.error = true;
				this.display = true;
			}
			else{
				this.imageData = imageData;
				this.imageDataLoaded = true;
				
				if(this.imageData.imagePaths.length-1 >= this.imageIn && this.imageIn >= 0)
					this.display = true;
				else {
					this.error = true;
					this.display = true;
				}
			}
		});
		this.imagePageService.getImageData(this.examinationIn);
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

	private getImageIn(): Number {
        return this.imageIn;
    }

	toggleCollectionsMenu() {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
		this.collectionsMenu.show(this.collectionsMenuVisible);
  }
}
