import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {DropdownComponent} from "../../shared/components/dropdown/dropdown.component";
import {ButtonComponent} from "../../shared/components/button/button.component";
import {AvatarComponent} from "../../shared/components/avatar/avatar.component";
import {IdentityService} from "../../data/services/identity.service";
import {
  createAdminHNav,
  createAdminMenu,
  createUserHNav,
  createUserMenu,
  Menu
} from "../../core/constants/menu.const";
import {RoleTypes} from "../../core/constants/author.const";
import {User} from "../../data/models/user.interface";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, DropdownComponent, ButtonComponent, AvatarComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  router:Router = inject(Router);
  identityService:IdentityService = inject(IdentityService);

  isLoggedIn!: boolean;
  user!: User;
  links!: Menu[];
  menu!: Menu[];

  constructor() {}

  ngOnInit(): void {
    this.isLoggedIn = this.identityService.isLogged;
    this.user = this.identityService.getUserInfoDetail();
    if(this.user.role === RoleTypes.ADMIN){
      this.menu = createAdminMenu(this.router, this.identityService);
      this.links = createAdminHNav(this.router)
    } else {
      this.menu = createUserMenu(this.router, this.identityService);
      this.links = createUserHNav(this.router)
    }
  }

  handleClick(menuItem: Menu): void {
    if (menuItem.onClick) {
      menuItem.onClick();
    }
  }
}
