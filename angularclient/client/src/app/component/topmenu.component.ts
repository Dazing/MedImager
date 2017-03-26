import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'

import { SearchService } from '../service/search.service';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

@Component({
	selector: 'top-menu',
	templateUrl: '../template/topmenu.component.html',
  	providers: [FormBuilder]
})
export class TopMenuComponent {
	form;
	active : boolean;
	autocompleteList;

	constructor(
		private router: Router, 
		private ar: ActivatedRoute,
		private formBuilder: FormBuilder,
		private searchService: SearchService
	){
		this.form = formBuilder.group({
			value: '',
			gender: '',
			smoke: '',
			snuff: '',
			includeTentative: '',
			includeHist: '',
			includeDiseasePast: ''
		});

		this.form.valueChanges.debounceTime(400).subscribe(data => {
			this.active = this.router.isActive(this.router.url,false);
		
			if (!(this.active)){
				this.router.navigate(['/search', { query: data }]);
			}
			console.log(data);
			
			this.searchService.getSearch(data);
		});

	}

	onEnter(term: string){
		console.log(this.form.getRawValue());
		
	}

	search(){
		
	}



}
