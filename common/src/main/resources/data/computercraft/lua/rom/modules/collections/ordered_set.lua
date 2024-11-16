local OrderedSet = {}
OrderedSet.__index = OrderedSet

function OrderedSet.new()
    return setmetatable({ items = {}, keys = {} }, OrderedSet)
end

function OrderedSet:insert(item)
    local key = tostring(item)
    if not self.keys[key] then
        table.insert(self.items, item)
        self.keys[key] = true
    end
end

function OrderedSet:values()
    return self.items
end

return OrderedSet
