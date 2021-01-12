import {Component, Input, OnInit} from '@angular/core';
import * as _ from 'lodash';
import {Router} from '@angular/router';
import {GenericService} from '../../service/GenericService';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./style.scss']
})
export class TableComponent implements OnInit {

  @Input()
  headers: string;

  @Input()
  data: any[];

  @Input()
  indexField: string;

  @Input()
  elementToDisplay: number[] = [2, 5, 10, 25, 50];

  @Input()
  actions = [];

  @Input()
  genericService: GenericService;

  @Input()
  currentUser;

  @Input()
  imageUrlAPI: string;

  @Input()
  formUrl: string;

  field = 'All';

  numOfElement: number;
  sortIcon: string;
  sorting = 'desc';
  selectedHeader = '';

  searchText = '';

  constructor(private router: Router) {
  }

  onSelect(selected: string): void {

    this.selectedHeader = selected;

    if (this.sorting === 'asc') {
      this.sortIcon = 'keyboard_arrow_down';
      this.sorting = 'des';
      this.data = _.sortBy(this.data, selected);
    } else {
      this.sortIcon = 'keyboard_arrow_up';
      this.sorting = 'asc';
      this.data = _.sortBy(this.data, selected).reverse();
    }
  }

  pagination(selected: number): void {
    console.log(selected);
    this.numOfElement = selected;
  }

  ngOnInit(): void {
    this.elementToDisplay = _.sortBy(this.elementToDisplay);
    this.numOfElement = this.elementToDisplay[this.elementToDisplay.length - 1];
  }

  getRowParameter(col, row) {
    if (col.arrayField) {
      let result = '';
      _.get(row, col.arrayField).forEach((element) => result += _.get(element, col.fieldName) + ',');
      return result;
    }
    return _.get(row, col.fieldName);
  }

  actionFunc(action, element: any): void {
    if (action.actionType === 'DELETE') {

      const msg = 'delete?';

      if (confirm(msg) === true) {
        this.genericService.delete(element[this.indexField])
          .subscribe(() => location.reload(),
            error => console.log(error));
      }
    }

    if (action.actionType === 'GO_TO') {
      const url = action.getUrl(element);
      this.router.navigateByUrl(url).then();
    }
  }
}
