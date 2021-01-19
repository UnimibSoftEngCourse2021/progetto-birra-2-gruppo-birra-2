import {Observable} from 'rxjs';
import {Response} from '../model/Response';

export interface GenericService {

  save(entity: any): Observable<Response>;

  edit(entity: any): Observable<Response>;

  delete(id: any): Observable<Response>;

  getAll(): Observable<Response>;

  getById(id: number): Observable<Response>;
}
