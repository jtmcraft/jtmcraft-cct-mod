-- Define the LogicGate class
local LogicGate = {}
LogicGate.__index = LogicGate

-- "Enum" for Logic Gates
local LogicGates = {
    AND = "AND",
    NAND = "NAND",
    OR = "OR",
    NOR = "NOR",
    XOR = "XOR",
    NXOR = "NXOR",
    INVERT = "INVERT"
}

-- Constructor for the LogicGate class
function LogicGate:new()
    local self = setmetatable({}, LogicGate)
    self.truthTables = {
        [LogicGates.AND] = { {0, 0, 0}, {0, 1, 0}, {1, 0, 0}, {1, 1, 1} },
        [LogicGates.NAND] = { {0, 0, 1}, {0, 1, 1}, {1, 0, 1}, {1, 1, 0} },
        [LogicGates.OR] = { {0, 0, 0}, {0, 1, 1}, {1, 0, 1}, {1, 1, 1} },
        [LogicGates.NOR] = { {0, 0, 1}, {0, 1, 0}, {1, 0, 0}, {1, 1, 0} },
        [LogicGates.XOR] = { {0, 0, 0}, {0, 1, 1}, {1, 0, 1}, {1, 1, 0} },
        [LogicGates.NXOR] = { {0, 0, 1}, {0, 1, 0}, {1, 0, 0}, {1, 1, 1} },
        [LogicGates.INVERT] = { {0, 1}, {1, 0} }
    }
    self.logicFunctions = {
        [LogicGates.INVERT] = function(a) return 1 - a end,
        [LogicGates.AND] = function(a, b) return a * b end,
        [LogicGates.NAND] = function(a, b) return 1 - (a * b) end,
        [LogicGates.OR] = function(a, b) return math.max(a, b) end,
        [LogicGates.NOR] = function(a, b) return 1 - math.max(a, b) end,
        [LogicGates.XOR] = function(a, b) return a ~= b and 1 or 0 end,
        [LogicGates.NXOR] = function(a, b) return a == b and 1 or 0 end
    }
    return self
end

-- Method to get the truth table as a string with new line characters
function LogicGate:getTruthTable(gate)
    local truthTable = self.truthTables[gate]
    if not truthTable then
        return nil, "Invalid logic gate"
    end

    local result = {}
    for _, row in ipairs(truthTable) do
        local inputPart = table.pack(table.unpack(row, 1, #row - 1)) -- Get all but the last element as inputs
        local outputPart = row[#row]                                  -- Get the last element as output
        local rowString = table.concat(inputPart, " ") .. " = " .. tostring(outputPart)
        table.insert(result, rowString)
    end

    return table.concat(result, "\n")
end

-- Method to compute the output for a given gate and inputs
function LogicGate:compute(gate, a, b)
    local func = self.logicFunctions[gate]
    if not func then
        return nil, "Invalid logic gate"
    end
    return func(a, b)
end

return {
    LogicGate = LogicGate,
    LogicGates = LogicGates
}
