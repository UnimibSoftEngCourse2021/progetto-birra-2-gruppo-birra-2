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

  isEdit = false;

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

  onSubmit(): void {
    if (this.isEdit) {
      this.ingredientService.edit(this.ingredient).subscribe(response => {
          this.router.navigate(['ingredient']);
        },
        error => {
          alert(error);
        });
    } else {
      this.ingredientService.save(this.ingredient).subscribe(response => {
        this.router.navigate(['ingredient']);
      });
    }
  }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private ingredientService: IngredientService) {
  }

  ngOnInit(): void {
    // tslint:disable-next-line:radix
    const ingredientId = Number.parseInt(this.route.snapshot.paramMap.get('id'));
    if (ingredientId) {
      this.isEdit = true;
      this.ingredientService.getById(ingredientId).subscribe(resp => {
          this.ingredient = resp.data;
        },
        error => {
          alert(error);
        });
    }
  }

  goBack(): void {
    this.location.back();
  }

}
