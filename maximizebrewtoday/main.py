import pulp
from flask import Flask, request, jsonify, make_response, Response
import json
import pydantic

app = Flask(__name__)


class OptimalRecipe(pydantic.BaseModel):
    ingredientName: str
    quantity: float


class OptimalResponse(pydantic.BaseModel):
    ingredients: list
    fo: int


@app.route('/', methods=['POST'])
def maximize_brew():
    payload = request.json

    print(payload)

    n = len(payload["ingredientNames"])

    model = pulp.LpProblem('brew_today', pulp.LpMaximize)

    f = pulp.LpAffineExpression(pulp.LpElement('x'))

    # VARIABLES

    x = (pulp.LpVariable(str(payload["ingredientNames"][i]), 0, payload["storage"][i]) for i in range(n))

    model.addVariables(x)

    print(model.variables())

    # OBJECTIVE

    fo = ""
    for i in range(n):
        fo += model.variables()[i]

    model += fo, "FO"

    # CONSTRAINTS

    quantityUsed = ""
    for i in range(n):
        quantityUsed += model.variables()[i]
    model += quantityUsed <= payload["capacity"]

    for i in range(n):
        model += model.variables()[i] == payload["proportions"][i] * fo  # recipe proportion constraints

    # SOLUTION

    print(model)
    model.solve()
    status = pulp.LpStatus[model.status]
    print(status)

    # OPTIMAL VALUE

    optimalRecipes = []

    for var in model.variables():
        print(var.name, "=", var.varValue)
        optimalRecipe = OptimalRecipe(ingredientName=var.name, quantity=round(var.varValue, 1))
        optimalRecipes.append(optimalRecipe)
    print()
    FO = pulp.value(model.objective)
    print("FO =", FO)

    optimalResponse = OptimalResponse(ingredients=optimalRecipes, fo=FO)

    return Response(optimalResponse.json(), mimetype='application/json')
