import {Component, OnInit} from '@angular/core';
import {BrewTodayService} from '../../service/BrewTodayService';
import {ACTIONS, BrewToday} from '../../model/BrewToday';

@Component({
  selector: 'app-ingredient',
  templateUrl: './brewtoday.component.html',
  styleUrls: ['./brewtoday.component.css']
})
export class BrewTodayComponent implements OnInit {

  brewToday: BrewToday;
  actions = ACTIONS;

  loading: boolean;

  constructor(private brewTodayService: BrewTodayService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.brewTodayService.get().subscribe(response => {
      this.brewToday = response.data;
      this.loading = false;
    });
  }

  onSubmit(): void {
    console.log(this.brewToday);
    // TODO Create Brew
  }
}
