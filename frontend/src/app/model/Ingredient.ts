export class Ingredient {
  ingredientId: number;
  name: string;
  description: string;
  unit: string;
  type: string;
  quantity: number;
  shared: string;
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
    label: 'Type',
    fieldName: 'type',
  },
  {
    label: 'Unit',
    fieldName: 'unit'
  },
  {
    label: 'Shared',
    fieldName: 'shared',
  }
];

export const ACTIONS = [
  {
    label: 'info',
    actionType: 'GO_TO',
    getUrl: row => '/ingredient/' + row.ingredientId,
    hoverText: 'details'
  },
  {
    label: 'edit',
    actionType: 'GO_TO',
    getUrl: row => '/ingredientForm/' + row.ingredientId,
    hoverText: 'edit ingredient'
  },
  {
    label: 'delete',
    actionType: 'DELETE',
    hoverText: 'delete ingredient'
  },
  {
    label: 'add_circle_outline',
    actionType: 'INCREASE_STORAGE',
    hoverText: 'add to storage'
  }
];
