local menu = require("rom.modules.menu.simple")

local function checkFuel()
    local fuelLevel = turtle.getFuelLevel()
    local maxFuelLevel = turtle.getFuelLimit()
    local percentFull = (fuelLevel / maxFuelLevel) * 100
    local text = string.format("%d / %d (%d%%)\n", fuelLevel, maxFuelLevel, percentFull)
    write(text)
    menu.run()
end

local MAX_INVENTORY_SLOTS = 16

local function refuelFromInventorySlot(inventorySlot)
    turtle.select(inventorySlot)
    turtle.refuel()
end

local function refuelFromInventory()
    for inventorySlot = 1, MAX_INVENTORY_SLOTS do
        if turtle.getFuelLevel == turtle.getFuelLimit() then
            write("The turtle is fully fueled.\n")
            break
        end
        refuelFromInventorySlot(inventorySlot)
    end

    checkFuel()
    menu.run()
end

menu.addCompositeItem("0", "Reprint this menu", menu.run)
menu.addCompositeItem("1", "Check fuel level", checkFuel)
menu.addCompositeItem("2", "Refuel from entire inventory", refuelFromInventory)

FuelMenu = {
    menu = menu,
    run = menu.run
}

return FuelMenu
