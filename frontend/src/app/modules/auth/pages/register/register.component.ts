import {Component, inject} from '@angular/core';
import {Oauth2ButtonComponent} from "../../../../shared/components/oauth2-button/oauth2-button.component";
import {ButtonComponent} from "../../../../shared/components/button/button.component";
import {AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import ValidationComparePassword from "../../../../core/validations/ValidationComparePassword";
import {RouterLink} from "@angular/router";
import {RegisterModel} from "../../../../data/models/register.interface";
import {LoadingService} from "../../../../data/services/loading.service";
import {IdentityService} from "../../../../data/services/identity.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonComponent,
    Oauth2ButtonComponent,
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrl: '../../../../../assets/css/auth.css'
})
export class RegisterComponent {

  loadingService:LoadingService = inject(LoadingService);
  identityService: IdentityService = inject(IdentityService);
  toast: ToastrService = inject(ToastrService);

  public submitted: Boolean = false;
  public messageError!: string;

  registerForm = new FormGroup({
    username: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    password: new FormControl(null, [Validators.required, Validators.minLength(6), Validators.maxLength(255)]),
    firstName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    lastName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    confirmPassword: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
  }, {
    validators: ValidationComparePassword,
  })

  get f(): { [key: string]: AbstractControl } {
    return this.registerForm.controls;
  }

  cancelError() {
    if (this.submitted) {
      this.submitted = false;
      this.messageError = "";
    }
  }

  onSubmit() {
    this.submitted = true;
    if (this.registerForm.valid) {
      this.loadingService.loadingOn();
      setTimeout(()=> {
        const data: RegisterModel = {
          username: this.f['username'].value,
          firstName: this.f['firstName'].value,
          lastName: this.f['lastName'].value,
          password: this.f['password'].value
        }
        this.identityService.register(data).subscribe(
          {
            next: value => {
              this.toast.success("Register successfully!","", {
                positionClass: "toast-bottom-right",
              });
            },
            error: error => {
              this.toast.error("Register fail!","", {
                positionClass: "toast-bottom-right",
              });
            }
          }
        )
        this.loadingService.loadingOff();
      }, 2000)
      this.loadingService.loadingOff();
    }
  }
}
