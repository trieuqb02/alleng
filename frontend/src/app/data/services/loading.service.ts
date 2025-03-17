import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private loadingSubject:BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  loading:Observable<boolean> = this.loadingSubject.asObservable();

  loadingOn():void {
    this.loadingSubject.next(true);
  }

  loadingOff():void {
    this.loadingSubject.next(false);
  }
}
