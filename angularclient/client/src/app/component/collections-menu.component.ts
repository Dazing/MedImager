import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import { CollectionService } from '../service/collection.service';
import { Image, Collection } from '../model/image';


@Component({
	selector: 'collections-menu',
	templateUrl: '../template/collections-menu.component.html',
	providers: []
})

export class CollectionsMenu implements OnInit {

	private collections;
	private visible;

	@ViewChild('collectionsMenu') collectionsMenu: CollectionsMenu;
	private collectionsMenuVisible = false;

	public isVisible: Observable<boolean>;
	private privIsVisible: Subject<boolean>;

	constructor(
		private collectionService: CollectionService,
	){
		this.privIsVisible = new Subject<boolean>();
		this.isVisible = this.privIsVisible.asObservable();
	}

	ngOnInit(): void {
		this.collectionService.collections.subscribe(collections => {
			console.log("sub: "+collections);
			this.collections = collections;
		})
	}

	show(visible: boolean): void {
		this.visible = visible;
	}

	toggleCollectionsMenu(show?: boolean) {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
		this.collectionsMenuVisible = show==undefined ? this.collectionsMenuVisible : show;
		this.show(this.collectionsMenuVisible);
		this.privIsVisible.next(this.collectionsMenuVisible);
  	}

	//myCollections = [{name:"Tandsten genom tiderna", id:"111111"}, {name:"Karies och baktus", id:"222222"},{name:"Bland tomtar och tandtroll", id:"3333"}];
	sharedCollections = [{name:"extern samling 1", id:"1232123"},{name:"extern samling 1", id:"1232123"}];
}