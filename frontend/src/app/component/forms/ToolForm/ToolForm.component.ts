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

  isEdit = false;
  tool: Tool = new Tool();

  onSubmit(): void {
    if (this.isEdit) {
      this.toolService.edit(this.tool).subscribe(response => {
          this.router.navigate(['tool']);
        },
        error => {
          alert(error.error.data);
        });
    } else {
      this.toolService.save(this.tool).subscribe(response => {
          this.router.navigate(['tool']);
        },
        error => {
          alert(error.error.data);
        });
    }
  }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private toolService: ToolService) {
  }

  ngOnInit(): void {
    // tslint:disable-next-line:radix
    const toolId = Number.parseInt(this.route.snapshot.paramMap.get('id'));
    if (toolId) {
      this.isEdit = true;
      this.toolService.getById(toolId).subscribe(resp => {
          this.tool = resp.data;
        },
        error => {
          alert(error.error.data);
        });
    }
  }

  goBack(): void {
    this.location.back();
  }

}
