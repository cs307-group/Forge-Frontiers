export interface UserData {
  id_: string;
  name: string;
  created_at: number;
  is_admin: boolean;
  mc_user: string;
}

export interface UserDataSecure extends UserData {
  secure: {
    user: string;
  };
}

export interface Tokens {
  accessToken: string;
  refreshToken: string;
}
