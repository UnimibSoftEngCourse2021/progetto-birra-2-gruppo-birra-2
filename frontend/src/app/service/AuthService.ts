import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Token} from '../model/Token';


@Injectable()
export class AuthService {

  constructor(private http: HttpClient) {
  }

  login(payload): Observable<Token> {
    return this.http.post<Token>('http://localhost:8080/login', payload);
  }
}
