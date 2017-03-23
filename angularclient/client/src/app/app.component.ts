import { Component } from '@angular/core';
import { SearchService } from './service/search.service';
import { UserService } from './service/user.service';
import { UserGuard } from './guard/user.guard';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	providers: [SearchService, UserService, UserGuard]
})
export class AppComponent {
	
}
