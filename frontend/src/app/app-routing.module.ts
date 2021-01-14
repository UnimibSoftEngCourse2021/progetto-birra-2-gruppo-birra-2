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

const routes: Routes = [
  {path: '', redirectTo: 'index', pathMatch: 'full'},
  {path: 'index', component: RecipeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'createRecipe', component: RecipeFormComponent},
  {path: 'ingredient', component: IngredientComponent},
  {path: 'createIngredient', component: IngredientFormComponent},
  {path: 'today', component: BrewTodayComponent},
  {path: 'createTool', component: ToolFormComponent},
  {path: 'tool', component: ToolComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
