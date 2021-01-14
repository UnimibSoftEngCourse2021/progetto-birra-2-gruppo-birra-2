import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Tool} from '../model/Tool';
import {Actions, URLS} from '../common/URLS';


@Injectable()
export class ToolService implements GenericService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Tool[]> {
    return this.http.get<Tool[]>(URLS[Actions.GET_TOOLS].url);
  }


  delete(id: number): Observable<any> {
    const url = `${URLS[Actions.DELETE_TOOL].url}/${id}`;
    return this.http.delete<boolean>(url);
  }


    edit(toolDTO: Tool): Observable<Tool> {
      return this.http.put<Tool>(URLS[Actions.EDIT_TOOL].url, toolDTO);
  }

  save(tool: Tool): Observable<Tool> {
    return this.http.post<Tool>(URLS[Actions.SAVE_TOOL].url, tool);
  }

  getById(id: any): Observable<any> {
    return undefined;
  }


}
