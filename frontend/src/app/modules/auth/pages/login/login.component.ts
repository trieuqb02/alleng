import {Component, inject} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ButtonComponent} from "../../../../shared/components/button/button.component";
import {Oauth2ButtonComponent} from "../../../../shared/components/oauth2-button/oauth2-button.component";
import {RouterLink} from "@angular/router";
import {IdentityService} from "../../../../data/services/identity.service";
import {LoginModel} from "../../../../data/models/login.interface";
import {LoadingService} from "../../../../data/services/loading.service";
import {JWT} from "../../../../data/models/jwt.interfact";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonComponent,
    Oauth2ButtonComponent,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: '../../../../../assets/css/auth.css'
})
export class LoginComponent {
  identityService: IdentityService = inject(IdentityService);
  loadingService:LoadingService = inject(LoadingService);
  toast: ToastrService = inject(ToastrService);

  public submitted: Boolean = false;
  public error!: String;

  loginInForm = new FormGroup({
    username: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.minLength(6), Validators.maxLength(255)]),
  })

  get f(): { [key: string]: AbstractControl } {
    return this.loginInForm.controls;
  }

  cancelError() {
    if (this.submitted) {
      this.submitted = false;
    }
  }

  onSubmit() {
    if (this.error != '') {
      this.error = '';
    }
    this.submitted = true;
    if (this.loginInForm.valid) {
      this.loadingService.loadingOn();
      setTimeout(() => {
        const data: LoginModel = {
          username: this.f['username'].value ?? '',
          password: this.f['password'].value ?? '',
        };
        this.identityService.login(data).subscribe(
          {
            next: (value:JWT): void => {
              this.toast.success("login successfully.","", {
                positionClass: "toast-bottom-right",
              });
              this.identityService.saveJwt(value);
              this.identityService.isLogged = true;
            },
            error: error => {
              this.toast.error("login fail.","", {
                positionClass: "toast-bottom-right",
              });
            }
          }
        )
        this.loadingService.loadingOff();
      }, 1000);

    }
  }
}
