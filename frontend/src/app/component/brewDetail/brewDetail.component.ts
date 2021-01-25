import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Ingredient} from '../../model/Ingredient';
import {Location} from '@angular/common';
import {Brew} from '../../model/Brew';
import {BrewService} from '../../service/BrewService';
import {RecipeService} from '../../service/RecipeService';

@Component({
  selector: 'app-brew-detail',
  templateUrl: './brewDetail.component.html',
  styleUrls: ['./brewDetail.component.css']
})
export class BrewDetailComponent implements OnInit {

  brewId: number;
  brew: Brew = new Brew();
  ingredients: Ingredient[] = [];
  loading: boolean;

  constructor(private brewService: BrewService,
              private recipeService: RecipeService,
              private route: ActivatedRoute,
              private location: Location,
              private router: Router) {
  }

  ngOnInit(): void {

    this.loading = true;
    // tslint:disable-next-line:radix
    this.brewId = Number.parseInt(this.route.snapshot.paramMap.get('id'));
    this.brewService.getById(this.brewId).subscribe(resp => {
      this.brew = resp.data;
      this.recipeService.getIngredientsByRecipeId(this.brew.recipe.recipeId).subscribe(resp2 => {
        this.ingredients = resp2.data;
        this.loading = false;
      });
    });
  }

  goBack(): void {
    this.location.back();
  }

  edit(): void {
    this.router.navigateByUrl('/brewForm/' + this.brew.brewId).then();
  }
}
