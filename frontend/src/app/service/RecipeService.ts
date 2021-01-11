import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Recipe} from '../model/Recipe';
import {Actions, URLS} from '../common/URLS';
import {RecipeIngredient} from '../model/RecipeIngredient';

@Injectable()
export class RecipeService implements GenericService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Recipe[]> {
    return this.http.get<Recipe[]>(URLS[Actions.GET_RECIPE].url);
  }

  getById(id: number): Observable<Recipe> {
    const url = `${URLS[Actions.GET_RECIPE_BY_ID].url}/${id}`;
    return this.http.get<Recipe>(url);
  }

  delete(id: number): Observable<any> {
    const url = `${URLS[Actions.DELETE_RECIPE].url}/${id}`;
    return this.http.delete<boolean>(url);
  }

  edit(entity: any): Observable<any> {
    return undefined;
  }

  save(recipe: Recipe): Observable<Recipe> {

    const recipeIngredient: RecipeIngredient = new RecipeIngredient();

    recipeIngredient.ingredientId = 4;
    recipeIngredient.quantity = 2;

    recipe.ingredients = [recipeIngredient];

    return this.http.post<Recipe>(URLS[Actions.SAVE_RECIPE].url, recipe);
  }

  /*

    save(recipe: Recipe): Observable<Recipe> {
      return this.http.post<Recipe>(URLS[Actions.SAVE].url, recipe);
    }
    edit(employeeDTO: Recipe): Observable<Recipe> {
      return this.http.put<Recipe>(URLS[Actions.EDIT].url, employeeDTO);
    }

    delete(id: number): Observable<boolean> {
      const url = `${URLS[Actions.DELETE].url}/${id}`;
      return this.http.delete<boolean>(url);
    }

    find(query: string): Observable<Recipe[]> {
      // const url = URLS[Actions.GET_EMPLOYEE].url + '/' + query;
      const url = URLS[Actions.GET_EMPLOYEE].url;
      return this.http.get<Recipe[]>(url);
    }

    userExist(username: string): Observable<boolean> {
      const url = URLS[Actions.USER_EXIST].url + '/' + username + '/';
      return this.http.get<boolean>(url);
    }

    changePassword(payload): Observable<any> {
      return this.http.post<any>(URLS[Actions.CHANGE_PASSWORD].url, payload);
    }

   */
}
