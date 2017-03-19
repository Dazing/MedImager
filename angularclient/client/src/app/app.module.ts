import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule }   from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { SearchComponent } from './search/search.component';
import { ThumbnailComponent } from './search/thumbnail.component';
import { CollectionsMenuComponent } from './search/collections-menu.component';
import { AppRoutingModule } from './app-routing.module';

import { DraggableImageDirective } from './search/draggableImage.directive';

@NgModule({
	declarations: [
		AppComponent,
		LoginComponent,
		SearchComponent,
		ThumbnailComponent,
		CollectionsMenuComponent,
		DraggableImageDirective
	],
	imports: [
		BrowserModule,
		FormsModule,
		HttpModule,
		AppRoutingModule
	],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule { }
