import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {JwtHelperService} from '@auth0/angular-jwt';
import {AuthService} from '../../service/AuthService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  invalidLogin = false;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private authService: AuthService,
              private jwtHelper: JwtHelperService) {
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }
    const loginPayload = {
      username: this.loginForm.controls.username.value,
      password: this.loginForm.controls.password.value
    };

    this.authService.login(loginPayload).subscribe(data => {
      window.localStorage.setItem('token', data.token);
      window.localStorage.setItem('username', this.jwtHelper.decodeToken(data.token).username);
      this.router.navigate(['index']);

    }, err => {
      this.invalidLogin = true;
      const msg = 'Login Error';
      alert(msg);
    });
  }

  ngOnInit(): void {
    window.localStorage.removeItem('token');
    window.localStorage.removeItem('username');
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.required]
    });
  }
}
