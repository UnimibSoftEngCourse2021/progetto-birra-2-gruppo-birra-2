import {Component, OnInit} from '@angular/core';
import {IngredientService} from '../../service/IngredientService';
import {ACTIONS, COLUMNS, Ingredient} from '../../model/Ingredient';
import {StorageService} from '../../service/StorageService';

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
  storageService: StorageService;

  loading: boolean;



  constructor(ingredientService: IngredientService, storageService: StorageService) {
    this.ingredientService = ingredientService;
    this.storageService = storageService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.ingredientService.getAll().subscribe(resp => {
      this.ingredients = resp.data;
      this.loading = false;
    });
  }
}
