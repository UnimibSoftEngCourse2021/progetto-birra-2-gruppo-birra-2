import {Observable} from 'rxjs';

export interface GenericService {

  save(entity: any): Observable<any>;

  edit(entity: any): Observable<any>;

  delete(id: any): Observable<any>;

  getById(id: any): Observable<any>;

  getAll(): Observable<any[]>;

}
