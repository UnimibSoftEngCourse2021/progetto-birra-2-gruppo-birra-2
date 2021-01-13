import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RecipeComponent} from './component/recipe/recipe.component';
import {LoginComponent} from './component/login/login.component';
import {RecipeFormComponent} from './component/forms/RecipeForm/RecipeForm.component';
import {IngredientComponent} from './component/ingredient/ingredient.component';
import {IngredientFormComponent} from './component/forms/IngredientForm/IngredientForm.component';


const routes: Routes = [
  {path: '', redirectTo: 'index', pathMatch: 'full'},
  {path: 'index', component: RecipeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'createRecipe', component: RecipeFormComponent},
  {path: 'ingredient', component: IngredientComponent},
  {path: 'createIngredient', component: IngredientFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
