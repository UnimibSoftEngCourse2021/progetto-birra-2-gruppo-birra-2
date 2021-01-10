import pulp

ingredient = ["acqua", "luppolo", "malto"]
storage = [5, 3, 3]
proportion = [0.5, 0.25, 0.25]
capacity = 5

n = len(ingredient)

model = pulp.LpProblem('brew_today', pulp.LpMaximize)

f = pulp.LpAffineExpression(pulp.LpElement('x'))

#VARIABLES


x = (pulp.LpVariable(ingredient[i], 0, storage[i]) for i in range(n))

model.addVariables(x)

print(model.variables())



#OBJECTIVE

fo = ""
for i in range(n):
    fo += model.variables()[i]

model += fo, "FO"

#CONSTRAINTS

quantityUsed = ""
for i in range(n):
    quantityUsed += model.variables()[i]
model += quantityUsed <= capacity


for i in range(n):
    model += model.variables()[i] == proportion[i]*fo   # recipe proportion constraints

#SOLUTION

print(model)
model.solve()
status = pulp.LpStatus[model.status]
print(status)

#OPTIMAL VALUE

for var in model.variables() :
    print(var.name, "=", var.varValue)
print()
FO = pulp.value(model.objective)
print("FO =", FO)
