import {Component, OnInit} from '@angular/core';
import {IngredientService} from '../../service/IngredientService';
import {ACTIONS, COLUMNS, Ingredient} from '../../model/Ingredient';
import {Brewer} from '../../model/Brewer';

@Component({
  selector: 'app-ingredient',
  templateUrl: './ingredient.component.html',
  styleUrls: ['./ingredient.component.css']
})
export class IngredientComponent implements OnInit {

  header = COLUMNS;
  ingredients: Ingredient[];
  actions = ACTIONS;

  currentUser = new Brewer();

  ingredientService: IngredientService;

  constructor(ingredientService: IngredientService) {
    this.ingredientService = ingredientService;
  }

  ngOnInit(): void {
    this.ingredientService.getAll().subscribe(data => this.ingredients = data);
  }
}
