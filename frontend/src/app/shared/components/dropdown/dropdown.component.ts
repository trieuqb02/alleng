import { Component } from '@angular/core';
import {DropdownDirective} from "../../directives/dropdown.directive";

@Component({
  selector: 'app-dropdown',
  standalone: true,
  imports: [DropdownDirective],
  templateUrl: './dropdown.component.html',
  styleUrl: './dropdown.component.css'
})
export class DropdownComponent {

}
