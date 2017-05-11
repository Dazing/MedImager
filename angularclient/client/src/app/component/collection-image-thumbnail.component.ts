import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { Http, RequestOptions, ResponseContentType, Headers } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import { Server } from '../model/server';
import { SearchService } from '../service/search.service';

@Component({
	selector: 'collection-image-thumbnail',
	templateUrl: '../template/collection-image-thumbnail.component.html'
})
export class CollectionImageThumbnailComponent implements OnInit{
    @Input('src') src: string = '';
    @Input('index') i: number;
    @Input('note') note: string = undefined;
    @Input('deleteConfirmation') deleteConfirmation: boolean;
    @Input('editMode') editMode: boolean;
    @Input('deletingThis') deletingThis: boolean;
    @Input('editingThis') editingThis: boolean;

    @Output() editNote = new EventEmitter(); 
    @Output() confirmDelete = new EventEmitter();
    @Output() deleteConfirmed = new  EventEmitter();
    @Output() setNote = new EventEmitter();
    @Output() cancelEdit = new EventEmitter();

    thumbnailLoaded: boolean = false;
    thumbnailUrl:any = '';

    showNote: boolean = true;

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
			    this.thumbnailUrl = window.URL.createObjectURL(response.blob());
                this.thumbnailLoaded = true;
			})
			.catch(e => {
				console.log("error fetching image");
			});
            
    }

    getNote():string {
        return this.note;
    }

    
}