import { Headers, Http } from '@angular/http';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';
import { Image } from '../model/image';

@Injectable()
export class ImagePageService {
    imageData:Observable<string[]>;

    private privImageData: Subject<string[]>;

    constructor(private http: Http, private router: Router) {
        this.privImageData = new Subject<string[]>();
        this.imageData = this.privImageData.asObservable();
    }

    getImageData(examinationIn:Number): void {
        var url = ('http://localhost:8080/ExaminationServer/examData/api/examination/'+examinationIn);
        var responsejson:JSON;

        this.http.get(url)
			.toPromise()
			.then(response => {
                    responsejson = response.json();
			})
			.catch(e => {
				alert("Server unreachable, try again later!");
			});
    }

    getImage(): Image {
		return new Image();
	}
}