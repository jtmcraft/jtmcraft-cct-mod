local SimpleMenu = {
    menuItems = {}
}

function SimpleMenu.addItem(item)
    table.insert(SimpleMenu.menuItems, item)
end

function SimpleMenu.addCompositeItem(itemOption, itemName, itemFunction)
    table.insert(SimpleMenu.menuItems, {
        itemOption = itemOption,
        itemName = itemName,
        itemFunction = itemFunction
    })
end

function SimpleMenu.printMenu()
    for _, menuItem in ipairs(SimpleMenu.menuItems) do
        local text = string.format("%s: %s", menuItem["itemOption"], menuItem["itemName"])
        print(text)
    end
end

local function processUserInput(user_input)
    local isValidOption = false
    for _, menuItem in ipairs(SimpleMenu.menuItems) do
        if user_input == menuItem["itemOption"] then
            menuItem["itemFunction"]()
            isValidOption = true
            break
        end
    end

    if not isValidOption and user_input ~= "q" then
        print("Invalid option. 'q' to exit menu.\n")
    end

    return user_input == "q" or isValidOption
end

function SimpleMenu.run()
    local user_input
    repeat
        SimpleMenu.printMenu()
        user_input = read()
    until processUserInput(user_input)
end

return SimpleMenu
