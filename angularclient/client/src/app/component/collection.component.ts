import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { Image } from '../model/image';

@Component({
	selector: 'collection',
	templateUrl: '../template/collection.component.html',
	providers: []
})
export class CollectionComponent implements OnInit {
	private collectionsMenuVisible = false;

	ngOnInit(): void {

		
	
	}
	
	toggleCollectionsMenu() {
		this.collectionsMenuVisible = !this.collectionsMenuVisible;
	}
}