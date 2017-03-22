import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';

import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

// Observable class extensions
import 'rxjs/add/observable/of';
// Observable operators
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';

@Component({
	selector: 'imagepage',
	templateUrl: './imagepage.component.html'
})
export class ImagePageComponent {
	private collectionsMenuVisible = false;

  toggleCollectionsMenu() {
	this.collectionsMenuVisible = !this.collectionsMenuVisible;
  }

}
