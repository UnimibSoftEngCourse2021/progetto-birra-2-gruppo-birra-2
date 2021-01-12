import {Component, OnInit} from '@angular/core';
import {RecipeService} from '../../service/RecipeService';
import {ACTIONS, COLUMNS, Recipe} from '../../model/Recipe';
import {Brewer} from '../../model/Brewer';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.css']
})
export class RecipeComponent implements OnInit {

  header = COLUMNS;
  recipes: Recipe[];
  actions = ACTIONS;

  currentUser = new Brewer();

  recipeService: RecipeService;

  constructor(recipeService: RecipeService) {
    this.recipeService = recipeService;
  }

  ngOnInit(): void {
    this.recipeService.getAll().subscribe(data => this.recipes = data);
  }
}
