import { Component, OnInit } from '@angular/core';
import { ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
import { Server } from '../model/server';
import { SearchService } from '../service/search.service';

@Component({
	selector: 'thumbnail',
	templateUrl: '../template/thumbnail.component.html'
})
export class ThumbnailComponent implements OnInit {

	private searchresults;
	private url;

	constructor(
		private searchService: SearchService,
		public ref: ChangeDetectorRef, 
		private server: Server
	){}

	ngOnInit(): void {
		console.log("Thumbnai init");
		this.url = this.server.getUrl();
		
		var a = this.searchService.images;
		console.log(a);
		

		this.searchService.images.subscribe(images => {
			this.searchresults = images;

		})
	}

	myClick():void{
		console.log("Hej"+this.searchresults);
		
	}

	formatURL(url: any): string {
		return url.replace(/\\/g,"/");
	}
}
