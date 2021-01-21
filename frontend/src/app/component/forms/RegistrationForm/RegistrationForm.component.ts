import {Component, OnInit} from '@angular/core';
import {Brewer} from '../../../model/Brewer';
import {AuthService} from '../../../service/AuthService';
import {Router} from '@angular/router';


@Component({
  selector: 'app-registration-form',
  templateUrl: './RegistrationForm.component.html',
  styleUrls: ['./RegistrationForm.component.css']
})

export class RegistrationFormComponent implements OnInit {

  brewer: Brewer = new Brewer();

  constructor(private authService: AuthService,
              private router: Router) {
  }

  onSubmit(): void {
    this.authService.registration(this.brewer).subscribe((resp) => {
        const msg = 'Registration successful, login with the username and password you entered';
        alert(msg);
        this.router.navigate(['index']);
      }, err => {
        alert(err);
      }
    );
  }

  ngOnInit(): void {
  }
}
