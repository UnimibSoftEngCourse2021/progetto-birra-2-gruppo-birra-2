import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Actions, URLS} from '../common/URLS';
import {Response} from '../model/Response';
import {Brewer} from '../model/Brewer';

@Injectable()
export class BrewerService {

  constructor(private http: HttpClient) {
  }

  getByUsername(username: string): Observable<Response> {
    const url = `${URLS[Actions.GET_BREWER_BY_USERNAME].url}/${username}`;
    return this.http.get<Response>(url);
  }

  delete(username: string): Observable<Response> {
    const url = `${URLS[Actions.DELETE_BREWER].url}/${username}`;
    return this.http.delete<Response>(url);
  }

  edit(brewer: Brewer): Observable<Response> {
    const url = `${URLS[Actions.EDIT_BREWER].url}/${brewer.username}`;
    return this.http.put<Response>(url, brewer);
  }
}
