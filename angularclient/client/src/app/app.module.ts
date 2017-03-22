import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'
import { HttpModule } from '@angular/http';
import { RouterModule }   from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './component/login.component';
import { TopMenuComponent } from './component/topmenu.component';
import { FooterComponent } from './component/footer.component';
import { SearchComponent } from './component/search.component';
import { ThumbnailComponent } from './component/thumbnail.component';
import { ImagePageComponent } from './component/imagepage.component';
import { CollectionsMenuComponent } from './component/collections-menu.component';
import { AppRoutingModule } from './app-routing.module';

import { SearchService } from './service/search.service';

import { DraggableImageDirective } from './directive/draggableImage.directive';

@NgModule({
	declarations: [
		AppComponent,
		LoginComponent,
		TopMenuComponent,
		FooterComponent,
		SearchComponent,
		ThumbnailComponent,
		ImagePageComponent,
		CollectionsMenuComponent,
		DraggableImageDirective,
		
	],
	imports: [
		BrowserModule,
		FormsModule,
		HttpModule,
		AppRoutingModule,
		ReactiveFormsModule
	],
	providers: [SearchService],
	bootstrap: [AppComponent]
})
export class AppModule {

	constructor(private searchService: SearchService){}

}
