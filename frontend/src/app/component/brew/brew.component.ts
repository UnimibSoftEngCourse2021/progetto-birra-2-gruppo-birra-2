import {Component, OnInit} from '@angular/core';
import {ACTIONS, Brew, COLUMNS} from '../../model/Brew';
import {BrewService} from '../../service/BrewService';

@Component({
  selector: 'app-brew',
  templateUrl: './brew.component.html',
  styleUrls: ['./brew.component.css']
})
export class BrewComponent implements OnInit {

  header = COLUMNS;
  brews: Brew[];
  actions = ACTIONS;

  brewService: BrewService;

  loading: boolean;

  constructor(brewService: BrewService) {
    this.brewService = brewService;
  }

  ngOnInit(): void {
    this.loading = true;
    this.brewService.getAll().subscribe(resp => {
      this.brews = resp.data;
      this.loading = false;
    });
  }
}
