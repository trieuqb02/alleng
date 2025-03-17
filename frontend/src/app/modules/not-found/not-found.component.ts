import {Component, inject} from '@angular/core';
import { Location } from '@angular/common';
import {ButtonComponent} from "../../shared/components/button/button.component";

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './not-found.component.html',
  styleUrl: './not-found.component.css'
})
export class NotFoundComponent {
  location:Location = inject(Location)

  navigateBack(): void {
    this.location.back();
  }
}
