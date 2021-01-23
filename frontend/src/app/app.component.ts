import {Component} from '@angular/core';
import {NavigationStart, Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Brew Day!';
  navBarVisible: boolean;

  constructor(private router: Router) {

    router.events.subscribe((event) => {

      if (router.getCurrentNavigation().extractedUrl.toString() === '/register') {
        return;
      }

      if (router.getCurrentNavigation().extractedUrl.toString() !== '/login') {
        if (event instanceof NavigationStart) {
          if (!window.localStorage.getItem('token')) {
            this.router.navigate(['login']);
            return;
          } else {
            this.navBarVisible = true;
          }
        }
      } else {
        this.navBarVisible = false;
      }
    });
  }
}
