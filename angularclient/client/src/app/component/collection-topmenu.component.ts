import { Component, ViewChild, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs/Subject';

import { Image, Collection } from '../model/image';
import { CollectionService } from '../service/collection.service';
import { PopupService } from '../service/popup.service';

@Component({
	selector: 'collection-topmenu',
	templateUrl: '../template/collection-topmenu.component.html'
})
export class CollectionTopMenu {
    private collection: Collection;
    private collectionSet = false;

    public collectionDeleted: Observable<number>;
    private privCollectionDeleted: Subject<number>;

    private detailsVisible: boolean = false;
    private editMode = false;

    constructor(private popupService: PopupService, private collectionService: CollectionService) {
        this.privCollectionDeleted = new Subject<number>();
        this.collectionDeleted = this.privCollectionDeleted.asObservable();
    }

    

    clickDetailsButton(event): void {
        if ( (" " + event.target.className + " ").replace(/[\n\t]/g, " ").indexOf(" collection-topmenu-details-button ") > -1 ) {
            this.detailsVisible = !this.detailsVisible;
        }
    }

    setCollection(coll: Collection): void {
        this.resetMenu();
        this.collection = coll;
        console.log("coll:" + coll);
        this.collectionSet = true;
    }

    deleteCollectionDialog(): void {
        this.popupService.collectionDeletionDialog().subscribe(answer => {
            if (answer) {
                this.collectionService.removeCollection(this.collection.id);
                this.privCollectionDeleted.next(this.collection.id);
                this.collectionSet = false;
            }
        });
    }


    resetMenu(): void {
        this.editMode = false;
        this.detailsVisible = false;
    }

    descriptionNotNull(): boolean {
        return !(this.collection.description == undefined || this.collection.description.replace(/ /g,"") == "");
    }

    acceptEdit(): void {
        console.log("TODO: edit collection description to:");
        let textarea: any = document.getElementById("description-edit-input");
        let text = textarea.value.replace(/\n\s*\n/g, '\n');//replace multiple consecutive line breaks, and remove lines containing only spaces
        textarea.value = "";
        console.log(text);
        this.editMode = false;
    }
    
    cancelEdit(): void {
        this.editMode = false;
    }
    
}