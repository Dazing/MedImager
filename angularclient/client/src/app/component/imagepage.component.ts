import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Router, ActivatedRoute } from '@angular/router';
import { Server } from '../model/server';
import { Observable } from 'rxjs';
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
	private collectionsMenuVisible = false;
	private examinationIn: String;
	private imageIn: String;
	private displayOrNot: String = "block";

	constructor(private router: Router, private server: Server) {
		this.router.routerState.root.queryParams.subscribe(params => {
			this.examinationIn = params['examination'];
			this.imageIn = params['image'];
		});
	 }

	private getPhoto(): string {
        return this.server.getUrl() + '/image/' + this.examinationIn +'/' + this.imageIn;
    }

	toggleCollectionsMenu() {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
	}

}
