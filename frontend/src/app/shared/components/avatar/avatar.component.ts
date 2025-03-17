import {Component, input, InputSignal} from '@angular/core';

@Component({
  selector: 'app-avatar',
  standalone: true,
  imports: [],
  templateUrl: './avatar.component.html',
  styleUrl: './avatar.component.css'
})
export class AvatarComponent {
  public src:InputSignal<string> = input.required<string>()
  public alt:InputSignal<string> = input.required<string>()
  public size:InputSignal<number> = input.required<number>()
  public name:InputSignal<string | undefined> = input<string>()
}
