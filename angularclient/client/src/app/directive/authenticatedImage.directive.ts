import { Directive, ElementRef, HostListener, Input } from '@angular/core';
import { Response } from '@angular/http';

import { SearchService } from '../service/search.service';

@Directive({ 
    selector: '[authenticatedImage]' 
})

export class AuthenticatedImageDirective {
    @Input('authenticatedImage') src: string = '';

    constructor(
        private el: ElementRef, 
        private searchService: SearchService
        
    ) {
        el.nativeElement.setAttribute("src", '');
        if (!/[0-9]+\/[0-9]+/.test(this.src)) {
            console.warn('WARNING: authenticatedImage directive was not supplied source, or was supplied an invalid source. authenticatedImage takes arguments on form n+/n+');
        } else {
            this.getImage();
        }
    }

    private getImage(): void {
        this.searchService.getImage(this.src, url => {
            this.el.nativeElement.setAttribute('src', url);
        });
    }
  
}