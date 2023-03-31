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

export interface BazaarLookup {
  slot_id: number;
  item_name: string;
  item_lore: string;
  item_material: string;
  custom_data: any;
}
export interface MarketState {
  order_id: number;
  order_type: boolean;
  lister_id: string;
  slot_id: number;
  amount: number;
  price: number;
  listdate: string;
  item: BazaarLookup;
}

export interface MarketStateFetch {
  bazaar: MarketState[];
  lookup: BazaarLookup[];
  cheapest: Record<number, MarketState>;
  counts: Record<number, number>;
}

export interface GeneratorStateFetch {
  id_: string
  level: number;
  last_collection_time: number;
  location_x: number;
  location_y: number;
  location_z: number;
  location_world: string;
  island_id: string;
}