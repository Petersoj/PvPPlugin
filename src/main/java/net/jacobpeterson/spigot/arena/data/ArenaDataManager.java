package net.jacobpeterson.spigot.arena.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import net.jacobpeterson.spigot.arena.Arena;
import net.jacobpeterson.spigot.arena.ArenaManager;
import net.jacobpeterson.spigot.data.GsonManager;
import net.jacobpeterson.spigot.util.Initializers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class ArenaDataManager implements Initializers {

    private static final String FFA_ARENAS_KEY = "ffa";
    private static final String RANKED_1V1_ARENAS_KEY = "ranked_1v1";
    private static final String TEAM_2V2_ARENAS = "team_2v2";

    private ArenaManager arenaManager;
    private File arenaDataFile;

    /**
     * Instantiates a new Arena Data Manager which is used to read/write data for {@link Arena}.
     *
     * @param arenaManager the arena manager
     */
    public ArenaDataManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public void init() throws IOException {
        File pluginDataFolder = arenaManager.getPvPPlugin().getDataFolder();
        this.arenaDataFile = new File(pluginDataFolder, "arenas.json");

        this.loadArenas(arenaManager.getPvPPlugin().getGsonManager());
    }

    @Override
    public void deinit() {
    }

    /**
     * Save arenas to JSON in {@link ArenaDataManager#getArenaDataFile()}.
     * Should probably be called after an arena is added or modified
     *
     * @param gsonManager the gson manager
     */
    public void saveArenas(GsonManager gsonManager) throws IOException {
        Gson gson = gsonManager.getGson();

        JsonObject arenasObject = new JsonObject();
        JsonElement ffaArenasArray = gson.toJsonTree(arenaManager.getFFAArenas(),
                arenaManager.getFFAArenas().getClass());
        JsonElement ranked1v1ArenasArray = gson.toJsonTree(arenaManager.getRanked1v1Arenas(),
                arenaManager.getRanked1v1Arenas().getClass());
        JsonElement team2v2ArenasArray = gson.toJsonTree(arenaManager.getTeam2v2Arenas(),
                arenaManager.getTeam2v2Arenas().getClass());

        arenasObject.add(FFA_ARENAS_KEY, ffaArenasArray);
        arenasObject.add(RANKED_1V1_ARENAS_KEY, ranked1v1ArenasArray);
        arenasObject.add(TEAM_2V2_ARENAS, team2v2ArenasArray);

        byte[] jsonBytes = gson.toJson(arenasObject).getBytes(StandardCharsets.UTF_8);

        Files.write(arenaDataFile.toPath(), jsonBytes); // Default OpenOptions are fine here
    }

    /**
     * Load arenas from JSON in {@link ArenaDataManager#getArenaDataFile()}.
     *
     * @param gsonManager the gson manager
     */
    @SuppressWarnings("unchecked") // Easier to ignore cast checking and let the runtime throw the exception if there is
    public void loadArenas(GsonManager gsonManager) throws IOException, ClassCastException {
        Gson gson = gsonManager.getGson();

        JsonReader jsonReader = new JsonReader(new FileReader(arenaDataFile));

        JsonElement arenasElement = gsonManager.getJsonParser().parse(jsonReader);
        if (!(arenasElement instanceof JsonObject)) {
            throw new JsonParseException(arenaDataFile.getName() + " JSON is a JSON Object!");
        }

        JsonObject arenasObject = (JsonObject) arenasElement;

        ArrayList ffaArenas = gson.fromJson(arenasObject.getAsJsonArray(FFA_ARENAS_KEY),
                arenaManager.getFFAArenas().getClass());
        ArrayList ranked1v1Arenas = gson.fromJson(arenasObject.getAsJsonArray(RANKED_1V1_ARENAS_KEY),
                arenaManager.getRanked1v1Arenas().getClass());
        ArrayList team2v2Arenas = gson.fromJson(arenasObject.getAsJsonArray(TEAM_2V2_ARENAS),
                arenaManager.getTeam2v2Arenas().getClass());

        arenaManager.setFFAArenas(ffaArenas);
        arenaManager.setRanked1v1Arenas(ranked1v1Arenas);
        arenaManager.setTeam2v2Arenas(team2v2Arenas);
    }

    /**
     * Gets arena data file.
     *
     * @return the arena data file
     */
    public File getArenaDataFile() {
        return arenaDataFile;
    }
}
