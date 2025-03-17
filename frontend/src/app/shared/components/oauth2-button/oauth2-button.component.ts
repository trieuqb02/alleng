import {Component, input, Input, InputSignal} from '@angular/core';

@Component({
  selector: 'app-oauth2-button',
  standalone: true,
  imports: [],
  templateUrl: './oauth2-button.component.html',
  styleUrl: './oauth2-button.component.css'
})
export class Oauth2ButtonComponent {
  public oauth2Type: InputSignal<string> = input.required();

  loginWithOauth2(): void{
    window.location.href = ``;
  }
}
