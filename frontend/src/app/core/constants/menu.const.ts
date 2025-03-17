import {Router} from '@angular/router';
import {IdentityService} from "../../data/services/identity.service";

export interface Menu {
  id: number;
  name: string;
  url: string;
  icon: string;
  onClick?: () => void;
}

function createUserMenu(router: Router, identityService: IdentityService): Menu[]{
  return [
    {
      id: 0,
      name: 'Profile',
      url: 'profile',
      icon: 'fa-solid fa-user',
      onClick: () => {
        router.navigate(['/user/profile']);
      },
    },
    {
      id: 1,
      name: 'Favourites',
      url: 'favorites',
      icon: 'fa-solid fa-user',
      onClick: () => {
        router.navigate(['/user/favourites']);
      },
    },
    {
      id: 2,
      name: 'Logout',
      url: 'logout',
      icon: 'fa-solid fa-right-from-bracket',
      onClick: () => {

      },
    },
  ];
}

function createAdminMenu(router: Router, identityService: IdentityService): Menu[] {
  return [
    {
      id: 0,
      name: 'Profile',
      url: 'profile',
      icon: 'fa-solid fa-user',
      onClick: () => {
        router.navigate(['/admin/profile']);
      },
    },
    {
      id: 1,
      name: 'Setting',
      url: 'setting',
      icon: 'fa-solid fa-gear',
      onClick: () => {
        router.navigate(['/admin/setting']);
      },
    },
    {
      id: 2,
      name: 'Logout',
      url: 'logout',
      icon: 'fa-solid fa-circle-left',
      onClick: () => {

      },
    },
  ];
}

function createAdminNav(router: Router): Menu[] {
  return [
    {
      id: -1,
      name: 'Management user',
      url: 'user',
      icon: 'fa-solid fa-user',
    },
    {
      id: 0,
      name: 'Management source',
      url: 'source',
      icon: 'fa-solid fa-globe',
    },
    {
      id: 1,
      name: 'Management topic',
      url: 'topic',
      icon: 'fa-solid fa-earth-americas',
    },
    {
      id: 2,
      name: 'Management news',
      url: 'news',
      icon: 'fa-regular fa-newspaper',
    },
    {
      id: 3,
      name: 'Management video',
      url: 'video',
      icon: 'fa-regular fa-file-video',
    },
    {
      id: 4,
      name: 'Management test',
      url: 'test',
      icon: 'fa-regular fa-clipboard',
    },
    {
      id: 5,
      name: 'Statistics',
      url: 'statistics',
      icon: 'fa-solid fa-chart-column',
    },
  ];
}

function createUserHNav(router: Router): Menu[]{
  return [
    {
      id: -1,
      name: 'home',
      url: 'home',
      icon: 'fa-solid fa-house',
    },
    {
      id: 0,
      name: 'test',
      url: 'test',
      icon: 'fa-solid fa-globe',
    },
  ]
}

function createAdminHNav(router: Router): Menu[] {
  return [
    {
      id: -1,
      name: 'home',
      url: 'home',
      icon: 'fa-solid fa-house',
    },
  ]
}

export {createAdminMenu, createAdminNav , createUserMenu, createUserHNav, createAdminHNav};
