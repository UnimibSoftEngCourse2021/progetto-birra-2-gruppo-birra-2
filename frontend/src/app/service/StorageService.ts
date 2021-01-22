import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Actions, URLS} from '../common/URLS';
import {Storage} from '../model/Storage';
import {Response} from '../model/Response';

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

  delete(ingredient: Storage): Observable<Response> {
    const url = `${URLS[Actions.EDIT_STORAGE].url}/${ingredient.ingredientId}`;
    ingredient.quantity = 0;
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
