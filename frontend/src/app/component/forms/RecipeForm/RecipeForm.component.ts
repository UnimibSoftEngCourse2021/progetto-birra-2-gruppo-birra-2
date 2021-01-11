import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Recipe} from '../../../model/Recipe';
import {RecipeService} from '../../../service/RecipeService';
import {Ingredient} from '../../../model/Ingredient';
import {IngredientService} from '../../../service/IngredientService';
import {RecipeIngredient} from '../../../model/RecipeIngredient';

@Component({
  selector: 'app-form',
  templateUrl: './RecipeForm.component.html',
  styleUrls: ['./RecipeForm.component.css']
})
export class RecipeFormComponent implements OnInit {

  recipe: Recipe = new Recipe();
  ingredients: Ingredient[] = [];
  recipeIngredients: RecipeIngredient[] = [];

  onSubmit(): void {
    console.log(this.recipe);
    this.recipeService.save(this.recipe).subscribe(response => {

    });
  }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private recipeService: RecipeService,
    private ingredientService: IngredientService) {
  }

  ngOnInit(): void {
    this.ingredientService.getAll().subscribe(resp => this.ingredients = resp);
    const recipeIngredient: RecipeIngredient = new RecipeIngredient();
    this.recipeIngredients.push(recipeIngredient);
  }

  goBack(): void {
    this.location.back();
  }

  addIngredient(recipeIngredient: RecipeIngredient): void {
    this.recipeIngredients.push(recipeIngredient);
    const recipeIngredientEmpty: RecipeIngredient = new RecipeIngredient();
    this.recipeIngredients.push(recipeIngredientEmpty);
  }
}
