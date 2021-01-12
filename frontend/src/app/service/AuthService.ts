import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Token} from '../model/Token';
import {Actions, URLS} from '../common/URLS';


@Injectable()
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(payload): Observable<Token> {
    return this.http.post<Token>(URLS[Actions.LOGIN].url, payload);
  }
}
