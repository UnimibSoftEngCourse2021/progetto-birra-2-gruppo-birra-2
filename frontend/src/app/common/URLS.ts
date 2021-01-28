import {environment} from '../../environments/environment';

interface Url {
  action: Actions;
  url: string;
}

export enum Actions {

  LOGIN,
  REGISTRATION,
  USER_EXIST,
  GET_BREWER_BY_USERNAME,
  EDIT_BREWER,
  DELETE_BREWER,
  GET_RECIPE,
  GET_RECIPE_BY_ID,
  SAVE_RECIPE,
  EDIT_RECIPE,
  DELETE_RECIPE,
  GET_PUBLIC_RECIPES,
  GET_INGREDIENTS_BY_RECIPE,
  GET_INGREDIENT,
  GET_INGREDIENT_BY_ID,
  SAVE_INGREDIENT,
  DELETE_INGREDIENT,
  EDIT_INGREDIENT,
  GET_STORAGE,
  GET_STORAGE_INGREDIENT,
  EDIT_STORAGE,
  BREW_TODAY,
  GET_TOOLS,
  GET_TOOL_BY_ID,
  DELETE_TOOL,
  EDIT_TOOL,
  SAVE_TOOL,
  GET_BREW,
  GET_BREW_BY_ID,
  SAVE_BREW,
  EDIT_BREW,
  DELETE_BREW,
  GET_INGREDIENT_FOR_BREW
}

export const BASE_URL = environment.serverUrl;

export const URLS: Url[] = [

  {
    action: Actions.LOGIN,
    url: BASE_URL + 'login'
  },
  {
    action: Actions.REGISTRATION,
    url: BASE_URL + 'brewer'
  },
  {
    action: Actions.USER_EXIST,
    url: BASE_URL + 'brewer/username'
  },
  {
    action: Actions.GET_BREWER_BY_USERNAME,
    url: BASE_URL + 'brewer'
  },
  {
    action: Actions.EDIT_BREWER,
    url: BASE_URL + 'brewer'
  },
  {
    action: Actions.DELETE_BREWER,
    url: BASE_URL + 'brewer'
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
    action: Actions.EDIT_RECIPE,
    url: BASE_URL + 'recipe'
  },
  {
    action: Actions.DELETE_RECIPE,
    url: BASE_URL + 'recipe'
  },
  {
    action: Actions.GET_PUBLIC_RECIPES,
    url: BASE_URL + 'recipe/shared'
  },
  {
    action: Actions.GET_INGREDIENTS_BY_RECIPE,
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
    action: Actions.GET_STORAGE,
    url: BASE_URL + 'storage'
  },
  {
    action: Actions.GET_STORAGE_INGREDIENT,
    url: BASE_URL + 'storage'
  },
  {
    action: Actions.EDIT_STORAGE,
    url: BASE_URL + 'storage'
  },
  {
    action: Actions.BREW_TODAY,
    url: BASE_URL + 'today'
  },
  {
    action: Actions.GET_TOOLS,
    url: BASE_URL + 'tool'
  },
  {
    action: Actions.GET_TOOL_BY_ID,
    url: BASE_URL + 'tool'
  },
  {
    action: Actions.DELETE_TOOL,
    url: BASE_URL + 'tool'
  },
  {
    action: Actions.EDIT_TOOL,
    url: BASE_URL + 'tool'
  },
  {
    action: Actions.SAVE_TOOL,
    url: BASE_URL + 'tool'
  },
  {
    action: Actions.GET_BREW,
    url: BASE_URL + 'brew/'
  },
  {
    action: Actions.GET_BREW_BY_ID,
    url: BASE_URL + 'brew'
  },
  {
    action: Actions.SAVE_BREW,
    url: BASE_URL + 'brew/'
  },
  {
    action: Actions.EDIT_BREW,
    url: BASE_URL + 'brew'
  },
  {
    action: Actions.DELETE_BREW,
    url: BASE_URL + 'brew'
  },
  {
    action: Actions.GET_INGREDIENT_FOR_BREW,
    url: BASE_URL + 'brew'
  },
];
