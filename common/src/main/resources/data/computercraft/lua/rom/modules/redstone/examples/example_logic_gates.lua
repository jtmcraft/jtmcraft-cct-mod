local logicGatesModule = require("rom.modules.redstone.logic_gates")
local LogicGate = logicGatesModule.LogicGate
local LogicGates = logicGatesModule.LogicGates

local logicGate = LogicGate:new()

local truthTable, err = logicGate:getTruthTable(LogicGates.AND)
if truthTable then
    print(LogicGates.AND .. " Truth Table:\n" .. truthTable .. "\n")
else
    print("Error: " .. err)
end

local a, b = 1, 0
print("AND(" .. a .. ", " .. b .. "): " .. logicGate:compute(LogicGates.AND, a, b))
