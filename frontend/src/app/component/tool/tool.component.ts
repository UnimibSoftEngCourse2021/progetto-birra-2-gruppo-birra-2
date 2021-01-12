import {Component, OnInit} from '@angular/core';
import {ToolService} from '../../service/ToolService';
import {ACTIONS, COLUMNS, Tool} from '../../model/Tool';
import {Brewer} from '../../model/Brewer';

@Component({
  selector: 'app-tool',
  templateUrl: './tool.component.html',
  styleUrls: ['./tool.component.css']
})
export class ToolComponent implements OnInit {

  header = COLUMNS;
  tools: Tool[];
  actions = ACTIONS;

  currentUser = new Brewer();

  toolService: ToolService;

  constructor(toolService: ToolService) {
    this.toolService = toolService;
  }

  ngOnInit(): void {
    this.toolService.getAll().subscribe(data => this.tools = data);
  }
}
