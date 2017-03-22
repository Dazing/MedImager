import { Component } from '@angular/core';

@Component({
  selector: 'collections-menu',
  templateUrl: '../template/collections-menu.component.html'
})

export class CollectionsMenuComponent {
  myCollections = [{name:"samling 1", id:"111111"}, {name:"samling 2", id:"222222"},{name:"samling 3", id:"3333"}];
  sharedCollections = [{name:"extern samling 1", id:"1232123"},{name:"extern samling 1", id:"1232123"}];
  inboxImages = [{name:"bild 1", id:"41233"},{name:"bild 2", id:"32644"},{name:"bild 3", id:"243546"}];

}