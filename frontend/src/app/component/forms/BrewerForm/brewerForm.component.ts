import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {BrewerService} from '../../../service/BrewerService';
import {Brewer} from '../../../model/Brewer';


@Component({
  selector: 'app-form-brewer',
  templateUrl: './brewerForm.component.html',
  styleUrls: ['./brewerForm.component.css']
})

export class BrewerFormComponent implements OnInit {

  isEdit = false;
  brewer: Brewer = new Brewer();

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private brewerService: BrewerService) {
  }

  ngOnInit(): void {
    // tslint:disable-next-line:radix
    const username = window.localStorage.getItem('username');

    this.brewerService.getByUsername(username).subscribe(resp => {
        this.brewer = resp.data;
      },
      error => {
        alert(error.error.data);
        window.location.href = '/login';
      });

  }

  onSubmit(): void {
    this.brewerService.edit(this.brewer).subscribe(response => {
        this.router.navigate(['ingredient']);
      },
      error => {
        alert(error);
      });
  }

  delete(): void {
    const msg = 'Are you really sure? You will permanently lost all your data';
    if (confirm(msg) === true) {
      this.brewerService.delete(this.brewer.username)
        .subscribe(() => {
            this.router.navigate(['login']);
          },
          error => console.log(error));
    }
  }

  goBack(): void {
    this.location.back();
  }

  edit(): void {
    this.isEdit = !this.isEdit;
  }
}
