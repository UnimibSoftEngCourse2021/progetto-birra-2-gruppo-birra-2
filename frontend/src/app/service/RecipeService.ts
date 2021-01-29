import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Recipe} from '../model/Recipe';
import {Actions, URLS} from '../common/URLS';
import {Response} from '../model/Response';

@Injectable()
export class RecipeService implements GenericService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Response> {
    return this.http.get<Response>(URLS[Actions.GET_RECIPE].url);
  }

  getById(id: number): Observable<Response> {
    const url = `${URLS[Actions.GET_RECIPE_BY_ID].url}/${id}`;
    return this.http.get<Response>(url);
  }

  delete(id: number): Observable<Response> {
    const url = `${URLS[Actions.DELETE_RECIPE].url}/${id}`;
    return this.http.delete<Response>(url);
  }

  edit(recipe: Recipe): Observable<Response> {
    const url = `${URLS[Actions.EDIT_RECIPE].url}/${recipe.recipeId}`;
    return this.http.put<Response>(url, recipe);
  }

  save(recipe: Recipe): Observable<Response> {
    return this.http.post<Response>(URLS[Actions.SAVE_RECIPE].url, recipe);
  }

  getIngredientsByRecipeId(id: number): Observable<Response> {
    const url = `${URLS[Actions.GET_INGREDIENTS_BY_RECIPE].url}/${id}/ingredient`;
    return this.http.get<Response>(url);
  }

  getPublicRecipes(): Observable<Response> {
    const url = `${URLS[Actions.GET_PUBLIC_RECIPES].url}`;
    return this.http.get<Response>(url);
  }
}
