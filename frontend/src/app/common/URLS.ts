interface Url {
  action: Actions;
  url: string;
}

export enum Actions {

  GET_RECIPE,
  GET_RECIPE_BY_ID,
  SAVE_RECIPE,
  DELETE_RECIPE,
  GET_INGREDIENT
}

export const BASE_URL = 'http://localhost:8080/';

export const URLS: Url[] = [
  {
    action: Actions.GET_RECIPE,
    url: BASE_URL + 'recipe/'
  },
  {
    action: Actions.GET_RECIPE_BY_ID,
    url: BASE_URL + 'recipe'
  },
  {
    action: Actions.SAVE_RECIPE,
    url: BASE_URL + 'recipe/'
  },
  {
    action: Actions.DELETE_RECIPE,
    url: BASE_URL + 'recipe'
  },
  {
    action: Actions.GET_INGREDIENT,
    url: BASE_URL + 'ingredient/'
  }
];
