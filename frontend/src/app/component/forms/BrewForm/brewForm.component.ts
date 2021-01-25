import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Brew} from '../../../model/Brew';
import {BrewService} from '../../../service/BrewService';
import {Ingredient} from '../../../model/Ingredient';
import {RecipeService} from '../../../service/RecipeService';
import {Recipe} from '../../../model/Recipe';
import {BrewerService} from '../../../service/BrewerService';
import {Brewer} from '../../../model/Brewer';
import {StorageService} from '../../../service/StorageService';

@Component({
  selector: 'app-form-brew',
  templateUrl: './brewForm.component.html',
  styleUrls: ['./brewForm.component.css']
})

export class BrewFormComponent implements OnInit {

  @Input()
  brewToday = false;

  @Input()
  brewQuantity: number;

  @Input()
  recipeId: number;

  brew: Brew = new Brew();
  ingredients: Ingredient[] = [];
  recipes: Recipe[] = [];
  brewer: Brewer = new Brewer();
  selectedRecipeId: number;
  subtractIngredient = false;
  loading: boolean;
  isEdit = false;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private brewService: BrewService,
    private brewerService: BrewerService,
    private storageService: StorageService,
    private recipeService: RecipeService) {
  }

  ngOnInit(): void {
    this.loading = true;

    // tslint:disable-next-line:radix
    const brewId = Number.parseInt(this.route.snapshot.paramMap.get('id'));

    if (brewId) {
      this.isEdit = true;
        this.brewService.getById(brewId).subscribe(resp => {
          this.brew = resp.data;
          this.selectedRecipeId = this.brew.recipe.recipeId;
          this.getIngredient();
          this.brewerService.getByUsername(localStorage.getItem('username')).subscribe(resp2 => {
            this.brewer = resp2.data;
          });
          this.loading = false;
        }, error => {
          alert(error.error.data);
          this.loading = false;
          this.router.navigate(['brew']);
        });
      } else {
        this.recipeService.getAll().subscribe(resp => {
          this.recipes = resp.data;
          this.brewerService.getByUsername(localStorage.getItem('username')).subscribe(resp2 => {
            this.brewer = resp2.data;
            this.brew.quantity = this.brewer.maxBrew;

            if (this.brewToday){
              this.selectedRecipeId = this.recipeId;
              this.brew.quantity = this.brewQuantity;
              this.getIngredient();
            }
          });
          this.loading = false;
        });
      }


  }


  setRecipe(): void {
    for (const r of this.recipes) {
      // tslint:disable-next-line:triple-equals
      if (r.recipeId == this.selectedRecipeId) {
        this.brew.recipe = r;
        break;
      }
    }
  }

  getIngredient(): void {
    this.loading = true;

    if (!this.isEdit) {
      this.setRecipe();
    }
    this.recipeService.getIngredientsByRecipeId(this.selectedRecipeId).subscribe(resp => {
      this.ingredients = resp.data;
      this.loading = false;
    });
  }

  getStorage(ingredientId: number): Ingredient {
    for (const ingredient of this.ingredients) {
      // tslint:disable-next-line:triple-equals
      if (ingredient.ingredientId == ingredientId) {
        return ingredient;
      }
    }
  }

  onSubmit(): void {

    if (this.brew.quantity > this.brewer.maxBrew) {
      alert('You can\'t produce more than you maximum capacity');
      return;
    }

    if (this.brew.quantity <= 0) {
      alert('Quantity to brew must be greater than zero');
      return;
    }

    if (this.brew.duration <= 0) {
      alert('Brew duration must be greater than zero');
      return;
    }

    if (!this.brew.recipe) {
      alert('Select a recipe first');
      return;
    }


    if (!this.isEdit) {
      for (const recipeIngredient of this.brew.recipe.ingredients) {
        if (this.brew.quantity / recipeIngredient.quantity > this.getStorage(recipeIngredient.ingredientId).quantity
            && this.subtractIngredient) {
          alert('The amount of ingredients in your storage are not enough to make this quantity of beer');
          return;
        }
      }

      this.brewService.save(this.brew).subscribe(response => {
          if (this.subtractIngredient) {
            for (const recipeIngredient of this.brew.recipe.ingredients) {
              this.storageService.decreaseStorage(this.getStorage(recipeIngredient.ingredientId),
                this.brew.quantity / recipeIngredient.quantity).subscribe(resp2 => {
                this.router.navigate(['brew']);
              }, error => {
                alert(error.error.data);
              });
            }
          } else {
            this.router.navigate(['brew']);
          }
        },
        error => {
          alert(error.error.data);
        });
    } else {
      this.brewService.edit(this.brew).subscribe(response => {
        this.router.navigate(['brew']);
      }, error => {
        alert(error.error.data);
      });
    }
  }

  goBack(): void {
    if (!this.brewToday){
      this.location.back();
    } else {
      window.location.reload();
    }

  }
}
