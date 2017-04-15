import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import { CollectionService } from '../service/collection.service';
import { SearchService } from '../service/search.service';
import { Image, Collection } from '../model/image';


@Component({
	selector: 'collections-menu',
	templateUrl: '../template/collections-menu.component.html',
	providers: []
})

export class CollectionsMenu implements OnInit {

	private collections: Collection[] = [];
	private sharedCollections: Collection[] = [];
	private visible = true;
	private myCollectionsExpanded: boolean = true;
	private sharedCollectionsExpanded: boolean = true;
	private searchModeEnabled: boolean = true;
	private selectedCollectionId: number;
	private newCollectionValid: string = "none";

	@ViewChild('searchParamsContainer') searchParamsContainer;
	@ViewChild('newCollectionInput') newCollectionInput;
	@ViewChild('collectionsMenu') collectionsMenu: CollectionsMenu;

	public searchMode: Observable<boolean>;
	private privSearchMode: Subject<boolean>;
	public selectedCollection: Observable<Collection>;
	private privSelectedCollection: Subject<Collection>;

	public isVisible: Observable<boolean>;
	private privIsVisible: Subject<boolean>;

	private tags: string[] = [];

	constructor(
		private collectionService: CollectionService,
		private searchService: SearchService
	){
		this.privSearchMode = new Subject<boolean>();
		this.searchMode = this.privSearchMode.asObservable();
		this.privSelectedCollection = new Subject<Collection>();
		this.selectedCollection = this.privSelectedCollection.asObservable();
	}

	ngOnInit(): void {
		this.collectionService.collections.subscribe(collections => {
			console.log("sub: "+collections);
			this.collections = collections;	
		});
		this.collectionService.sharedCollections.subscribe(collections => {
			console.log("sub shared: "+collections);
			this.sharedCollections = collections;	
		});

		this.searchService.tags.subscribe(tags => {
			this.tags = tags;
			console.log(this.tags);
			
		});


		this.collectionService.getCollectionList();
		console.log("TODO: uncomment getSharedCollectionList() in collections-menu when it is fixed om backend");
		//this.collectionService.getSharedCollectionList();
	}

	show(visible: boolean): void {
		this.visible = visible;
	}

	toggleCollectionsMenu(show?: boolean) {
		this.visible = !this.visible;
		this.visible = show==undefined ? this.visible : show;
		this.show(this.visible);
  	}

	removeTag(tag: string): void {
		this.searchService.removeTag(tag);
	}

	toggleSharedCollections(): void {
		this.sharedCollectionsExpanded = !this.sharedCollectionsExpanded;
	}

	newCollection(target): boolean {
		console.log(target.value);
		if (target.value == "test") { //stick in various kontroller här
			this.newCollectionValid = 'invalid';
			return false;
		}
		this.newCollectionValid = 'valid'
		this.collectionService.createCollection(target.value);
		target.value = "";
		return true;
		
	}

	goToSearch():void {
		this.searchModeEnabled = true;
		this.privSearchMode.next(this.searchModeEnabled);
	}

	private getCollectionById(id: number): Collection {
		for (let col of this.collections) {
			if (col.id == id) {
				return col
			}
		}
		return undefined;
	}

	collectionClicked(event): void {
		this.searchModeEnabled = false;
		console.log("clicked collection id: " + event.currentTarget.getAttribute("data-id"));
		this.selectedCollectionId = event.currentTarget.getAttribute("data-id");
		this.privSelectedCollection.next(this.getCollectionById(this.selectedCollectionId));
		this.privSearchMode.next(this.searchModeEnabled);
	}

	emitMode(): void {
		this.privSearchMode.next(this.searchModeEnabled);
		this.privSelectedCollection.next( !this.searchModeEnabled ? this.getCollectionById(this.selectedCollectionId) : undefined);
	}

}