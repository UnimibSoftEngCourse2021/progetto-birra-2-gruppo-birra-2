import {Recipe} from './Recipe';

export class Brew {
  brewId: number;
  quantity: number;
  startDate: string;
  duration: number;
  note: string;
  recipe: Recipe;
  username: string;
}

export const COLUMNS = [
  {
    label: 'Recipe',
    fieldName: 'recipe.name',
  },
  {
    label: 'Quantity',
    fieldName: 'quantity',
  },
  {
    label: 'Start Date',
    fieldName: 'startDate',
  },
  {
    label: 'Duration',
    fieldName: 'duration',
  },
  {
    label: 'Note',
    fieldName: 'note',
  }
];

export const ACTIONS = [
  {
    label: 'info',
    actionType: 'GO_TO',
    getUrl: row => '/brew/' + row.brewId,
  },
  {
    label: 'edit',
    actionType: 'GO_TO',
    getUrl: row => '/brewForm/' + row.brewId,
  },
  {
    label: 'delete',
    actionType: 'DELETE'
  }
];
