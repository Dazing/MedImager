import { Injectable } from '@angular/core';

@Injectable()
export class SearchService {

	getSearch(): Observable<Object[]> {
		return [{}];
	}
}
