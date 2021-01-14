interface Url {
  action: Actions;
  url: string;
}

export enum Actions {

  LOGIN,
  GET_RECIPE,
  GET_RECIPE_BY_ID,
  SAVE_RECIPE,
  DELETE_RECIPE,
  GET_INGREDIENT,
  GET_INGREDIENT_BY_ID,
  SAVE_INGREDIENT,
  DELETE_INGREDIENT,
  EDIT_INGREDIENT,
  BREW_TODAY,
  GET_TOOLS,
  DELETE_TOOL,
  EDIT_TOOL,
  SAVE_TOOL
}

export const BASE_URL_DEV = 'http://ec2-18-156-174-69.eu-central-1.compute.amazonaws.com:8080/';
export const BASE_URL_LOCALHOST = 'http://localhost:8080/';

export const BASE_URL = BASE_URL_LOCALHOST;

export const URLS: Url[] = [

  {
    action: Actions.LOGIN,
    url: BASE_URL + 'login'
  },
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
  },
  {
    action: Actions.GET_INGREDIENT_BY_ID,
    url: BASE_URL + 'ingredient'
  },
  {
    action: Actions.SAVE_INGREDIENT,
    url: BASE_URL + 'ingredient'
  },
  {
    action: Actions.DELETE_INGREDIENT,
    url: BASE_URL + 'ingredient'
  },
  {
    action: Actions.EDIT_INGREDIENT,
    url: BASE_URL + 'ingredient/'
  },
  {
    action: Actions.BREW_TODAY,
    url: BASE_URL + 'today'
  }
];
