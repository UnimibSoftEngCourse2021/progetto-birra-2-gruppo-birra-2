import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(value: any, searchText: any, field: any) {
    if (!searchText) {
      return value;
    }
    return value.filter((data) => this.matchValue(data, searchText, field));
  }

  matchValue(data, value, field) {
    return Object.keys(data).map((key) => {
      if (!field || field === 'All') {
        return new RegExp(value, 'gi').test(data[key]);
      }
      return new RegExp(value, 'gi').test(data[key]) && key === field;
    }).some(result => result);
  }
}
