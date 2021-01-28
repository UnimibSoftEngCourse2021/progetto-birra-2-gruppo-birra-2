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

  isEdit = false;

  recipe: Recipe = new Recipe();
  ingredients: Ingredient[] = [];
  recipeIngredient: RecipeIngredient = new RecipeIngredient();
  recipeIngredients: RecipeIngredient[] = [];
  localTot = 0;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private recipeService: RecipeService,
    private ingredientService: IngredientService) {
  }

  ngOnInit(): void {
    this.ingredientService.getAll().subscribe(resp => this.ingredients = resp.data);
    // tslint:disable-next-line:radix
    const recipeId = Number.parseInt(this.route.snapshot.paramMap.get('id'));
    if (recipeId) {
      this.isEdit = true;
      this.recipeService.getById(recipeId).subscribe(resp => {
          this.recipe = resp.data;
          this.recipeIngredients = this.recipe.ingredients;
          for (const recipeIngredient of this.recipeIngredients){
            recipeIngredient.quantity = recipeIngredient.quantity * recipeIngredient.originalTotQuantity;
          }
          this.localTot = this.recipeIngredients[0].originalTotQuantity;
        },
        error => {
          alert(error);
        });
    }
  }

  onSubmit(): void {
    this.recipe.ingredients = this.recipeIngredients;

    if (this.isEdit) {
      this.recipeService.edit(this.recipe).subscribe(response => {
          this.router.navigate(['recipe']);
        },
        error => {
          alert('The recipe is not valid, make sure the ingredients are selected correctly.');
        });
    } else {
      this.recipeService.save(this.recipe).subscribe(response => {
          this.router.navigate(['recipe']);
        },
        error => {
          alert('The recipe is not valid, make sure the ingredients are selected correctly.');
        });
    }
  }

  goBack(): void {
    this.location.back();
  }

  getIngredientName(id: number): string {
    for (const ingredient of this.ingredients) {
      // tslint:disable-next-line:triple-equals
      if (ingredient.ingredientId == id) {
        return ingredient.name;
      }
    }
  }

  getIngredientUnit(id: number): string {
    for (const ingredient of this.ingredients) {
      // tslint:disable-next-line:triple-equals
      if (ingredient.ingredientId == id) {
        return ingredient.unit;
      }
    }
  }

  addIngredient(): void {
    this.localTot += this.recipeIngredient.quantity;
    this.recipeIngredients.push(this.recipeIngredient);
    this.recipeIngredient = new RecipeIngredient();
  }

  removeIngredient(recipeIngredient: RecipeIngredient): void {
    this.localTot = this.localTot - recipeIngredient.quantity;
    this.recipeIngredients.splice(this.recipeIngredients.indexOf(recipeIngredient), 1);
  }
}
