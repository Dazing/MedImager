import { Component, ViewChild } from '@angular/core';
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
	suggestedAutocomplete;
	selectedAutocompleteIndex = -1;
	maxAutocompleteSuggestions = 7;
	searchFieldBlurred = true;
	@ViewChild('value') searchBox; 

	autocompleteList= ["tandtråd", "tandtroll", "tandvärk", "tandsten", "tandpetare", "tandkött", "herpes", "tandlös", "blomkål", "tandkossa", "kossan säger mu", "karies", "baktus"];;

	tags = [];

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
		console.log(this.form.getRawValue());
		
	}

	onNavpress(keycode): void {
		console.log(keycode);
		
		if (keycode == 40) { //down arrow
			console.log("down");
			this.selectedAutocompleteIndex++;
			if (this.selectedAutocompleteIndex >= this.suggestedAutocomplete.length) {
				this.selectedAutocompleteIndex = 0;
			}
		} else if (keycode == 38) { //up arrow
			console.log("up");
			this.selectedAutocompleteIndex--;
			if (this.selectedAutocompleteIndex < 0) {
				this.selectedAutocompleteIndex = this.suggestedAutocomplete.length -1;
			}
		} else if (keycode == 13) {
			console.log("enter");
			if (this.selectedAutocompleteIndex > -1 && this.selectedAutocompleteIndex <= this.suggestedAutocomplete.length) {
				this.addTag(this.suggestedAutocomplete[this.selectedAutocompleteIndex]);
			}
		} else if (keycode == 27) {
			console.log("escape");
			this.searchFieldBlurred = true;
			this.selectedAutocompleteIndex = -1;
		}
	}

	onSearchFieldChange(newValue): void {
		if (this.searchBox.nativeElement.value == "") {
			this.suggestedAutocomplete = [];
		} else {
			let textToMatch = this.searchBox.nativeElement.value;

			this.suggestedAutocomplete = this.autocompleteList.filter(
				function(el) {
					return el.toLowerCase().indexOf(textToMatch.toLowerCase()) > -1;
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
		console.log("ADD TAG");
		this.tags.push(term);
		console.log(this.tags);
		//this.searchBox.setAttribute("value", "");
		this.emptySearchField();
	}

	removeTag(index: number): void {
		this.tags.splice(index, 1);
		console.log("REMOVE TAG");
		console.log(this.tags);
	}

	generateAutocompleteItemId(index: number): string {
		return ("sugg-"+index);
	}

	generateTagId(index: number): string {
		return ("tag-"+index);
	}

}
