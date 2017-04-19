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

	public parameters: any;
	public parameterStrings: any;
	public parameterLists;
	public jsonReady = false;
	public selectedParameterLists: number[] = [-1];
	public selectedTerms: number[] = [-1];
	private oldFiltersReceived: boolean = false;
	private paramsReceived = false;

	@ViewChild('advancedBar') advancedBar;

	constructor(
		private router: Router, 
		private ar: ActivatedRoute,
		private formBuilder: FormBuilder,
		private searchService: SearchService
	){}

	ngOnInit(): void {
		this.searchService.searchParameters.subscribe(parameters => {
			this.parameterLists = Object.getOwnPropertyNames(parameters);
			this.parameters = parameters;
			this.searchService.getSelectedSearchParameters();
			this.jsonReady = true;
			
		});
		this.searchService.selectedParameters.subscribe(params => {
			if (!this.paramsReceived) {
				if (params.length > 0) {
					let thisHandle = this;
					thisHandle.selectedParameterLists = [];
					thisHandle.selectedTerms = [];
					params.forEach(param => {
						thisHandle.selectedParameterLists.push(param.parameter);
						thisHandle.selectedTerms.push(param.value);
					});
				}
				this.paramsReceived = true;
			}
		});

		this.searchService.filterDeletion.subscribe(filter => {
			let deleted = false;
			for (let i=0; i < this.selectedParameterLists.length; i++) {
				if (this.selectedParameterLists[i] == filter.parameter && this.selectedTerms[i] == filter.value) {
					this.selectedParameterLists.splice(i,1);
					this.selectedTerms.splice(i,1);
					if (this.selectedParameterLists.length < 1) {
						this.selectedParameterLists.push(-1);
						this.selectedTerms.push(-1);
					}
					deleted = true;
				}
			}
			if (!deleted) {
				console.log("WARNING: didn't delete a filter despite getting filter delete event, something might be wrong in advanced-form.component.ts");
			}
		});

		this.searchService.getSearchParameters();
		
	}

	addDiagDef(value: string): void {
		for (let i = 0; i < this.parameterLists.length; i++) {
			if (this.parameterLists[i] == "diagDef") {
				for (let j=0; j < this.parameters["diagDef"].length;j++) {
					if (this.parameters["diagDef"][j] == value) {
						this.selectedParameterLists.unshift(i);
						this.selectedTerms.unshift(j);
					}
				}
			}
		}
		this.sendParamsToService();
	}

	getBarHeight(): number {
		return this.advancedBar.offsetHeight;
	}

	public deleteFilter(index: number):void {
		this.selectedParameterLists.splice(index,1);
		this.selectedTerms.splice(index,1);
		if (this.selectedParameterLists.length < 1) {
			this.selectedParameterLists.push(-1);
			this.selectedTerms.push(-1);
		}
		this.sendParamsToService();
	}

	private onChangeParameterList(index: number){
		this.selectedTerms[index] = -1;
		this.sendParamsToService();
	}

	public sendParamsToService() {
		this.searchService.setSelectedSearchParameters(this.selectedParameterLists, this.selectedTerms);
	}


	private addRow(){
		this.selectedParameterLists.push(-1);
		this.selectedTerms.push(-1);
	}
}
