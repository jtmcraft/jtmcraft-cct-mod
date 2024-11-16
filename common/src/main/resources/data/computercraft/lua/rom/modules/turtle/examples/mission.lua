local ShapedMission = require("rom.modules.turtle.shaped_mission")
local MissionExecutor = require("rom.modules.turtle.execute_mission")
local controller = require("rom.modules.turtle.controller")

local major = 4
local minor = 8
local shapedMission = ShapedMission:new()

local center = controller.getPosition()
center.y = center.y + 1
shapedMission:ellipse(center, major, minor)
print("Ellipse created successfully with " .. tostring(#shapedMission.points:values()) .. " points")
MissionExecutor.execute(shapedMission)
print("Done.")


-- function addCenter(point)
--     point.x = point.x + center.x
--     point.y = point.y + center.y
--     point.z = point.z + center.z
-- end

--shapedMission:rectangle(5, 3)
--shapedMission:postProcessPoints(addCenter)
--print("Rectangle created successfully with " .. tostring(#shapedMission.points:values()) .. " points")

--local pointsString = {}
--for i, point in pairs(shapedMission.points:values()) do
--    if i > 20 then
--        break
--    end
--    table.insert(pointsString, tostring(point))
--end
--
--print("Points: " .. table.concat(pointsString, ", "))

--MissionExecutor.execute(shapedMission)
--print("Mission executed successfully")
