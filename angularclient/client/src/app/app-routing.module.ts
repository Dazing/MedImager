import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent }   from './component/login.component';
import { SearchComponent }   from './component/search.component';
import { ImagePageComponent }   from './component/imagepage.component';
import { CollectionComponent }   from './component/collection.component';

import { ServerUnreachable } from './component/server-unreachable.component';
import { NotFound }   from './component/not-found.component';


import { UserGuard }   from './guard/user.guard';

const routes: Routes = [
	{ path: '', component: LoginComponent },
	{ path: 'search', canActivate: [UserGuard], component: SearchComponent },
	//{ path: 'image', component: ImagePageComponent },
	{ path: 'image/:imagepageexaminationid/:imagepageimageindex', canActivate: [UserGuard], component: SearchComponent },
	{ path: 'collection', canActivate: [UserGuard], component: CollectionComponent },

	{ path: 'serverunreachable',canActivate: [UserGuard], component: ServerUnreachable },
	{ path: '**', canActivate: [UserGuard],  component: NotFound }

];

@NgModule({
	providers: [UserGuard],
	imports: [ RouterModule.forRoot(routes) ],
	exports: [ RouterModule ]
})
export class AppRoutingModule {}


/*
Copyright 2017 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/
