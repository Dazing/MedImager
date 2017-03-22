import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'
import { Image } from '../model/image';

import { SearchService } from '../service/search.service';
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
	providers: []
})
export class SearchComponent {
	images: Image[];
	private searchTerms = new Subject<string>();

	form;

	private collectionsMenuVisible = false;

	constructor(private searchService: SearchService){
		
	}

	/*ngOnInit(): void {
		this.images = this.searchTerms
		.debounceTime(300)        // wait 300ms after each keystroke before considering the term
		.distinctUntilChanged()   // ignore if next search term is same as previous
		.switchMap(term => term   // switch to new observable each time the term changes
			// return the http search observable
			? this.searchService.getSearch()
			// or the observable of empty heroes if there was no search term
			: Observable.of<Image[]>([]))
			.catch(error => {
				// TODO: add real error handling
				console.log(error);
				return Observable.of<Image[]>([]);
			});
		}
	*/
	onEnter(term: string){
		 console.log(JSON.stringify(this.images));

	}

	search(term: string): void {
    this.searchTerms.next(term);
  }

  toggleCollectionsMenu() {
	this.collectionsMenuVisible = !this.collectionsMenuVisible;
  }

}
