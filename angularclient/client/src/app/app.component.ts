import { Component } from '@angular/core';
import { SearchService } from './service/search.service';
import { ImagePageService } from './service/imagepage.service';
import { AdvancedFormService } from './service/advanced-form.service';
import { UserService } from './service/user.service';
import { UserGuard } from './guard/user.guard';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	providers: [SearchService, ImagePageService, UserService, UserGuard, AdvancedFormService]
})
export class AppComponent {
	
}
