package net.fasilsmp.mods.jtmcraft_cct.lua;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.LuaValues;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.fasilsmp.mods.jtmcraft_cct.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerAwareFunctions<T> {
    private final MinecraftServer minecraftServer;
    private final Scoreboard scoreboard;
    private final Gson gson;
    private final T target;

    public ServerAwareFunctions(T target) {
        this.target = target;

        if (this.target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        }

        if (this.target instanceof ITurtleAccess turtleAccess) {
            minecraftServer = turtleAccess.getLevel().getServer();
        } else {
            throw new IllegalArgumentException("Invalid target type.");
        }

        if (minecraftServer == null) {
            throw new IllegalArgumentException("Target must return a server.");
        }

        scoreboard = minecraftServer.getScoreboard();
        gson = new Gson();
    }

    private static void validateWhoArgument(boolean badArgs, LuaException x) throws LuaException {
        if (badArgs) {
            throw x;
        }
    }

    private @NotNull String getDimension() {
        if (target instanceof ITurtleAccess turtle) {
            return turtle.getLevel().dimension().toString();
        }

        throw new InvalidTargetException();
    }

    private BlockPos getPosition() {
        if (target instanceof ITurtleAccess turtle) {
            return turtle.getPosition();
        }

        throw new InvalidTargetException();
    }

    @LuaFunction
    public final @NotNull MethodResult objectives() {
        if (scoreboard == null) {
            return MethodResult.of((Object) null);
        }

        Collection<Objective> scoreboardObjectives = scoreboard.getObjectives();
        if (scoreboardObjectives.isEmpty()) {
            return MethodResult.of(scoreboardObjectives);
        }

        Collection<Map<Object, Object>> objectives = new HashSet<>();
        scoreboardObjectives.forEach(objective -> {
            String name = objective.getName();
            String displayName = objective.getDisplayName().getString();
            ObjectiveCriteria criteria = objective.getCriteria();
            String criteriaName = criteria.getName();
            String criteriaRenderType = criteria.getDefaultRenderType().getId();
            Map<Object, Object> objectiveMap = new HashMap<>();
            objectiveMap.put("name", name);
            objectiveMap.put("display_name", displayName);
            objectiveMap.put("criteria_name", criteriaName);
            objectiveMap.put("criteria_render", criteriaRenderType);
            objectives.add(objectiveMap);
        });

        return MethodResult.of(objectives);
    }

    @LuaFunction
    public final @NotNull MethodResult objectiveNames() {
        if (scoreboard == null) {
            return MethodResult.of((Object) null);
        }

        return MethodResult.of(scoreboard.getObjectiveNames());
    }

    @LuaFunction
    public final @NotNull MethodResult trackedPlayers() {
        if (scoreboard == null) {
            return MethodResult.of((Object) null);
        }

        return MethodResult.of(scoreboard.getTrackedPlayers());
    }

    @LuaFunction
    public final @NotNull MethodResult playerScores(IArguments arguments) throws LuaException {
        if (scoreboard == null) {
            return MethodResult.of((Object) null);
        }

        String playerName = arguments.getString(0);
        boolean useDisplayName = arguments.count() != 2 || arguments.getBoolean(1);
        Collection<Map<Object, Object>> playerScores = new HashSet<>();
        Map<Objective, Score> playerScoresMap = scoreboard.getPlayerScores(playerName);
        playerScoresMap.forEach((objective, score) -> {
            Map<Object, Object> playerScore = new HashMap<>();
            playerScore.put("objective", useDisplayName ? objective.getDisplayName().getString() : objective.getName());
            playerScore.put("score", score.getScore());
            playerScores.add(playerScore);
        });

        return MethodResult.of(playerScores);
    }

    @LuaFunction
    public final @NotNull MethodResult teams() {
        Collection<PlayerTeam> playerTeams = scoreboard.getPlayerTeams();
        Collection<Map<Object, Object>> results = new ArrayList<>();
        playerTeams.forEach(playerTeam -> {
            String prefix = playerTeam.getPlayerPrefix().getString();
            String name = playerTeam.getName();
            String suffix = playerTeam.getPlayerSuffix().getString();
            Map<Object, Object> teamName = new HashMap<>();
            teamName.put("prefix", prefix);
            teamName.put("name", name);
            teamName.put("suffix", suffix);
            results.add(teamName);
        });

        return MethodResult.of(results);
    }

    @LuaFunction
    public final @NotNull MethodResult who(@NotNull IArguments arguments) throws LuaException {
        String sourceDimension;
        double x, y, z;
        boolean hasArguments = arguments.count() == 2;

        if (hasArguments) {
            validateWhoArgument(!"string".equals(arguments.getType(0)), LuaValues.badArgumentOf(arguments, 0, "string"));
            validateWhoArgument(!"table".equals(arguments.getType(1)), LuaValues.badArgumentOf(arguments, 1, "table"));
            sourceDimension = arguments.getString(0);
            Map<?, ?> sourcePosition = arguments.getTable(1);
            x = (double) sourcePosition.get("x");
            y = (double) sourcePosition.get("y");
            z = (double) sourcePosition.get("z");
        } else {
            sourceDimension = getDimension();
            BlockPos sourcePosition = getPosition();
            x = sourcePosition.getX();
            y = sourcePosition.getY();
            z = sourcePosition.getZ();
        }

        return MethodResult.of(getDimensions(sourceDimension, x, y, z));
    }

    @NotNull
    private Map<Object, Collection<Object>> getDimensions(String sourceDimension, double sourceX, double sourceY, double sourceZ) {
        Map<Object, Collection<Object>> dimensions = new HashMap<>();
        List<String> ops = List.of(minecraftServer.getPlayerList().getOpNames());
        Iterable<ServerLevel> serverLevels = minecraftServer.getAllLevels();
        serverLevels.forEach(serverLevel -> {
            String dimension = serverLevel.dimension().location().toString();
            Collection<Object> playersInDimension = new HashSet<>();
            serverLevel.players().forEach(serverPlayer -> {
                String playerName = serverPlayer.getName().getString();
                String op = ops.contains(playerName) ? "*" : " ";
                Vec3 targetPosition = serverPlayer.position();
                String distanceString = "";
                String directionString = "";

                if (dimension.equalsIgnoreCase(sourceDimension)) {
                    Vec3 sourcePosition = new Vec3(sourceX, sourceY, sourceZ);
                    double distance = sourcePosition.distanceTo(targetPosition);
                    distanceString = String.format(" %f block%s", distance, (distance == 1 ? "" : "s"));
                    int direction = MathUtil.directionFromOrigin(sourcePosition, targetPosition);
                    directionString = String.format(" %d deg", direction);
                }

                String positionString = String.format("[ %.2f, %.2f, %.2f ] %s%s",
                        targetPosition.x(), targetPosition.y(), targetPosition.z(), distanceString, directionString
                );
                playersInDimension.add(String.format("%s %s %s", op, playerName, positionString));
            });
            dimensions.put(dimension, playersInDimension);
        });
        return dimensions;
    }

    @LuaFunction
    public final @NotNull MethodResult statsFor(@NotNull IArguments arguments) throws LuaException {
        if (arguments.count() != 1) {
            return MethodResult.of((Object) null);
        }

        PlayerList playerList = minecraftServer.getPlayerList();
        ServerPlayer foundPlayer = null;
        for (ServerPlayer serverPlayer : playerList.getPlayers()) {
            if (arguments.getString(0).equalsIgnoreCase(serverPlayer.getGameProfile().getName())) {
                foundPlayer = serverPlayer;
                break;
            }
        }

        if (foundPlayer == null) {
            return MethodResult.of((Object) null);
        }

        String uuid = foundPlayer.getGameProfile().getId().toString();
        String fileName = String.format("./world/stats/%s.json", uuid);
        String dataJson;
        try {
            dataJson = FileUtils.readFileToString(new File(fileName), Charset.defaultCharset());
        } catch (IOException ioException) {
            throw new LuaException(ioException.getMessage());
        }
        JsonElement root = JsonParser.parseString(dataJson);
        JsonObject stats = root.getAsJsonObject().get("stats").getAsJsonObject();
        Set<String> statCategories = stats.keySet();
        Map<Object, Map<Object, Object>> statsMap = new HashMap<>();
        statCategories.forEach(statCategory -> {
            JsonObject category = stats.get(statCategory).getAsJsonObject();
            statsMap.put(statCategory, gson.fromJson(category, new TypeToken<Map<Object, Object>>() {
            }.getType()));
        });

        return MethodResult.of(statsMap);
    }
}
