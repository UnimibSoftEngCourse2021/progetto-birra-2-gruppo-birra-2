import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Actions, URLS} from '../common/URLS';
import {Response} from '../model/Response';
import {Brew} from '../model/Brew';

@Injectable()
export class BrewService implements GenericService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Response> {
    return this.http.get<Response>(URLS[Actions.GET_BREW].url);
  }

  getById(id: number): Observable<Response> {
    const url = `${URLS[Actions.GET_BREW_BY_ID].url}/${id}`;
    return this.http.get<Response>(url);
  }

  delete(id: number): Observable<Response> {
    const url = `${URLS[Actions.DELETE_BREW].url}/${id}`;
    return this.http.delete<Response>(url);
  }

  edit(brew: Brew): Observable<Response> {
    const url = `${URLS[Actions.EDIT_BREW].url}/${brew.brewId}`;
    return this.http.put<Response>(url, brew);
  }

  save(brew: Brew): Observable<Response> {
    return this.http.post<Response>(URLS[Actions.SAVE_BREW].url, brew);
  }
}
