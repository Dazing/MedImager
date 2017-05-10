import { Component, Input, OnInit } from '@angular/core';
import { Http, RequestOptions, ResponseContentType, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import { Server } from '../model/server';

@Component({
	selector: 'image-thumbnail',
	templateUrl: '../template/image-thumbnail.component.html'
})
export class ImageThumbnailComponent implements OnInit{
    @Input('src') src: string = '';

    thumnailLoaded: boolean = false;
    thumbnailUrl: string;

    examinationID = '';
    imageIndex = '';

    constructor(
        private http: Http,
        private server: Server
    ){

    }

    ngOnInit(){
        this.fetchImage();
        let srcSplit = this.src.split('/');
        this.examinationID = srcSplit[0];
        this.imageIndex = srcSplit[1];
    }

    private fetchImage(): void {
        console.log("RUnning SS getTHumb");
		
		var url = (this.server.getUrl() + '/thumbnail/'+this.src);
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
                console.log("got thumbnail response:");
                console.log(response);
                console.log("blobified:");
                console.log(response.blob());
			    this.thumbnailUrl = window.URL.createObjectURL(response.blob());
                console.log("thumb url:");
                console.log(this.thumbnailUrl);
                this.thumnailLoaded = true;
			})
			.catch(e => {
				console.log("error fetching image");
			});

    }
}