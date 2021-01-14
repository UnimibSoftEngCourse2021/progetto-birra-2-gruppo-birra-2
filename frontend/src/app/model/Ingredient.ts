export class Ingredient {
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
    label: 'Quantity',
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
