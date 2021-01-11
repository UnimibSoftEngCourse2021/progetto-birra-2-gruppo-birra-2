import {Component, Input, OnInit} from '@angular/core';
import {Brewer} from '../../model/Brewer';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  @Input()
  user: Brewer;

  ngOnInit(): void {
  }
}
