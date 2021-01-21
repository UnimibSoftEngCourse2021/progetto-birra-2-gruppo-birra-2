import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Ingredient} from '../../model/Ingredient';
import {Location} from '@angular/common';
import {IngredientService} from '../../service/IngredientService';

@Component({
  selector: 'app-ingredient-detail',
  templateUrl: './ingredientDetail.component.html',
  styleUrls: ['./ingredientDetail.component.css']
})
export class IngredientDetailComponent implements OnInit {

  ingredientId: number;
  ingredient: Ingredient = new Ingredient();

  constructor(private ingredientService: IngredientService,
              private route: ActivatedRoute,
              private location: Location,
              private router: Router) {
  }

  ngOnInit(): void {
    // tslint:disable-next-line:radix
    this.ingredientId = Number.parseInt(this.route.snapshot.paramMap.get('id'));
    this.ingredientService.getById(this.ingredientId).subscribe(resp => {
      this.ingredient = resp.data;
      });
    }

  goBack(): void {
    this.location.back();
  }

  edit(): void {
    // tslint:disable-next-line:no-unused-expression
    this.router.navigateByUrl('/ingredientForm/' + this.ingredient.ingredientId).then;
  }
}
