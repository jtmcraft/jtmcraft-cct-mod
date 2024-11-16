local TurtleBlockPos = require("rom.modules.turtle.block_pos")
local OrderedSet = require("rom.modules.collections.ordered_set")

local ShapedMission = {}
ShapedMission.__index = ShapedMission

function ShapedMission.new()
    local self = setmetatable({}, ShapedMission)
    self.points = OrderedSet.new()
    return self
end

-- ellipse elongated along x-axis, make the major > minor
-- ellipse elongated along z-axis, make the minor > major
function ShapedMission:ellipse(center, semiMajor, semiMinor)
    local precisionStepSize = 0.001

    -- walk the ellipse
    for t = 0, 2 * math.pi, precisionStepSize do
        local x = math.floor(semiMajor * math.cos(t) + 0.5)
        local z = math.floor(semiMinor * math.sin(t) + 0.5)

        local blockPos = TurtleBlockPos:new(center.x + x, center.y, center.z + z)
        self.points:insert(blockPos)
    end
end

-- rectangle elongated along x-axis, make the length > width.
-- rectangle elongated along z-axis, make the width > length.
function ShapedMission:rectangle(length, width)
    local halfLength = math.floor(length / 2)
    local halfWidth = math.floor(width / 2)

    for x = -halfLength, halfLength do
        local blockPos = TurtleBlockPos:new(x, 0, -halfWidth)
        self.points:insert(blockPos)
    end

    for z = -halfWidth, halfWidth do
        local blockPos = TurtleBlockPos:new(halfLength, 0, z)
        self.points:insert(blockPos)
    end

    for x = halfLength, -halfLength, -1 do
        local blockPos = TurtleBlockPos:new(x, 0, halfWidth)
        self.points:insert(blockPos)
    end

    for z = halfWidth, -halfWidth, -1 do
        local blockPos = TurtleBlockPos:new(-halfLength, 0, z)
        self.points:insert(blockPos)
    end
end

function ShapedMission:circle(center, radius)
    self:ellipse(center, radius, radius)
end

function ShapedMission:square(length)
    self:rectangle(length, length)
end

function ShapedMission:postProcessPoints(postProcessor)
    local pointsList = self.points:values()

    for _, point in ipairs(pointsList) do
        if point ~= nil then
            postProcessor(point)
        end
    end
end

return ShapedMission
