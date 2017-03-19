import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'
import { HttpModule } from '@angular/http';
import { RouterModule }   from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { SearchComponent } from './search/search.component';
import { ThumbnailComponent } from './search/thumbnail.component';
import { AppRoutingModule } from './app-routing.module';

@NgModule({
	declarations: [
		AppComponent,
		LoginComponent,
		SearchComponent,
		ThumbnailComponent
	],
	imports: [
		BrowserModule,
		FormsModule,
		HttpModule,
		AppRoutingModule,
		ReactiveFormsModule
	],
	providers: [],
	bootstrap: [AppComponent]
})
export class AppModule { }
