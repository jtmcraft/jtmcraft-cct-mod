local TurtleUtil = {
    lap = peripheral.find("location_aware"),
    dryRun = false
}

function TurtleUtil.getPosition()
    return TurtleUtil.lap.position()
end

function TurtleUtil.getDirection()
    return TurtleUtil.lap.direction()
end

TurtleUtil.directionMapping = {
    NORTH = {EAST = "turnRight", SOUTH = "turnAround", WEST = "turnLeft"},
    SOUTH = {EAST = "turnLeft", WEST = "turnRight", NORTH = "turnAround"},
    WEST = {NORTH = "turnRight", SOUTH = "turnLeft", EAST = "turnAround"},
    EAST = {NORTH = "turnLeft", SOUTH = "turnRight", WEST = "turnAround"}
}

function TurtleUtil.turnToDirection(targetDirection)
    if TurtleUtil.dryRun then
        return
    end

    local currentDirection = TurtleUtil.getDirection()
    local turnFunction = TurtleUtil.directionMapping[currentDirection][targetDirection]

    if turnFunction == "turnRight" then
        turtle.turnRight()
    elseif turnFunction == "turnLeft" then
        turtle.turnLeft()
    elseif turnFunction == "turnAround" then
        turtle.turnRight()
        turtle.turnRight()
    end
end

function TurtleUtil.doMovement(action, postMoveCallback)
    local success = false
    local reason = nil

    if action == 'up' then
        if TurtleUtil.dryRun then
            success = true
            reason = nil
        else
            success, reason = turtle.up()
        end
    elseif action == 'down' then
        if TurtleUtil.dryRun then
            success = true
            reason = nil
        else
            success, reason = turtle.down()
        end
    else
        if TurtleUtil.dryRun then
            success = true
            reason = nil
        else
            success, reason = turtle.forward()
        end
    end

    if postMoveCallback ~= nil then
        postMoveCallback(success, reason, action)
    end
end

function TurtleUtil.moveTo(targetX, targetY, targetZ, postMoveCallback)
    local currentPosition = TurtleUtil.getPosition()
    local xOffset = targetX - currentPosition.x
    local yOffset = targetY - currentPosition.y
    local zOffset = targetZ - currentPosition.z
    TurtleUtil.moveToOffset(xOffset, yOffset, zOffset, postMoveCallback)
end

function TurtleUtil.moveInXDirection(diffX, postMoveCallback)
    if diffX ~= 0 then
        TurtleUtil.turnToDirection(diffX > 0 and "EAST" or "WEST")
        for _ = 1, math.abs(diffX) do
            TurtleUtil.doMovement("forward", postMoveCallback)
        end
    end
end

function TurtleUtil.moveInYDirection(diffY, postMoveCallback)
    for _ = 1, math.abs(diffY) do
        if diffY > 0 then
            TurtleUtil.doMovement("up", postMoveCallback)
        else
            TurtleUtil.doMovement("down", postMoveCallback)
        end
    end
end

function TurtleUtil.moveInZDirection(diffZ, postMoveCallback)
    if diffZ ~= 0 then
        TurtleUtil.turnToDirection(diffZ > 0 and "SOUTH" or "NORTH")
        for _ = 1, math.abs(diffZ) do
            TurtleUtil.doMovement("forward", postMoveCallback)
        end
    end
end

function TurtleUtil.moveToByAxis(targetX, targetY, targetZ, postMoveCallback)
    local currentPosition = TurtleUtil.getPosition()
    TurtleUtil.moveInXDirection(targetX - currentPosition.x, postMoveCallback)
    TurtleUtil.moveInYDirection(targetY - currentPosition.y, postMoveCallback)
    TurtleUtil.moveInZDirection(targetZ - currentPosition.z, postMoveCallback)
end

function TurtleUtil.moveToOffsetByAxis(xOffset, yOffset, zOffset, postMoveCallback)
    local currentPosition = TurtleUtil.getPosition()
    TurtleUtil.moveToByAxis(currentPosition.x + xOffset, currentPosition.y + yOffset, currentPosition.z + zOffset, postMoveCallback)
end

function TurtleUtil.moveToOffset(xOffset, yOffset, zOffset, postMoveCallback)
    local steps = {
        x = {count = math.abs(xOffset), direction = xOffset > 0 and "EAST" or "WEST"},
        y = {count = math.abs(yOffset), direction = yOffset > 0 and "UP" or "DOWN"},
        z = {count = math.abs(zOffset), direction = zOffset > 0 and "SOUTH" or "NORTH"}
    }

    local maxSteps = math.max(steps.x.count, steps.y.count, steps.z.count)

    for _ = 1, maxSteps do
        for _, axis in pairs({"x", "y", "z"}) do
            if steps[axis].count > 0 then
                if axis == "y" then
                    if steps[axis].direction == "UP" then
                        TurtleUtil.doMovement("up", postMoveCallback)
                    else
                        TurtleUtil.doMovement("down", postMoveCallback)
                    end
                else
                    if TurtleUtil.getDirection() ~= steps[axis].direction then
                        TurtleUtil.turnToDirection(steps[axis].direction)
                    end
                    TurtleUtil.doMovement("forward", postMoveCallback)
                end
                steps[axis].count = steps[axis].count - 1
            end
        end
    end
end

return TurtleUtil
