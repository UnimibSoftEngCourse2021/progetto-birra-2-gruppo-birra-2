import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Actions, URLS} from '../common/URLS';
import {Response} from '../model/Response';


@Injectable()
export class BrewTodayService {

  constructor(private http: HttpClient) {
  }

  get(): Observable<Response> {
    return this.http.get<Response>(URLS[Actions.BREW_TODAY].url);
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
