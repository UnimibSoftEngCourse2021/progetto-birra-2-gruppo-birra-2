import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Tool} from '../model/Tool';
import {Actions, URLS} from '../common/URLS';


export class ToolService  {

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
    return undefined;
  }

  save(tool: Tool): Observable<Tool> {
    return this.http.post<Tool>(URLS[Actions.SAVE_TOOL].url, tool);
  }


}
