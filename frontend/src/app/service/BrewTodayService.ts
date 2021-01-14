import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Actions, URLS} from '../common/URLS';
import {BrewToday} from '../model/BrewToday';

@Injectable()
export class BrewTodayService {

  constructor(private http: HttpClient) {
  }

  get(): Observable<any> {
    return this.http.get<BrewToday>(URLS[Actions.BREW_TODAY].url);
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
