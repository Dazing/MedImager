import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from '@angular/http';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'
import { Image, Collection } from '../model/image';

import { PopupService } from '../service/popup.service';
import { SearchService } from '../service/search.service';
import { PopupComponent } from './popup.component';
import { CollectionsMenu } from './collections-menu.component';
import { CollectionTopMenu } from './collection-topmenu.component';
import { CollectionThumbnailComponent } from './collection-thumbnail.component';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

// Observable class extensions
import 'rxjs/add/observable/of';
// Observable operators
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';

@Component({
	selector: 'search',
	templateUrl: '../template/search.component.html',
	providers: [] // Changed from "PopupService" to none, start here if popup breaks
})
export class SearchComponent {
	images: Image[];
	private searchTerms = new Subject<string>();
	private collectionsMenuVisible = true;
	private settingsPageOpen = true;

	@ViewChild('collectionsMenu') collectionsMenu: CollectionsMenu;
	@ViewChild('collectionTopmenu') collectionTopmenu: CollectionTopMenu;
	@ViewChild('collectionThumbnail') collectionThumbnail: CollectionTopMenu;

	public modeSelected = false;
	public searchMode: boolean;
	public selectedCollection: Collection;

	form;

	constructor(
		private searchService: SearchService,
		private popupService: PopupService,
		private route: ActivatedRoute
	){
		console.log("ROUTER CHILDREN:");
		console.log(this.route.params);
	}



	ngAfterViewInit(): void {
		this.collectionsMenu.searchMode.subscribe(mode => {
			this.searchMode = mode;
		});
		this.collectionsMenu.selectedCollection.subscribe(sel => {
			if (sel != undefined) {
				this.selectedCollection = sel;
				this.collectionTopmenu.setCollection(sel);
				this.collectionThumbnail.setCollection(sel);
			}
		});
		this.collectionTopmenu.collectionDeleted.subscribe(del => {
			this.collectionTopmenu.resetMenu();
			this.collectionsMenu.goToSearch();
		});
		this.collectionsMenu.emitMode();
		this.modeSelected = true;
	}

	
	onEnter(term: string){
		 console.log(JSON.stringify(this.images));
	}

	search(term: string): void {
    this.searchTerms.next(term);
  }
}
