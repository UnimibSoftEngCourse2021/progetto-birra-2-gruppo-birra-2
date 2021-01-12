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
  GET_TOOLS,
  SAVE_TOOL,
  DELETE_TOOL,
  EDIT_TOOL
}

export const BASE_URL_DEV = 'http://ec2-52-29-235-197.eu-central-1.compute.amazonaws.com:8080/';
export const BASE_URL_LOCALHOST = 'http://localhost:8080/';

export const BASE_URL = BASE_URL_LOCALHOST;

export const URLS: Url[] = [

  {
    action: Actions.LOGIN,
    url: BASE_URL + 'login/'
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
    action: Actions.GET_TOOLS,
    url: BASE_URL + 'tool/'
  },
  {
    action: Actions.SAVE_TOOL,
    url: BASE_URL + 'tool/'
  },
  {
    action: Actions.DELETE_TOOL,
    url: BASE_URL + 'tool/'
  },
  {
    action: Actions.EDIT_TOOL,
    url: BASE_URL + 'tool/'
  }

];
