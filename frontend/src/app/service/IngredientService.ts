import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Recipe} from '../model/Recipe';
import {Actions, URLS} from '../common/URLS';
import {Ingredient} from '../model/Ingredient';

@Injectable()
export class IngredientService implements GenericService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Ingredient[]> {
    return this.http.get<Ingredient[]>(URLS[Actions.GET_INGREDIENT].url);
  }

  getById(id: number): Observable<Recipe> {
    return undefined;
  }

  delete(id: number): Observable<any> {
    return undefined;
  }

  edit(entity: any): Observable<any> {
    return undefined;
  }

  save(recipe: Recipe): Observable<Recipe> {

    return undefined;
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
