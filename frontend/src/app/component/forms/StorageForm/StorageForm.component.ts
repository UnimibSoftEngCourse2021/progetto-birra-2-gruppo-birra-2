import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Ingredient} from '../../../model/Ingredient';
import {StorageService} from '../../../service/StorageService';
import {IngredientService} from '../../../service/IngredientService';
import {Storage} from '../../../model/Storage';

@Component({
  selector: 'app-form',
  templateUrl: './StorageForm.component.html',
  styleUrls: ['./StorageForm.component.css']
})
export class StorageFormComponent implements OnInit {

  ingredients: Ingredient[] = [];
  ingredient: Storage = new Storage();

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private ingredientService: IngredientService,
    private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.ingredientService.getAll()
      .subscribe(resp => this.ingredients = resp.data);
  }

  onSubmit(): void {
    this.storageService.increaseStorage(this.ingredient, this.ingredient.quantity)
      .subscribe(() => {
        this.location.back();
      },
      error => alert(error));
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
}
