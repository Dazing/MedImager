import { Directive, ElementRef, HostListener, Input } from '@angular/core';

import { CollectionService } from '../service/collection.service';
import { AppModule } from '../app.module';

declare var Materialize: any;

@Directive({ 
    selector: '[droppableCollection]' 
})

export class DroppableCollectionDirective {
    @Input('droppableCollection') collectionId: number;
    constructor(private el: ElementRef, private collectionService: CollectionService) {
    }

    @HostListener('dragenter', ['$event']) handleDragEnter(event) {
        event.preventDefault();
	    return true;
    }

    @HostListener('dragover', ['$event']) handleDragOver(event) {
        event.preventDefault();
	    return false;
    }

    @HostListener('drop', ['$event']) handleDrop(event) {	
        event.preventDefault();
        var examinationID = event.dataTransfer.getData("examinationID");
        var imageIndex = event.dataTransfer.getData("imageIndex");
        var type = event.dataTransfer.getData("sourceType");
        if (type == "draggableImage") {
            this.collectionService.addImage(examinationID, imageIndex, this.collectionId, (success) => {
                if (success) {
                    Materialize.toast("Bild tillagd i samling", 4000, "tertiary-colour black-text");
                } else {
                    Materialize.toast("Bilden fanns redan i samlingen", 4000, "red white-text");
                }
            });
            
        }

    }
}