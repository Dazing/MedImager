import { Headers, Http, RequestOptions} from '@angular/http';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';
import { Image } from '../model/image';

import { Server } from '../model/server';

@Injectable()
export class ImagePageService {
    private headers = new Headers({'Content-Type': 'application/json'});
    imageData:Observable<string[]>;
    otherExams:Observable<string[]>

    private privImageData: Subject<string[]>;
    private privOtherExams: Subject<string[]>;

    constructor(private http: Http, private router: Router, private server: Server) {
        this.privImageData = new Subject<string[]>();
        this.privOtherExams = new Subject<string[]>();
        this.imageData = this.privImageData.asObservable();
        this.otherExams = this.privOtherExams.asObservable();
    }

    getImageData(examinationIn:Number): void {
        var url = (this.server.getUrl()+'/examination/'+examinationIn);

        // Set authorization header
		let headers = new Headers();
		headers.append('authorization', localStorage.getItem("currentUser"));
		let options = new RequestOptions({ headers: headers });

        this.http.get(url, options)
			.toPromise()
			.then(response => {
                   var responsejson = response.json();
                   this.privImageData.next(responsejson);
                   console.log(responsejson);
			})
			.catch(e => {
                this.privImageData.next(null);
			});
    }

    getOtherExaminations(examinationIn:Number): void {
        var url = (this.server.getUrl()+'/patient/'+examinationIn);

         // Set authorization header
		let headers = new Headers();
		headers.append('authorization', localStorage.getItem("currentUser"));
		let options = new RequestOptions({ headers: headers });


        this.http.get(url, options)
			.toPromise()
			.then(response => {
                   var responsejson = response.json();
                   this.privOtherExams.next(responsejson);
                   console.log(responsejson);
			})
			.catch(e => {
                this.privOtherExams.next(null);
			});
    }

    getImage(): Image {
		return new Image();
	}
}