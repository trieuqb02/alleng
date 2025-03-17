import { AbstractControl, ValidationErrors } from "@angular/forms";

export default function ValidationComparePassword(controls:AbstractControl): ValidationErrors | null{
    const controlPassword = controls.get('password');
    const controlConfirmPassword = controls.get('confirmPassword');
    if(controlPassword?.value !== controlConfirmPassword?.value){
      controlConfirmPassword?.setErrors({ matching: true });
        return { matching: true };
    } else{
        return null;
    }
}
