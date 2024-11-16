local TurtleBlockPos = {
    x = 0,
    y = 0,
    z = 0
}

TurtleBlockPos.__index = TurtleBlockPos

function TurtleBlockPos:new(x, y, z)
    local point = {}
    setmetatable(point, TurtleBlockPos)

    point.x = x
    point.y = y
    point.z = z

    return point
end

function TurtleBlockPos:isEqual(other)
    return self.x == other.x and self.y == other.y and self.z == other.z
end

function TurtleBlockPos:__tostring()
    return "TurtleBlockPos: { x = " .. self.x .. ", y = " .. self.y .. ", z = " .. self.z .. " }"
end

return TurtleBlockPos
