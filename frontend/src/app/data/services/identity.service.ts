import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {RegisterModel} from "../models/register.interface";
import {LoginModel} from "../models/login.interface";
import {Observable} from "rxjs";
import baseUrl from "../../core/environments/config";
import {CookieService} from "ngx-cookie-service";
import {AccessToken, IdToken, JWT} from "../models/jwt.interfact";
import {User} from "../models/user.interface";
import { jwtDecode } from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class IdentityService {
  private user!: User;
  isLogged: boolean = false;
  http:HttpClient = inject(HttpClient);
  cookie:CookieService = inject(CookieService);

  login(data:LoginModel):Observable<JWT>{
    return this.http.post<any>(`${baseUrl}/login`,data);
  }

  register(data:RegisterModel): Observable<any>{
    return this.http.post<any>(`${baseUrl}/register`,data);
  }

  saveJwt(data: JWT): void {
    // save jwt into cookie
    this.cookie.set('access_token', data.accessToken);
    this.cookie.set('refresh_token', data.refreshToken);
    this.cookie.set('id_token', data.idToken);
    this.isLogged = true;

    // transform id_token to user data
    const decodedIdToken:IdToken = jwtDecode("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJBbGxFbkcuY29tIiwic3ViIjoiam9obkBleGFtcGxlLmNvbSIsImV4cCI6MTc0MDMyMjcwMiwiaWF0IjoxNzQwMzE5MTAyfQ.r7lmYe4HC194xRjgcIZNmfkmT5aNvnBsStXZvp83utdgv_0hvDO8QTcg9S8yD6MxfqmSy3gcNs4RJrUBP3uy2A")
    const decodedAccessToken:AccessToken = jwtDecode("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3NDAzMjAyOTIsImlhdCI6MTc0MDMxOTEwMiwic2NvcGUiOiJhZG1pbiJ9.RLNaAjEWiCmHJ34nVfj2N0W4wRttVHmXXGwViUXweNeLmLcFSNKaIFU8tmkWbzkXLhd4XkuayJqEKoKIU7M85Q")
    const user: User = {
      email: decodedIdToken.email,
      fullName: decodedIdToken.fullName,
      permissions: decodedAccessToken.scope.split(" "),
      role: data.scope,
      thumbnail: decodedIdToken.thumbnail,
      username: decodedIdToken.username,
    }
    this.setUserInfoDetail(user);
  }

  getTokenType(type:string): string{
    return this.cookie.get(type);
  }

  getUserInfoDetail(): User {
    return this.user || {};
  }

  setUserInfoDetail(user: User): User {
    this.user = { ...user}
    return this.user;
  }

  logout(): Observable<any> {
    const refreshToken: string = this.cookie.get('refresh_token');
    return this.http.delete<any>(`${baseUrl}/register`, {
      body: JSON.stringify({refreshToken: refreshToken})
    });
  }
}
