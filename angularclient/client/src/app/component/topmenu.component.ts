import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'

import { SearchService } from '../search/search.service';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

@Component({
	selector: 'top-menu',
	templateUrl: './topmenu.component.html',
  	providers: [FormBuilder]
})
export class TopMenuComponent {
	form;
	active : boolean;

	constructor(
		private router: Router, 
		private ar: ActivatedRoute,
		private formBuilder: FormBuilder,
		private searchService: SearchService
	){
		this.form = formBuilder.group({
			searchTerms: '',
			rokare: '',
			snusare: '',
			alder: ''
		})

		this.form.valueChanges.subscribe(data => {
			this.active = this.router.isActive(this.router.url,false);
		
			if (!(this.active)){
				this.router.navigate(['/search', { query: data }]);
			}

			this.searchService.getSearch(data);
		})
	}

	onEnter(term: string){
		
	}

	search(){
		
	}



}
