import pulp
from flask import Flask, request

app = Flask(__name__)


@app.route('/', methods=['POST'])
def hello_world():

    payload = request.json

    print(payload)

    n = len(payload["ingredients"])

    model = pulp.LpProblem('brew_today', pulp.LpMaximize)

    f = pulp.LpAffineExpression(pulp.LpElement('x'))

    # VARIABLES

    x = (pulp.LpVariable(payload["ingredients"][i], 0, payload["storage"][i]) for i in range(n))

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

    for var in model.variables():
        print(var.name, "=", var.varValue)
    print()
    FO = pulp.value(model.objective)
    print("FO =", FO)

    return str(FO)
