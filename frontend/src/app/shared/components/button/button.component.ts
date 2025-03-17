import {Component, input, Input, InputSignal} from '@angular/core';

type ButtonType = 'button' | 'submit' | 'reset';
type ButtonFontSize= 'xl' | 'lg'| 'md' | 'sm' | 'xs' | 'xxs';
type TextPosition = 'start' | 'center' | 'end';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [],
  templateUrl: './button.component.html',
  styleUrl: './button.component.css'
})
export class ButtonComponent {
  type: InputSignal<ButtonType> = input<ButtonType>('button');
  buttonClass: InputSignal<string> = input<string>('none');
  fontSize: InputSignal<ButtonFontSize> = input<ButtonFontSize>('md');
  disabled: InputSignal<boolean> = input<boolean>(false);
  borderRadius: InputSignal<string | undefined> = input<string>();
  position: InputSignal<TextPosition> = input<TextPosition>('center');
}
