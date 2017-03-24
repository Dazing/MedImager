import { Component, OnInit } from '@angular/core';
import { ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core'
import { SearchService } from '../service/search.service';

@Component({
	selector: 'thumbnail',
	templateUrl: '../template/thumbnail.component.html'
})
export class ThumbnailComponent implements OnInit {

	private searchresults;

	constructor(private searchService: SearchService, public ref: ChangeDetectorRef){}

	ngOnInit(): void {
		console.log("Thumbnail init");
		
		var a = this.searchService.images;
		console.log(a);
		

		this.searchService.images.subscribe(images => {
			this.searchresults = images;

		})
	}

	myClick():void{
		console.log("Hej"+this.searchresults);
		
	}
}
