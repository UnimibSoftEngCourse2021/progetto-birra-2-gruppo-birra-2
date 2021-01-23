import {Component, OnInit} from '@angular/core';
import {IngredientService} from '../../service/IngredientService';
import {ACTIONS, COLUMNS, Ingredient} from '../../model/Ingredient';

@Component({
  selector: 'app-ingredient',
  templateUrl: './ingredient.component.html',
  styleUrls: ['./ingredient.component.css']
})
export class IngredientComponent implements OnInit {

  header = COLUMNS;
  ingredients: Ingredient[];
  actions = ACTIONS;

  ingredientService: IngredientService;

  loading: boolean;


  constructor(ingredientService: IngredientService) {
    this.ingredientService = ingredientService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.ingredientService.getAll().subscribe(resp => {
      this.ingredients = resp.data;
      this.loading = false;
    });
  }
}
