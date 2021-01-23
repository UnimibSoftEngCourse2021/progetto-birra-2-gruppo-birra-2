import {observable, Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Actions, URLS} from '../common/URLS';
import {Storage} from '../model/Storage';
import {Response} from '../model/Response';
import { throwError } from 'rxjs';

@Injectable()
export class StorageService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Response> {
    return this.http.get<Response>(URLS[Actions.GET_STORAGE].url);
  }

  getById(id: number): Observable<Response> {
    const url = `${URLS[Actions.GET_STORAGE_INGREDIENT].url}/${id}`;
    return this.http.get<Response>(url);
  }

  edit(ingredient: Storage): Observable<Response> {
    return this.http.put<Response>(URLS[Actions.EDIT_STORAGE].url, ingredient);
  }

  increaseStorage(ingredient: any, value: number): Observable<Response> {
    const url = `${URLS[Actions.EDIT_STORAGE].url}/${ingredient.ingredientId}`;
    if (value < 0) {
      return throwError(`The value has to be positive`);
    } else {
      ingredient.quantity = value;
    }
    return this.http.put<Response>(url, ingredient);
  }

  decreaseStorage(ingredient: Storage, value: number): Observable<Response> {
    const url = `${URLS[Actions.EDIT_STORAGE].url}/${ingredient.ingredientId}`;

    if (value > ingredient.quantity){
      return throwError(`You can't remove more than the amount you have in storage`);
    }

    if (ingredient.quantity > 0) {
      ingredient.quantity = -value;
    } else {
      ingredient.quantity = value;
    }
    return this.http.put<Response>(url, ingredient);
  }

  /*
   This method passes passes the negative value of the ingredient's stored quantity
   so that the new quantity will be zero
  */
  delete(ingredient: Storage): Observable<Response> {
    const url = `${URLS[Actions.EDIT_STORAGE].url}/${ingredient.ingredientId}`;
    ingredient.quantity = -ingredient.quantity;
    return this.http.put<Response>(url, ingredient);
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
