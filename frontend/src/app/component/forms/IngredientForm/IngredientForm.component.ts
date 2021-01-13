import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Ingredient} from '../../../model/Ingredient';
import {IngredientService} from '../../../service/IngredientService';


@Component({
  selector: 'app-form',
  templateUrl: './IngredientForm.component.html',
  styleUrls: ['./IngredientForm.component.css']
})

export class IngredientFormComponent implements OnInit {

  ingredient: Ingredient = new Ingredient();
  ingredientType = [
    'MALT',
    'HOP',
    'YEAST',
    'SUGAR',
    'ADDITIVE',
    'WATER',
    'OTHER'
  ];

  onSubmit(): void{
    console.log(this.ingredient);
    this.ingredientService.save(this.ingredient).subscribe(response => {

    });
  }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private ingredientService: IngredientService) {
  }

  ngOnInit(): void {
    return;
  }

  goBack(): void {
    this.location.back();
  }

}
