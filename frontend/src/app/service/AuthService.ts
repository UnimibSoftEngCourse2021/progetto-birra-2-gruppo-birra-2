import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Token} from '../model/Token';
import {Actions, URLS} from '../common/URLS';
import {Brewer} from '../model/Brewer';
import {Response} from '../model/Response';


@Injectable()
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(payload): Observable<Token> {
    return this.http.post<Token>(URLS[Actions.LOGIN].url, payload);
  }

  registration(brewer: Brewer): Observable<Response> {
    return this.http.post<Response>(URLS[Actions.REGISTRATION].url, brewer);
  }

  userExist(username: string): Observable<boolean> {
    const url = URLS[Actions.USER_EXIST].url + '/' + username + '/';
    return this.http.get<boolean>(url);
  }
}
