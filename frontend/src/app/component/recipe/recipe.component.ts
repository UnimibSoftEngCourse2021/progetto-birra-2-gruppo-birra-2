import {Component, OnInit} from '@angular/core';
import {RecipeService} from '../../service/RecipeService';
import {ACTIONS, COLUMNS, Recipe} from '../../model/Recipe';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.css']
})
export class RecipeComponent implements OnInit {

  header = COLUMNS;
  recipes: Recipe[];
  actions = ACTIONS;
  brew = false;

  recipeService: RecipeService;

  loading: boolean;

  constructor(recipeService: RecipeService) {
    this.recipeService = recipeService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.recipeService.getAll().subscribe(resp => {
      this.recipes = resp.data;
      this.loading = false;
    });
  }
}
