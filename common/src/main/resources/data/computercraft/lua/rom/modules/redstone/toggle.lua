local Toggle = {}
local function is_state_high(state) return state end
local function is_state_low(state) return not state end
local DELAY = 1

local function toggle_output_if_needed(old_state, new_state, should_output_toggle, output_side)
    if new_state ~= old_state and should_output_toggle(old_state) then
        rs.setOutput(output_side, not rs.getOutput(output_side))
    end
    return new_state
end

local function toggle(input_side, output_side, should_output_change)
    local old_state = rs.getInput(input_side)
    while (true) do
        old_state = toggle_output_if_needed(old_state, rs.getInput(input_side), should_output_change, output_side)
        os.sleep(DELAY)
    end
end

function Toggle.high(input_side, output_side)
    toggle(input_side, output_side, is_state_low)
end

function Toggle.low(input_side, output_side)
    toggle(input_side, output_side, is_state_high)
end

return Toggle
