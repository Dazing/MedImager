import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent }   from './component/login.component';
import { SearchComponent }   from './component/search.component';
import { ImagePageComponent }   from './component/imagepage.component';

import { ServerUnreachable } from './component/server-unreachable.component';
import { NotFound }   from './component/not-found.component';


import { UserGuard }   from './guard/user.guard';

const routes: Routes = [
	{ path: '', redirectTo: '/login', pathMatch: 'full' },
	{ path: 'login', component: LoginComponent },
	{ path: 'search', component: SearchComponent },
	{ path: 'image', component: ImagePageComponent },

	{ path: 'serverunreachable', component: ServerUnreachable },
	{ path: '**',  component: NotFound }

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
