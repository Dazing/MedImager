import { Component, OnInit } from '@angular/core';

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

	constructor(
		private collectionService: CollectionService,
	){
		
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

	//myCollections = [{name:"Tandsten genom tiderna", id:"111111"}, {name:"Karies och baktus", id:"222222"},{name:"Bland tomtar och tandtroll", id:"3333"}];
	sharedCollections = [{name:"extern samling 1", id:"1232123"},{name:"extern samling 1", id:"1232123"}];

}