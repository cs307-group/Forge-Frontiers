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

export interface PlayerStats {
  player_uuid: string;
  current_health: number;
  HP: number;
  ATK: number;
  STR: number;
  DEX: number;
  CRATE: number;
  CDMG: number;
  DEF: number;
}
