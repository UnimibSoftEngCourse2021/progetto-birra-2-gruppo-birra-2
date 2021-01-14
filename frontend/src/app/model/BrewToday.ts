export class BrewToday {
  recipeId: number;
  recipeName: string;
  recipeDescription: string;
  ingredientQuantities: BrewTodayIngredient[];
}

export class BrewTodayIngredient {
  ingredientName: string;
  quantity: number;
}

export const ACTIONS = [
  {
    label: 'brew',
    actionType: 'GO_TO',
    getUrl: '/today'
  }
];
