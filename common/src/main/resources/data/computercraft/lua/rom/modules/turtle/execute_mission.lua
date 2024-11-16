local controller = require("rom.modules.turtle.controller")

local MissionExecutor = {}

function MissionExecutor.calculateRequirements(mission)
    local fuelRequired = 0

    local moveCallback = function(success)
        if success then
            fuelRequired = fuelRequired + 1
        end
    end

    controller.dryRun = true

    for _, point in pairs(mission.points:values()) do
        controller.moveTo(point.x, point.y, point.z, moveCallback)
    end

    controller.dryRun = false

    return fuelRequired
end

function MissionExecutor.verifyFuel(fuelRequired)
    local fuel = turtle.getFuelLevel()

    if fuelRequired <= fuel then
        print("Sufficient fuel.")
        return true
    else
        print("Insufficient fuel. Needs " .. tostring(fuelRequired - fuel))
        return false
    end
end

function MissionExecutor.verifyRequirements(mission)
    local fuelRequired = MissionExecutor.calculateRequirements(mission)
    local sufficientFuel = MissionExecutor.verifyFuel(fuelRequired)
    return sufficientFuel
end

function MissionExecutor.execute(mission)
    local ready = MissionExecutor.verifyRequirements(mission)

    if not ready then
        print("Mission cancelled.")
        return
    end

    local currentSlot = 1
    local waitingForInventory = false
    turtle.select(currentSlot)

    for _, point in pairs(mission.points:values()) do
        while currentSlot <= 16 and turtle.getItemCount() == 0 do
            currentSlot = currentSlot + 1
            if currentSlot <= 16 then
                turtle.select(currentSlot)
            end
            if currentSlot > 16 then
                waitingForInventory = true
            end
        end

        while waitingForInventory do
            print("Waiting for inventory addition.")
            os.pullEvent("turtle_inventory")
            print("Continuing.")
            currentSlot = 1
            if currentSlot <= 16 then
                turtle.select(currentSlot)
                if turtle.getItemCount() > 0 then
                    waitingForInventory = false
                end
            end
        end

        if waitingForInventory == false then
            controller.moveTo(point.x, point.y, point.z)
            if turtle.getItemCount() > 0 then
                turtle.placeDown()
            end
        end
    end
end

return MissionExecutor
