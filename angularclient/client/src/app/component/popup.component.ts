import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { SafeResourceUrl } from '@angular/platform-browser';
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
    public liked:boolean = false;
    public imgSrc:SafeResourceUrl = '';
    public imageLoaded = false;

    private dialogVisible = false;
    private dialogType: string;

    constructor(
		private searchService: SearchService,
		private popupService: PopupService,
		public ref: ChangeDetectorRef, 
        private router: Router,
		private server: Server
	){
     
    }

	ngOnInit(): void {

		this.popupService.popup.subscribe(popup => {
			this.popup = popup;
            this.visible = true;
            this.resolutionLoaded = false;
            this.imageLoaded = false;
            this.searchService.getImage(this.popup.examinationID +'/' + this.popup.imageIndex, url=>{
                this.imgSrc=url;
                this.imageLoaded = true;
            })
        })


        this.popupService.dialogs.subscribe(dialog => {
            this.dialogType = dialog;
            this.dialogVisible = true;
        });
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
        this.popup = null;
    }

  

    private getIndex(): string {
        return this.popup.imageIndex;
    }

    nextImage(): void {
        //this.popupService.setNextImage(this.popup.searchIndex, this.popup.imageIndex);
        if (this.popupService.callbackNext) {
            this.popupService.callbackNext(this.popup.searchIndex, this.popup.imageIndex);
        }
    }

    prevImage(): void {
        //this.popupService.setPreviousImage(this.popup.searchIndex, this.popup.imageIndex);
        if (this.popupService.callbackPrev) {
            this.popupService.callbackPrev(this.popup.searchIndex, this.popup.imageIndex);
        }
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

    confirmDialog(answer: boolean): void {
        this.dialogVisible = false;
        this.dialogType = undefined;
        this.popupService.returnDialog(answer);
    }

    likeImage(like:boolean): void{
        this.liked = !this.liked;
    }

    goToExamination() {
        this.router.navigate(['/image/'+this.popup.examinationID + '/' + this.popup.imageIndex]);
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