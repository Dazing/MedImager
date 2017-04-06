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
		this.collectionsMenu.isVisible.subscribe(visible => {
			this.collectionsMenuVisible = visible;
		});
	}
	
	toggleCollectionsMenu(show?: boolean) {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
		this.collectionsMenuVisible = show==undefined ? this.collectionsMenuVisible : show;
		this.collectionsMenu.show(this.collectionsMenuVisible);
  	}
}