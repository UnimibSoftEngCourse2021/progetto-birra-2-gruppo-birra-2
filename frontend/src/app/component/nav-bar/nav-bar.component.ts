import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  username: string = undefined;

  constructor() {
    this.username = window.localStorage.getItem('username');
  }

  ngOnInit(): void {
  }
}
