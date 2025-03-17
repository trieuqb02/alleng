export interface JWT {
  tokenType: string;
  accessToken: string;
  idToken: string;
  refreshToken: string;
  expiresIn: number;
  scope: string;
}

interface CommonToken {
  iat: number,
  exp: number;
}

export interface IdToken extends CommonToken{
  username: string;
  fullName: string;
  thumbnail: string;
  email: string;
}

export interface AccessToken extends CommonToken{
  scope: string;
}
