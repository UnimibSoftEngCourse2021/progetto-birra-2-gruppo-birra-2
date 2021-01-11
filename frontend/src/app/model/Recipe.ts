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
    label: 'Id',
    fieldName: 'recipeId',
  },
  {
    label: 'Name',
    fieldName: 'name',
  },
  {
    label: 'Description',
    fieldName: 'description',
  },
  {
    label: 'Username',
    fieldName: 'username',
  },
  {
    label: 'Shared',
    fieldName: 'shared',
  },
  {
    label: 'Ingredients',
    fieldName: 'ingredientId',
    arrayField: 'ingredients'
  }
];

export const ACTIONS = [
  {
    label: 'info',
    actionType: 'GO_TO',
    getUrl: row => '/editForm/' + row.id,
  },
  {
    label: 'edit',
    actionType: 'GO_TO',
    getUrl: row => '/editForm/' + row.id,
  },
  {
    label: 'delete',
    actionType: 'DELETE'
  }
];
