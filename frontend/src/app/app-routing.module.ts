import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RecipeComponent} from './component/recipe/recipe.component';
import {LoginComponent} from './component/login/login.component';
import {RecipeFormComponent} from './component/forms/RecipeForm/RecipeForm.component';
import {IngredientComponent} from './component/ingredient/ingredient.component';
import {IngredientFormComponent} from './component/forms/IngredientForm/IngredientForm.component';
import {BrewTodayComponent} from './component/brewtoday/brewtoday.component';
import {ToolComponent} from './component/tool/tool.component';
import {ToolFormComponent} from './component/forms/ToolForm/ToolForm.component';
import {HomeComponent} from './component/home/home.component';
import {RegistrationFormComponent} from './component/forms/RegistrationForm/RegistrationForm.component';
import {RecipeDetailComponent} from './component/recipeDetail/recipeDetail.component';
import {BrewerFormComponent} from './component/forms/BrewerForm/brewerForm.component';
import {IngredientDetailComponent} from './component/ingredientDetail/ingredientDetail.component';

const routes: Routes = [
  {path: '', redirectTo: 'index', pathMatch: 'full'},
  {path: 'index', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegistrationFormComponent},
  {path: 'recipe', component: RecipeComponent},
  {path: 'recipeForm', component: RecipeFormComponent},
  {path: 'recipeForm/:id', component: RecipeFormComponent},
  {path: 'recipe/:id', component: RecipeDetailComponent},
  {path: 'ingredient/:id', component: IngredientDetailComponent},
  {path: 'ingredient', component: IngredientComponent},
  {path: 'ingredientForm', component: IngredientFormComponent},
  {path: 'ingredientForm/:id', component: IngredientFormComponent},
  {path: 'today', component: BrewTodayComponent},
  {path: 'toolForm', component: ToolFormComponent},
  {path: 'toolForm/:id', component: ToolFormComponent},
  {path: 'tool', component: ToolComponent},
  {path: 'brewerForm', component: BrewerFormComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
