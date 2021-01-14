import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Tool} from '../../../model/Tool';
import {ToolService} from '../../../service/ToolService';


@Component({
  selector: 'app-form',
  templateUrl: './ToolForm.component.html',
  styleUrls: ['./ToolForm.component.css']
})
export class ToolFormComponent implements OnInit {

  tool: Tool = new Tool();
  tools: Tool[] = [];

  onSubmit(): void {
    console.log(this.tool);
    this.toolService.save(this.tool).subscribe(response => {

    });
  }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private toolService: ToolService) {
  }

  ngOnInit(): void {
    this.toolService.getAll().subscribe(resp => this.tools = resp);

  }

  goBack(): void {
    this.location.back();
  }

}
