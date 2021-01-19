import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Actions, URLS} from '../common/URLS';
import {Ingredient} from '../model/Ingredient';
import {Response} from '../model/Response';

@Injectable()
export class IngredientService implements GenericService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Response> {
    return this.http.get<Response>(URLS[Actions.GET_INGREDIENT].url);
  }

  getById(id: number): Observable<Response> {
    const url = `${URLS[Actions.GET_INGREDIENT_BY_ID].url}/${id}`;
    return this.http.get<Response>(url);
  }

  delete(id: number): Observable<Response> {
    const url = `${URLS[Actions.DELETE_INGREDIENT].url}/${id}`;
    return this.http.delete<Response>(url);
  }

  edit(entity: any): Observable<Response> {
    return undefined;
  }

  save(ingredient: Ingredient): Observable<Response> {

    return this.http.post<Response>(URLS[Actions.SAVE_INGREDIENT].url, ingredient);
  }



  /*

    save(recipe: Recipe): Observable<Recipe> {
      return this.http.post<Recipe>(URLS[Actions.SAVE].url, recipe);
    }
    edit(employeeDTO: Recipe): Observable<Recipe> {
      return this.http.put<Recipe>(URLS[Actions.EDIT].url, employeeDTO);
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
