import { Component, OnInit, HostListener } from '@angular/core';
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
    private visible = false;

    constructor(
		private searchService: SearchService,
		private popupService: PopupService,
		public ref: ChangeDetectorRef, 
		private server: Server
	){
     
    }

	ngOnInit(): void {

		this.popupService.popup.subscribe(popup => {
			this.popup = popup;
            this.visible = true;
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

    hidePopup(): void {
        this.visible = false;
    }

    private getUrl(): string {
        return this.server.getUrl() + '/image/' + this.popup.examinationID +'/' + this.popup.imageIndex;
    }

    
    @HostListener('window:keydown', ['$event'])
        keyboardInput(event: KeyboardEvent) {
            if (event.keyCode == 27)
                this.hidePopup();
            else if (event.keyCode == 37) {
                {}//prev image
            }
            else if (event.keyCode == 39) {
                this.popupService.setNextImage(this.popup.examinationID, this.popup.imageIndex);
            }
        }

}