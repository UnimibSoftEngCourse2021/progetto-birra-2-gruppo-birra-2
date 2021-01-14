import {Component, OnInit} from '@angular/core';
import {Brewer} from '../../model/Brewer';
import {BrewTodayService} from '../../service/BrewTodayService';
import {ACTIONS, BrewToday, BrewTodayIngredient} from '../../model/BrewToday';

@Component({
  selector: 'app-ingredient',
  templateUrl: './brewtoday.component.html',
  styleUrls: ['./brewtoday.component.css']
})
export class BrewTodayComponent implements OnInit {

  brewToday: BrewToday;
  recipeIngredients: BrewTodayIngredient[] = [];
  actions = ACTIONS;

  currentUser = new Brewer();

  brewTodayService: BrewTodayService;

  constructor(brewTodayService: BrewTodayService) {
    this.brewTodayService = brewTodayService;
  }

  ngOnInit(): void {
      this.brewTodayService.get().subscribe(data => this.brewToday = data);
  }

  counter(i: number) {
    return new Array(i);
  }

  onSubmit(): void {
    console.log(this.brewToday);
    // TODO Create Brew
  }
}
