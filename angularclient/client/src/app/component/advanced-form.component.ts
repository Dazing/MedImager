import { Component, ViewChild, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'

import { SearchService } from '../service/search.service';
import { AdvancedFormService } from '../service/advanced-form.service';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

@Component({
	selector: 'advanced-form',
	templateUrl: '../template/advanced-form.template.html',
  	providers: [FormBuilder]
})
export	 class AdvancedFormComponent {
	@Input('visible') visible: boolean; 

	public searchableStuff: any;
	public searchableStuffStrings: any;
	public searchableStuffLists;
	public jsonReady = false;
	public selectedSearchableStuffLists: Array<Number> = [null];
	public selectedTerms: Array<Number> = [null];


	constructor(
		private router: Router, 
		private ar: ActivatedRoute,
		private formBuilder: FormBuilder,
		private searchService: SearchService
	){}

	ngOnInit(): void {
		this.searchService.searchableStuff.subscribe(searchableStuff => {
			this.searchableStuffLists = Object.getOwnPropertyNames(searchableStuff);
			this.searchableStuff = searchableStuff;
			this.jsonReady = true;
		});
		this.searchService.getSearchableStuff();
	}

	private onChangeSearchableStuffList(selectId, listId){
		this.selectedSearchableStuffLists[selectId] = listId;
	}


	private onChangeSearchableStuffString(selectId, listId){
		this.selectedTerms[selectId] = listId;
	}

	private addRow(){
		this.selectedSearchableStuffLists.push(0);
		this.selectedTerms.push(0);
	}
}
