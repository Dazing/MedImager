import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'searchCompletion'})
export class SearchCompletion implements PipeTransform {
	transform(terms: string[], term: string): string[] {
		console.log("SC: term"+term+", terms: "+terms);
		
		if (term) {
			return terms.filter(
				function(el) {
    				return el.toLowerCase().indexOf(term.toLowerCase()) > -1;
    			}
			)
		}
		else {
			return [];
		}
		
	}
}