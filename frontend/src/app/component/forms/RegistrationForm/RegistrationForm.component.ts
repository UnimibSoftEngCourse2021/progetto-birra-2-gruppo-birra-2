import {Component, OnInit} from '@angular/core';
import {Brewer} from '../../../model/Brewer';
import {AuthService} from '../../../service/AuthService';
import {Router} from '@angular/router';
import {Location} from '@angular/common';

@Component({
  selector: 'app-registration-form',
  templateUrl: './RegistrationForm.component.html',
  styleUrls: ['./RegistrationForm.component.css']
})

export class RegistrationFormComponent implements OnInit {

  brewer: Brewer = new Brewer();

  confirmPassword: string;

  invalidForm: boolean;

  constructor(private authService: AuthService,
              private router: Router,
              private location: Location) {
  }

  ngOnInit(): void {
    this.brewer.password = this.confirmPassword = this.brewer.email = '';
    this.brewer.maxBrew = 0;
  }

  onSubmit(): void {
    if (this.brewer.maxBrew < 0) {
      this.invalidForm = true;
      return;
    }
    if (this.brewer.password !== this.confirmPassword) {
      this.invalidForm = true;
      return;
    }
    if (!this.brewer.email.match('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')) {
      this.invalidForm = true;
      return;
    }
    this.authService.registration(this.brewer).subscribe((resp) => {
        const msg = 'Registration successful, login with the username and password you entered';
        alert(msg);
        this.router.navigate(['login']);
      }, err => {
        alert(err.error.data);
        this.invalidForm = true;
      }
    );
  }

  goBack(): void {
    this.location.back();
  }
}
