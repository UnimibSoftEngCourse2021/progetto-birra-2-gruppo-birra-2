import {RecipeIngredient} from './RecipeIngredient';

export class Recipe {
  recipeId: number;
  name: string;
  description: string;
  username: string;
  shared: string;
  ingredients: RecipeIngredient[];
}

export const COLUMNS = [
  {
    label: 'Name',
    fieldName: 'name',
  },
  {
    label: 'Description',
    fieldName: 'description',
  },
  {
    label: 'Ingredients',
    fieldName: 'ingredientName',
    arrayField: 'ingredients'
  }
];

export const ACTIONS = [
  {
    label: 'info',
    actionType: 'GO_TO',
    getUrl: row => '/recipe/' + row.recipeId,
  },
  {
    label: 'edit',
    actionType: 'GO_TO',
    getUrl: row => '/recipeForm/' + row.recipeId,
  },
  {
    label: 'delete',
    actionType: 'DELETE'
  },
  {
    label: 'science',
    actionType: 'BREW',
    hoverText: 'brew this recipe'
  }
];
