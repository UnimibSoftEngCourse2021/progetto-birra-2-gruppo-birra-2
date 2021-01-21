import {Component, OnInit} from '@angular/core';
import {ToolService} from '../../service/ToolService';
import {ACTIONS, COLUMNS, Tool} from '../../model/Tool';

@Component({
  selector: 'app-tool',
  templateUrl: './tool.component.html',
  styleUrls: ['./tool.component.css']
})
export class ToolComponent implements OnInit {

  header = COLUMNS;
  tools: Tool[];
  actions = ACTIONS;

  loading: boolean;

  toolService: ToolService;

  constructor(toolService: ToolService) {
    this.toolService = toolService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.toolService.getAll().subscribe(resp => {
      this.tools = resp.data;
      this.loading = false;
    });
  }
}
