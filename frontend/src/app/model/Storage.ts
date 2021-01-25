export class Storage {
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
  }
];

export const ACTIONS = [
  {
    label: 'layers_clear',
    actionType: 'DELETE_FROM_STORAGE',
    hoverText: 'delete from storage'
  },
  {
    label: 'add_box',
    actionType: 'INCREASE_STORAGE',
    hoverText: 'add to storage'
  },
  {
    label: 'remove_circle',
    actionType: 'DECREASE_STORAGE',
    hoverText: 'remove from storage'

  }
];
