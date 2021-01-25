import {Component, Input, OnInit} from '@angular/core';
import * as _ from 'lodash';
import {Router} from '@angular/router';
import {GenericService} from '../../service/GenericService';
import {StorageService} from '../../service/StorageService';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./style.scss']
})
export class TableComponent implements OnInit {

  @Input()
  headers: any;

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
  storageService: StorageService;

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

  getRowParameter(col, row): any {
    if (col.arrayField) {
      if (_.get(row, col.arrayField).length > 2) {
        let result = '';
        result += _.get(_.get(row, col.arrayField)[0], col.fieldName) + ',';
        result += _.get(_.get(row, col.arrayField)[1], col.fieldName) + ' and ' + (_.get(row, col.arrayField).length - 2) + ' more';
        return result;
      } else {
        let result = '';
        _.get(row, col.arrayField).forEach((element) => result += _.get(element, col.fieldName) + ',');
        return result;
      }
    }
    return _.get(row, col.fieldName);
  }

  actionFunc(action, element: any): void {
    if (action.actionType === 'DELETE') {

      const msg = 'delete?';

      if (confirm(msg) === true) {
        this.genericService.delete(element[this.indexField])
          .subscribe(() => {
              location.reload();
            },
            error => console.log(error));
      }
    }

    if (action.actionType === 'DELETE_FROM_STORAGE') {

      const msg = 'delete the ingredient from storage?';

      if (confirm(msg) === true) {
        this.storageService.delete(element)
          .subscribe(() => {
              location.reload();
            },
            error => console.log(error));
      }
    }

    if (action.actionType === 'INCREASE_STORAGE') {

      const msg = 'Insert quantity to add in storage';
      const value = parseFloat(prompt(msg));
      if (!isNaN(value) && value !== null) {
        this.storageService.increaseStorage(element, value)
          .subscribe(() => {
              location.reload();
            },
            error => alert(error));
      } else {
        alert('Invalid amount');
      }
    }

    if (action.actionType === 'DECREASE_STORAGE') {

      const msg = 'Insert quantity to remove from storage';
      const value = parseFloat(prompt(msg));
      if (!isNaN(value) && value !== null) {
        this.storageService.decreaseStorage(element, value)
          .subscribe(() => {
              location.reload();
            },
            error => alert(error));
      } else {
        alert('Invalid amount');
      }
    }

    if (action.actionType === 'GO_TO') {
      const url = action.getUrl(element);
      this.router.navigateByUrl(url).then();
    }
  }
}
