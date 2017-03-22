import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({ 
    selector: '[draggableImage]' 
})

export class DraggableImageDirective {
    constructor(el: ElementRef) {
       el.nativeElement.setAttribute("draggable", true);
    }

    @HostListener('dragstart', ['$event']) onDragStart(event) {
        event.dataTransfer.setData("sourceId", event.target.getAttribute("data-id"));
        event.dataTransfer.setData("sourceType", "draggableImage");
        
        //DnD ghost images must be drawn on the document, draw a styled (resized) clone thumbnail to assign as DnD ghost:
        var clone = event.target.cloneNode(true);
        clone.style.width = "50px";
        clone.style.height = "35px";
        clone.style.position = "fixed";
        clone.style.top = "0";
        clone.style.left = "-75px";//draw on a fixed position outside visible area
        clone.style.zIndex = event.target.style.zIndex - 1; //make sure clone is drawn behind original image
        event.target.appendChild(clone);
        event.dataTransfer.setDragImage(clone, 0, 0);

        window.setTimeout(function() { //delete clone after 1 second
            // ghost image has been snapshotted, so deleting before it has been dropped will not cause it to disappear
            clone.parentNode.removeChild(clone);
        }, 1000);
    }
}