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
  createBrew = false;
  brewQuantity = null;
  actions = ACTIONS;

  loading: boolean;

  constructor(private brewTodayService: BrewTodayService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.brewTodayService.get().subscribe(response => {
        this.brewToday = response.data;
        // tslint:disable-next-line:forin
        for (const index in this.brewToday.ingredientQuantities){
          this.brewQuantity += this.brewToday.ingredientQuantities[index].quantity;
        }
        this.loading = false;
      },
      error => {
        this.loading = false;
      });
  }

  brew(): void {
    this.createBrew = true;
    // TODO Pass the recipe and create Brew
  }
}
