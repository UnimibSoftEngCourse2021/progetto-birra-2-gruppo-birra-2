import {Component, OnInit} from '@angular/core';
import {Recipe} from '../../model/Recipe';
import {RecipeService} from '../../service/RecipeService';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  header =  [
    {
      label: 'Name',
      fieldName: 'name',
    },
    {
      label: 'Description',
      fieldName: 'description',
    },
    {
      label: 'Ingredients',
      fieldName: 'ingredientName',
      arrayField: 'ingredients'
    },
    {
      label: 'Owner',
      fieldName: 'username',
    }
  ];
  actions =  [
    {
      label: 'info',
      actionType: 'GO_TO',
      getUrl: row => '/recipe/' + row.recipeId,
    },
    {
      label: 'save',
      actionType: 'COPY_RECIPE',
      hoverText: 'save this recipe'
    }
  ];

  recipes: Recipe[];
  loading: boolean;

  recipeService: RecipeService;

  constructor(recipeService: RecipeService) {
    this.recipeService = recipeService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.recipeService.getPublicRecipes().subscribe(response => {
      this.recipes = response.data;
      this.loading = false;
    });
  }
}
