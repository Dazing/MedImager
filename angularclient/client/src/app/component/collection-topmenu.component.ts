import { Component, ViewChild, Input } from '@angular/core';

import { CollectionService } from '../service/collection.service';

@Component({
	selector: 'collection-topmenu',
	templateUrl: '../template/collection-topmenu.component.html'
})
export class CollectionTopMenu {
    @Input('name') name: string;
    @Input('description') description: string;

    
}