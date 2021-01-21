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
    label: 'Quantity In Storage',
    fieldName: 'quantity',
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
  },
  {
    label: 'edit',
    actionType: 'GO_TO',
    getUrl: row => '/ingredientForm/' + row.ingredientId,
  },
  {
    label: 'delete',
    actionType: 'DELETE'
  }
];
