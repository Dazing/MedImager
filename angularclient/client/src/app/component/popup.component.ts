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
    public popup;
    public visible = false;
    public arrowsVisible = false;
    public resolutionLoaded = false;
    public imageHeight: number;
    public imageWidth: number;

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
            this.resolutionLoaded = false;
            var img = new Image();
            var popupScopeHandle = this;

            img.onload = function(){
                popupScopeHandle.imageHeight = img.height;
                popupScopeHandle.imageWidth = img.width;
                popupScopeHandle.resolutionLoaded = true;
            }
            img.src = this.getUrl();
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

    showArrows(visible: boolean): void {
        this.arrowsVisible = visible;
    }

    hidePopup(): void {
        this.visible = false;
    }

    private getUrl(): string {
        return this.server.getUrl() + '/image/' + this.popup.examinationID +'/' + this.popup.imageIndex;
    }

    private getIndex(): string {
        return this.popup.imageIndex;
    }

    nextImage(): void {
        this.popupService.setNextImage(this.popup.searchIndex, this.popup.imageIndex);
    }

    prevImage(): void {
        this.popupService.setPreviousImage(this.popup.searchIndex, this.popup.imageIndex);
    }

    listToText(items: string[]): string {
        let tot = "";
        for (let item of items) {
            if (tot != "") {
                tot += ", "
            }
            tot += item;
        }
        return tot;
    }

    
    @HostListener('window:keydown', ['$event'])
        keyboardInput(event: KeyboardEvent) {
            if (event.keyCode == 27)
                this.hidePopup();
            else if (event.keyCode == 37) {
                if (this.visible) {
                    this.prevImage();
                }
            }
            else if (event.keyCode == 39) {
                if (this.visible) {
                    this.nextImage();
                }
            }
        }

}