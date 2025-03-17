import {Component, inject, OnInit} from '@angular/core';
import {createAdminNav, Menu} from "../../core/constants/menu.const";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent implements OnInit {
  router:Router = inject(Router);

  links!:Menu[];

  ngOnInit(): void {
    this.links = createAdminNav(this.router)
  }

}
