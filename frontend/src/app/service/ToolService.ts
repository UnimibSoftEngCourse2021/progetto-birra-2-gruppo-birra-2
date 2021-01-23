import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {GenericService} from './GenericService';
import {Tool} from '../model/Tool';
import {Actions, URLS} from '../common/URLS';
import {Response} from '../model/Response';


@Injectable()
export class ToolService implements GenericService {

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Response> {
    return this.http.get<Response>(URLS[Actions.GET_TOOLS].url);
  }

  getById(id: number): Observable<Response> {
    const url = `${URLS[Actions.GET_TOOL_BY_ID].url}/${id}`;
    return this.http.get<Response>(url);
  }

  delete(id: number): Observable<Response> {
    const url = `${URLS[Actions.DELETE_TOOL].url}/${id}`;
    return this.http.delete<Response>(url);
  }


  edit(toolDTO: Tool): Observable<Response> {
    const url = `${URLS[Actions.EDIT_TOOL].url}/${toolDTO.toolId}`;
    return this.http.put<Response>(url, toolDTO);
  }

  save(tool: Tool): Observable<Response> {
    return this.http.post<Response>(URLS[Actions.SAVE_TOOL].url, tool);
  }

}
