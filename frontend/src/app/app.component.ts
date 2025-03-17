import {Component, inject} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {LoadingService} from "./data/services/loading.service";
import {LoadingComponent} from "./shared/components/loading/loading.component";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoadingComponent, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'alleng';
  loadingService:LoadingService = inject(LoadingService);
}
