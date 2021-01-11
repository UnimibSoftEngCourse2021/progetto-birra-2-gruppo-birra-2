import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RecipeComponent} from './component/recipe/recipe.component';
import {LoginComponent} from './component/login/login.component';
import {RecipeFormComponent} from './component/forms/RecipeForm/RecipeForm.component';


const routes: Routes = [
  {path: '', redirectTo: 'index', pathMatch: 'full'},
  {path: 'index', component: RecipeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'createRecipe', component: RecipeFormComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
