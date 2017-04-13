
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder } from '@angular/forms'
import { HttpModule } from '@angular/http';
import { RouterModule }   from '@angular/router';

import { AppComponent } from './app.component';
import { LoginComponent } from './component/login.component';
import { TopMenuComponent } from './component/topmenu.component';
import { CollectionTopMenu } from './component/collection-topmenu.component';
import { FooterComponent } from './component/footer.component';
import { SearchComponent } from './component/search.component';
import { ThumbnailComponent } from './component/thumbnail.component';
import { CollectionThumbnailComponent } from './component/collection-thumbnail.component';
import { ImagePageComponent } from './component/imagepage.component';
import { CollectionsMenu } from './component/collections-menu.component';
import { CollectionComponent } from './component/collection.component';
import { NotFound } from './component/not-found.component';
import { ServerUnreachable } from './component/server-unreachable.component';
import { PopupComponent } from './component/popup.component';
import { AdvancedFormComponent } from './component/advanced-form.component';

import { AppRoutingModule } from './app-routing.module';

import { SearchService } from './service/search.service';
import { ImagePageService } from './service/imagepage.service';
import { PopupService } from './service/popup.service';
import { CollectionService } from './service/collection.service';
import { UserService } from './service/user.service';

import { Server } from './model/server'

import { DraggableImageDirective } from './directive/draggableImage.directive';
import { DroppableCollectionDirective } from './directive/droppableCollection.directive';

import { SearchCompletion } from './pipe/search-completion.pipe';

@NgModule({
	declarations: [
		AppComponent,
		LoginComponent,
		TopMenuComponent,
		CollectionTopMenu,
		FooterComponent,
		SearchComponent,
		ThumbnailComponent,
		CollectionThumbnailComponent,
		ImagePageComponent,
		CollectionsMenu,
		CollectionComponent,
		PopupComponent,
		AdvancedFormComponent,
		NotFound,
		ServerUnreachable,
		DraggableImageDirective,
		DroppableCollectionDirective,
		SearchCompletion
	],
	imports: [
		BrowserModule,
		FormsModule,
		HttpModule,
		AppRoutingModule,
		ReactiveFormsModule
	],
	providers: [SearchService, ImagePageService, PopupService, CollectionService, UserService, Server],
	bootstrap: [AppComponent]
})
export class AppModule {

	constructor(
		private searchService: SearchService, 
		private imagePageService: ImagePageService, 
		private popupService: PopupService,
		private collectionService: CollectionService,
		private userService: UserService,
		private server: Server
	) {

	}

}
