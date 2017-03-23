import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Image } from '../model/image';

@Component({
	selector: 'collection',
	templateUrl: '../template/collection.component.html',
	providers: []
})
export class CollectionComponent {
	private collectionsMenuVisible = false;
	
	toggleCollectionsMenu() {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
	}
}