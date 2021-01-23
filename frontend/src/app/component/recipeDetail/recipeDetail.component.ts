import {Component, OnInit} from '@angular/core';
import {RecipeService} from '../../service/RecipeService';
import {ActivatedRoute, Router} from '@angular/router';
import {Recipe} from '../../model/Recipe';
import {Ingredient} from '../../model/Ingredient';
import {Location} from '@angular/common';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipeDetail.component.html',
  styleUrls: ['./recipeDetail.component.css']
})
export class RecipeDetailComponent implements OnInit {

  recipeId: number;
  recipe: Recipe = new Recipe();
  ingredients: Ingredient[] = [];
  loading: boolean;

  constructor(private recipeService: RecipeService,
              private route: ActivatedRoute,
              private location: Location,
              private router: Router) {
  }

  ngOnInit(): void {

    this.loading = true;
    // tslint:disable-next-line:radix
    this.recipeId = Number.parseInt(this.route.snapshot.paramMap.get('id'));
    this.recipeService.getById(this.recipeId).subscribe(resp => {
      this.recipe = resp.data;

      this.recipeService.getIngredientsByRecipeId(this.recipe.recipeId).subscribe(resp2 => {
        this.ingredients = resp2.data;
        this.loading = false;
      });
    });
  }

  goBack(): void {
    this.location.back();
  }

  edit(): void {
    this.router.navigateByUrl('/recipeForm/' + this.recipe.recipeId).then();
  }
}
