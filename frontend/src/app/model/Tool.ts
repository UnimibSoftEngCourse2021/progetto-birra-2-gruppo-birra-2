export class Tool {
  toolId: number;
  name: string;
  capacity: number;
  unit: string;
  quantity: number;
  description: string;
}
export const COLUMNS = [
  {
    label: 'Id',
    fieldName: 'toolId',
  },
  {
    label: 'Name',
    fieldName: 'name',
  },
  {
    label: 'Capacity',
    fieldName: 'capacity',
  },
  {
    label: 'Unit',
    fieldName: 'unit',
  },
  {
    label: 'Quantity',
    fieldName: 'quantity',
  },
  {
    label: 'Description',
    fieldName: 'description',
  }
];

export const ACTIONS = [
  {
    label: 'info',
    actionType: 'GO_TO',
    getUrl: row => '/createForm/' + row.id,
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
