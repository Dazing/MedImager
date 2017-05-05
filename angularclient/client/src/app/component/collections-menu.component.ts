import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import { CollectionService } from '../service/collection.service';
import { SearchService } from '../service/search.service';
import { UserService } from '../service/user.service';
import { Image, Collection } from '../model/image';
import { Filter } from '../model/tag';


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

	public tags: string[] = [];
	public searchParams = [];
	public searchParamNames = [];
	public filters: Filter[] = [];

	constructor(
		private collectionService: CollectionService,
		private searchService: SearchService,
		private userService: UserService,
		private router: Router

	){
		this.privSearchMode = new Subject<boolean>();
		this.searchMode = this.privSearchMode.asObservable();
		this.privSelectedCollection = new Subject<Collection>();
		this.selectedCollection = this.privSelectedCollection.asObservable();
	}

	ngOnInit(): void {
		this.collectionService.collections.subscribe(collections => {
			this.collections = collections;	
		});
		this.collectionService.sharedCollections.subscribe(collections => {
			this.sharedCollections = collections;	
		});

		this.searchService.tags.subscribe(tags => {
			this.tags = tags;
			console.log(this.tags);
			
		});

		this.searchService.searchParameters.subscribe(parameters => {
			this.searchParamNames = Object.getOwnPropertyNames(parameters);
			this.searchParams = parameters;
			this.searchService.getSelectedSearchParameters();
		});

		this.searchService.selectedParameters.subscribe(params => {
			this.filters = params;
		});

		this.searchService.getSearchParameters();
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

	removeFilter(filter: Filter): void {
		this.searchService.deleteFilter(filter);
	}

	toggleSharedCollections(): void {
		this.sharedCollectionsExpanded = !this.sharedCollectionsExpanded;
	}

	newCollection(target): boolean {
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
		this.selectedCollectionId = event.currentTarget.getAttribute("data-id");
		this.privSelectedCollection.next(this.getCollectionById(this.selectedCollectionId));
		this.router.navigate(['/search']);
		this.privSearchMode.next(this.searchModeEnabled);
	}

	emitMode(): void {
		this.privSearchMode.next(this.searchModeEnabled);
		this.privSelectedCollection.next( !this.searchModeEnabled ? this.getCollectionById(this.selectedCollectionId) : undefined);
	}

	toggleUserPage(): void {
		this.userService.toggleUserPage();
	}
}