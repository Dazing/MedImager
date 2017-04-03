import { Component, OnInit, ViewChild } from '@angular/core';
import { Http } from '@angular/http';
import { CollectionsMenu } from './collections-menu.component'
import { Image } from '../model/image';

@Component({
	selector: 'collection',
	templateUrl: '../template/collection.component.html',
	providers: []
})
export class CollectionComponent implements OnInit {
	
	@ViewChild('collectionsMenu') collectionsMenu: CollectionsMenu;
	private collectionsMenuVisible = false;

	ngOnInit(): void {

		
	
	}
	
	toggleCollectionsMenu() {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
		this.collectionsMenu.show(this.collectionsMenuVisible);
	}
}