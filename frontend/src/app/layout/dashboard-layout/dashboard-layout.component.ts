import { Component } from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {RouterOutlet} from "@angular/router";
import {NavComponent} from "../nav/nav.component";
import {FooterComponent} from "../footer/footer.component";

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [
    HeaderComponent,
    RouterOutlet,
    NavComponent,
    FooterComponent
  ],
  templateUrl: './dashboard-layout.component.html',
  styleUrl: './dashboard-layout.component.css'
})
export class DashboardLayoutComponent {

}
