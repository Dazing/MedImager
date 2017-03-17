import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ApplyComponent }   from './apply.component';
import { ForumComponent }   from './forum.component';

const routes: Routes = [
  { path: '', redirectTo: '/', pathMatch: 'full' },
  { path: 'apply',  component: ApplyComponent },
  { path: 'forum',  component: ForumComponent }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}


/*
Copyright 2017 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/
