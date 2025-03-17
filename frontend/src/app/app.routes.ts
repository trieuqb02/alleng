import {Route, Routes} from '@angular/router';
import {UserLayoutComponent} from "./layout/user-layout/user-layout.component";
import {AuthLayoutComponent} from "./layout/auth-layout/auth-layout.component";
import {DashboardLayoutComponent} from "./layout/dashboard-layout/dashboard-layout.component";
import {authRoutes} from "./modules/auth/auth.routes";
import {userHomeRoutes} from "./modules/user/home/home.routes";
import {adminHomeRoutes} from "./modules/admin/home/home.routes";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: '',
    component: UserLayoutComponent,
    children: [
      {
        path: 'home',
        loadChildren: (): Route[] => userHomeRoutes
      },
      {
        path: 'auth',
        component: AuthLayoutComponent,
        loadChildren: (): Routes => authRoutes
      }
    ]
  },
  {
    path: 'dashboard',
    component: DashboardLayoutComponent,
    children: [
      {
        path: 'home',
        loadChildren: (): Route[] => adminHomeRoutes
      },
    ]
  },
  {
    path: '**', loadComponent: () =>
      import('./modules/not-found/not-found.component')
        .then(m => m.NotFoundComponent),
  }
];
