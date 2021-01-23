import {Component, OnInit} from '@angular/core';
import {StorageService} from '../../service/StorageService';
import {ACTIONS, COLUMNS, Storage} from '../../model/Storage';

@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.css']
})
export class StorageComponent implements OnInit {

  header = COLUMNS;
  ingredients: Storage[];
  actions = ACTIONS;

  storageService: StorageService;

  loading: boolean;


  constructor(storageService: StorageService) {
    this.storageService = storageService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.storageService.getAll().subscribe(resp => {
      this.ingredients = resp.data;
      this.loading = false;
    });
  }
}
