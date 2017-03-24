import { Component, OnInit } from '@angular/core';
import { ChangeDetectionStrategy, ChangeDetectorRef} from '@angular/core';
import { Server } from '../model/server';
import { SearchService } from '../service/search.service';
import { PopupService } from '../service/popup.service';


@Component({
	selector: 'popup',
	templateUrl: '../template/popup.component.html'
})
export class PopupComponent {
    private popup;

    constructor(
		private searchService: SearchService,
		private popupService: PopupService,
		public ref: ChangeDetectorRef, 
		private server: Server
	){
        
	this.popup = {
   "age": [
      "70"
   ],
   "allergy": [
      "Nej"
   ],
   "biopsySite": [],
   "diagDef": [
      "Atrofisk lichen planus"
   ],
   "diagHist": [],
   "diagTent": [],
   "disNow": [
      "Atopiskt eksem"
   ],
   "disPast": [
      "Nej"
   ],
   "drug": [
      "Nej"
   ],
   "examinationID": "980122112626",
   "factorNeg": [
      "Hård föda"
   ],
   "factorPos": [
      "Förbättrad munhygien"
   ],
   "family": [
      "Broder"
   ],
   "gender": [
      "1"
   ],
   "imagePaths": [
      "TestData.mvd\\Pictures\\G0222\\g02223.jpg",
      "TestData.mvd\\Pictures\\G0222\\g02222.jpg",
      "TestData.mvd\\Pictures\\G0222\\g02221.jpg"
   ],
   "lesnOn": [],
   "lesnSite": [],
   "skinPbl": [
      "Torr hud",
      "Eksem"
   ],
   "smoke": [
      "Nej"
   ],
   "snuff": [
      "Nej"
   ],
   "symptNow": [],
   "symptSite": [],
   "treatType": [
      "Information"
   ],
   "vasNow": [
      "3.2"
   ]
}
    }

	ngOnInit(): void {

		this.popupService.popup.subscribe(popup => {
			this.popup = popup;
		})
	}

    notNull(value: any){
        if (value.length > 0){
            return true;
        }
        else {
            return false;
        }
    }
}