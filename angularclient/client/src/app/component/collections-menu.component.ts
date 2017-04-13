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

	public collections: Collection[];
	public visible = true;
	public myCollectionsExpanded: boolean = true;
	public sharedCollectionsExpanded: boolean = true;
	public searchModeEnabled: boolean = true;
	public selectedCollectionId: number;
	public newCollectionValid: string = "none";

	@ViewChild('searchParamsContainer') searchParamsContainer;
	@ViewChild('newCollectionInput') newCollectionInput;
	@ViewChild('collectionsMenu') collectionsMenu: CollectionsMenu;

	public searchMode: Observable<boolean>;
	public privSearchMode: Subject<boolean>;
	public selectedCollection: Observable<Collection>;
	public privSelectedCollection: Subject<Collection>;

	public isVisible: Observable<boolean>;
	public privIsVisible: Subject<boolean>;

	public tags: string[] = [];

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
		})

		this.searchService.tags.subscribe(tags => {
			this.tags = tags;
			console.log(this.tags);
			
		});

		this.collectionService.getCollectionList();
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

	//myCollections = [{name:"Tandsten genom tiderna", id:"111111"}, {name:"Karies och baktus", id:"222222"},{name:"Bland tomtar och tandtroll", id:"3333"}];
	sharedCollections = [{name:"extern samling 1", id:"111111111"},{name:"extern samling 2", id:"222222222"}];
}