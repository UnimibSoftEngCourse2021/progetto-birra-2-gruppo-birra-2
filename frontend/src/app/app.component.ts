import {Component, OnInit} from '@angular/core';
import {NavigationStart, Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Brew Day!';
  navBarVisible = false;

  constructor(private router: Router) {
  }

  ngOnInit(): void {

    this.router.events.subscribe((event) => {

      if (this.router.getCurrentNavigation().extractedUrl.toString() === '/register') {
        return;
      }

      if (this.router.getCurrentNavigation().extractedUrl.toString() !== '/login') {
        if (event instanceof NavigationStart) {
          if (!window.localStorage.getItem('token') || !window.localStorage.getItem('username')) {
            this.navBarVisible = false;
            this.router.navigate(['login']);
            return;
          } else {
            this.navBarVisible = true;
          }
        }
      }
    });
  }
}
