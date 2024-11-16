local J = {}

function J.stringify(t, indent)
    indent = indent or 0
    local str = "{\n"

    local function format_key(k)
        if type(k) == "number" then
            return "[" .. k .. "] = "
        elseif type(k) == "string" then
            return k .. " = "
        end
    end

    local function format_value(v)
        if type(v) == "number" or type(v) == "string" then
            return "\"" .. v .. "\",\n"
        elseif type(v) == "table" then
            return J.stringify(v, indent + 2) .. ",\n"
        else
            return "\"" .. tostring(v) .. "\",\n"
        end
    end

    for k, v in pairs(t) do
        str = str .. string.rep(" ", indent) .. format_key(k) .. format_value(v)
    end

    str = str .. string.rep(" ", indent - 2) .. "}"

    return str
end

function J.each(t, f)
    for k, v in pairs(t) do
        f(v, k)
    end
end

function J.reduce(t, f, state)
    for _, value in pairs(t) do
        if state == nil then
            state = value
        else
            state = f(state, value)
        end
    end
    return state
end

function J.filter(t, f)
    local selected = {}
    for k, v in pairs(t) do
        if f(v, k) then
            selected[#selected + 1] = v
        end
    end
    return selected
end

function J.skip(t, i)
    local skipped = {}
    for j = 1, #t do
        if j <= i then
            skipped[j] = nil
        else
            skipped[#skipped + 1] = t[j]
        end
    end
    return skipped
end

function J.max(t)
    return math.max(table.unpack(t))
end

function J.min(t)
    return math.min(table.unpack(t))
end

local function new_mapped(index, fn_value, mapped, value)
    local key, key_value, v = index, fn_value(value, index)
    mapped[v and key_value or key] = v or key_value
    return mapped
end

local function new_table(start_index, end_index, t, array_or_value)
    for i = start_index, end_index do
        t[#t + 1] = type(array_or_value) == 'table' and array_or_value[i] or array_or_value
    end
    return t
end

function J.map(t, f)
    local mapped = {}
    for index, value in pairs(t) do
        mapped = new_mapped(index, f, mapped, value)
    end
    return mapped
end

function J.imap(t, f)
    local mapped = {}
    for index, value in ipairs(t) do
        mapped = new_mapped(index, f, mapped, value)
    end
    return mapped
end

function J.slice(array, start, finish)
    local t = {}
    return new_table(start or 1, finish or #array, t, array)
end

function J.fill(i, j)
    local filled = {}
    return new_table(1, i, filled, j)
end

function J.keys(obj)
    local keys = {}
    for key in pairs(obj) do
        keys[#keys + 1] = key
    end
    return keys
end

function J.values(obj)
    local values = {}
    for _, value in pairs(obj) do
        values[#values + 1] = value
    end
    return values
end

function J.chain(value)
    local t = {
        _value = value,
        value = function(self)
            return self._value
        end
    }

    for k, v in pairs(J) do
        if type(v) == "function" then
            t[k] = function(self, ...)
                local result = v(self._value, ...)
                if result ~= self._value then
                    self._value = result
                end
                return self
            end
        end
    end

    return t
end

return J
