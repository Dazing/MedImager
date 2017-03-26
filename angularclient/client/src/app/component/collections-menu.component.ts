import { Component } from '@angular/core';

@Component({
  selector: 'collections-menu',
  templateUrl: '../template/collections-menu.component.html'
})

export class CollectionsMenuComponent {
  myCollections = [{name:"Tandsten genom tiderna", id:"111111"}, {name:"Karies och baktus", id:"222222"},{name:"Bland tomtar och tandtroll", id:"3333"}];
  sharedCollections = [{name:"extern samling 1", id:"1232123"},{name:"extern samling 1", id:"1232123"}];

}