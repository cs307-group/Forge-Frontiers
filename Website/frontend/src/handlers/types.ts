import Stripe from "stripe";

export interface UserData {
  id_: string;
  name: string;
  created_at: number;
  is_admin: boolean;
  mc_user: string;
  island_id: string;
  purchased_ranks: string[];
}

export interface UserDataSecure extends UserData {
  secure: {
    user: string;
    config: {
      "dark-mode"?: boolean;
      "disable-autosync"?: boolean;
    };
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

export interface GeneratorState {
  id_: string;
  level: number;
  last_collection_time: number;
  location_x: number;
  location_y: number;
  location_z: number;
  location_world: string;
  generator_id: string;
  island_id: string;
}
export interface StashState {
  id_: string;
  stash_id: string;
  location_x: number;
  location_y: number;
  location_z: number;
  location_world: string;
  contents_json: string;
  island_id: string;
}

export interface GeneratorStateFetch {
  generators: GeneratorState[];
  stashes: StashState[];
}

export interface GeneratorConfigStatic {
  "silver-gen": {
    resource: string;
    levels: Record<number, {generation_rate: number; max_size: number}>;
  };
  "coin-gen": {
    resource: string;
    levels: Record<number, {generation_rate: number; max_size: number}>;
  };
}

export interface ShopData {
  id_: string;
  item_material: string;
  item_name: string;
  item_lore: string;
  price: number;
  amount: number;
  lister_player_id: string;
  buyer_id: string;
  date_sold: number;
  custom_data: string;
}

export type StripeProductResponse = (Stripe.Product & {
  default_price: Stripe.Price;
})[];

export interface FeatureList {
  _id: string;
  value: string;
}
