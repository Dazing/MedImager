import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { Http, RequestOptions, ResponseContentType, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import { Server } from '../model/server';
import { SearchService } from '../service/search.service';

@Component({
	selector: 'image-thumbnail',
	templateUrl: '../template/image-thumbnail.component.html'
})
export class ImageThumbnailComponent implements OnInit{
    @Input('src') src: string = '';
    @ViewChild('image') image;

    thumbnailLoaded: boolean = false;
    thumbnailUrl:any = '';

    examinationID = '';
    imageIndex = '';

    constructor(
        private http: Http,
        private server: Server,
        private searchService: SearchService
    ){

    }

    ngOnInit(){
        this.fetchImage();
        let srcSplit = this.src.split('/');
        this.examinationID = srcSplit[0];
        this.imageIndex = srcSplit[1];
    }

    private fetchImage(): void {
        this.searchService.getImage(this.src, url => {
            this.image.nativeElement.style.backgroundImage = url;
        },true);

		/*var url = (this.server.getUrl() + '/thumbnail/'+this.src);
		// Set authorization header
		let headers = new Headers();
		headers.append('Authorization', sessionStorage.getItem("currentUser"));
		headers.append('Content-Type', 'image/jpg');
		
		let options = new RequestOptions({ 
			headers: headers, 
			responseType: ResponseContentType.Blob
		});

		this.http.get(url, options)
			.toPromise()
            .then(response => {
			    this.thumbnailUrl = window.URL.createObjectURL(response.blob());
                this.thumbnailLoaded = true;
			})
			.catch(e => {
				console.log("error fetching image");
			});
            */
    }
}