import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'

import { SearchService } from '../service/search.service';
import { Filter } from '../model/tag';
import { AdvancedFormComponent } from '../component/advanced-form.component';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

@Component({
	selector: 'top-menu',
	templateUrl: '../template/topmenu.component.html',
  	providers: [FormBuilder]
})
export class TopMenuComponent implements OnInit {
	//form;
	active : boolean;
	advancedBarVisible: boolean = false;
	suggestedAutocomplete;
	selectedAutocompleteIndex = -1;
	maxAutocompleteSuggestions = 7;
	searchFieldBlurred = true;
	searchFieldModel;
	paramsLoaded = false;
	@ViewChild('value') searchBox; 
	@ViewChild('menuBar') menuBar;
	@ViewChild('advancedForm') advancedForm: AdvancedFormComponent;

	params: any;
	filtersRaw: Filter[] = [];
	autocompleteList: string[] = [];
	tags: string[] = [];

	constructor(
		private router: Router, 
		private ar: ActivatedRoute,
		private formBuilder: FormBuilder,
		private searchService: SearchService
	){}

	ngOnInit(): void {
		this.searchService.searchParameters.subscribe(params => {
			this.params = params;
			this.autocompleteList = params["diagDef"];
			this.paramsLoaded = true;
		});
		
		this.searchService.selectedParameters.subscribe(filters => {
			if (!this.paramsLoaded) {
				return;
			}
			this.tags = [];
			if (filters){
				let parameterNames = Object.getOwnPropertyNames(this.params);
				let indexOfDiagDef: number;
				for (let i=0; i < parameterNames.length;i++) {
					if (parameterNames[i] == "diagDef") {
						indexOfDiagDef = i;
					}
				}
				if(filters.length > 0) {
					for(let filter of filters) {
						if (filter.parameter == indexOfDiagDef) {
							this.tags.push(this.params["diagDef"][filter.value]);
						}
					}
				}
			}
			
		});

		this.searchService.getSearchParameters();
	}

	search(): void {
		this.searchService.getSearch();
		this.toggleAdvancedBar("hide");
	}

	emptySearchField(): void {
		this.searchBox.nativeElement.value = "";
		this.suggestedAutocomplete = [];
		this.selectedAutocompleteIndex = -1;
	}

	onSearchFieldBlur(): void {
		this.searchFieldBlurred = true;
		this.selectedAutocompleteIndex = -1;
	}

	onSearchFieldFocus(): void {
		this.searchFieldBlurred = false;
	}

	onEnter(term: string){
		//console.log(this.form.getRawValue());
		
	}

	onNavpress(keycode): void {
		switch (keycode) {
			case 40: { //down arrow
				this.selectedAutocompleteIndex++;
				if (this.selectedAutocompleteIndex >= this.suggestedAutocomplete.length) { //take care of looping
					this.selectedAutocompleteIndex = 0;
				}
				break;
			} case 38: { //up arrow
				this.selectedAutocompleteIndex--;
				if (this.selectedAutocompleteIndex < 0) { //take care of looping
					this.selectedAutocompleteIndex = this.suggestedAutocomplete.length -1;
				}
				break;
			} case 13: {
				if (this.selectedAutocompleteIndex > -1 && this.selectedAutocompleteIndex <= this.suggestedAutocomplete.length) {
					this.addTag(this.suggestedAutocomplete[this.selectedAutocompleteIndex]);
				}
				break;
			} case 27: {
				this.searchFieldBlurred = true;
				this.selectedAutocompleteIndex = -1;
				break;
			}
		}
	}

	onSearchFieldChange(newValue): void {
		if (this.searchBox.nativeElement.value == "") {
			this.suggestedAutocomplete = [];
		} else {
			let thisHandle = this;

			this.suggestedAutocomplete = this.autocompleteList.filter(
				function(el) {
					for (let alreadySelectedTag of thisHandle.tags) {
						if (el.toLowerCase() == alreadySelectedTag.toLowerCase()) {
							return false;
						}
					}
					return el.toLowerCase().indexOf(newValue.toLowerCase()) > -1;
				}
			);
			
			if (this.suggestedAutocomplete.length > this.maxAutocompleteSuggestions) {
				this.suggestedAutocomplete = this.suggestedAutocomplete.slice(0, this.maxAutocompleteSuggestions);
			}
			this.selectedAutocompleteIndex = -1;
			this.searchFieldBlurred = false;
		}
		
	}

	addTag(term: string): void {
		console.log("TERM: " + term);
		this.advancedForm.addDiagDef(term);
		this.emptySearchField();
	}

	removeTag(index: number): void {
		this.searchService.removeTag(this.tags[index]);
		this.tags.splice(index, 1);
		console.log("REMOVE TAG");
		console.log(this.tags);
		setTimeout(()=>{ 
			this.menuBar.nativeElement.dispatchEvent(new Event('resize'));
		}, 250)
	}

	generateAutocompleteItemId(index: number): string {
		return ("sugg-"+index);
	}

	generateTagId(index: number): string {
		return ("tag-"+index);
	}

	handleMousedown(term: string, event): void {
		if (event.button == 0) {
			this.addTag(term);
		}
	}

	toggleAdvancedBar(state:String) {
		console.log("CALLED: "+state);
		
		if(state == "show")
			this.advancedBarVisible = true;
		else if(state == "hide")
			this.advancedBarVisible = false;
		else
			this.advancedBarVisible = !this.advancedBarVisible;
	}

}
