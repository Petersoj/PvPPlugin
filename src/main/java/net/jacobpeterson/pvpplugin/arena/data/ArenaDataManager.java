package net.jacobpeterson.pvpplugin.arena.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.jacobpeterson.pvpplugin.PvPPlugin;
import net.jacobpeterson.pvpplugin.arena.Arena;
import net.jacobpeterson.pvpplugin.arena.ArenaManager;
import net.jacobpeterson.pvpplugin.arena.arenas.ffa.FFAArena;
import net.jacobpeterson.pvpplugin.arena.arenas.ranked1v1.Ranked1v1Arena;
import net.jacobpeterson.pvpplugin.arena.arenas.team2v2.Team2v2Arena;
import net.jacobpeterson.pvpplugin.data.GsonManager;
import net.jacobpeterson.pvpplugin.util.Initializers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ArenaDataManager implements Initializers {

    private static final String FFA_ARENA_KEY = "ffa";
    private static final String RANKED_1V1_ARENAS_KEY = "ranked_1v1";
    private static final String TEAM_2V2_ARENAS = "team_2v2";
    private static final Type RANKED_1V1_ARENAS_TYPE = new TypeToken<ArrayList<Ranked1v1Arena>>() {
    }.getType();
    private static final Type TEAM_2V2_ARENAS_TYPE = new TypeToken<ArrayList<Team2v2Arena>>() {
    }.getType();

    private final Logger LOGGER;
    private ArenaManager arenaManager;
    private GsonManager gsonManager;
    private File arenaDataFile;

    /**
     * Instantiates a new Arena Data Manager which is used to read/write data for {@link Arena}.
     *
     * @param arenaManager the arena manager
     */
    public ArenaDataManager(ArenaManager arenaManager, GsonManager gsonManager) {
        this.LOGGER = PvPPlugin.getPluginLogger();
        this.arenaManager = arenaManager;
        this.gsonManager = gsonManager;
    }

    @Override
    public void init() throws IOException {
        File pluginDataFolder = arenaManager.getPvPPlugin().getDataFolder();
        this.arenaDataFile = new File(pluginDataFolder, "arenas.json");

        try {
            this.loadArenas();

            LOGGER.info("Loaded Arenas");
        } catch (FileNotFoundException exception) {
            LOGGER.warning(arenaDataFile.getName() + " file not found - No Arenas loaded!");
        }
    }

    @Override
    public void deinit() throws IOException {
        this.saveArenas();
    }

    /**
     * Save arenas to JSON in {@link ArenaDataManager#getArenaDataFile()}.
     * Should probably be called after an arena is added or modified
     *
     * @throws IOException the io exception
     */
    public void saveArenas() throws IOException {
        Gson prettyGson = gsonManager.getPrettyGson();

        ArenaSerializer arenaSerializer = gsonManager.getArenaSerializer();
        arenaSerializer.setReferenceSerialization(false); // This will serialize the entire object via Gson

        JsonObject arenasObject = new JsonObject();
        JsonElement ffaArenaJson = prettyGson.toJsonTree(arenaManager.getFFAArena());
        JsonElement ranked1v1ArenasJsonArray = prettyGson.toJsonTree(arenaManager.getRanked1v1Arenas());
        JsonElement team2v2ArenasJsonArray = prettyGson.toJsonTree(arenaManager.getTeam2v2Arenas());

        arenasObject.add(FFA_ARENA_KEY, ffaArenaJson);
        arenasObject.add(RANKED_1V1_ARENAS_KEY, ranked1v1ArenasJsonArray);
        arenasObject.add(TEAM_2V2_ARENAS, team2v2ArenasJsonArray);

        byte[] jsonBytes = prettyGson.toJson(arenasObject).getBytes(StandardCharsets.UTF_8);

        Files.write(arenaDataFile.toPath(), jsonBytes); // Default OpenOptions are fine here
    }

    /**
     * Load arenas from JSON in {@link ArenaDataManager#getArenaDataFile()}.
     *
     * @throws IOException           the io exception
     * @throws ClassCastException    the class cast exception
     * @throws FileNotFoundException the file not found exception
     */
    @SuppressWarnings("unchecked") // Easier to ignore cast checking and let the runtime throw the exception if so
    public void loadArenas() throws IOException, ClassCastException, FileNotFoundException {
        Gson gson = gsonManager.getGson();

        ArenaSerializer arenaSerializer = gsonManager.getArenaSerializer();
        arenaSerializer.setReferenceDeserialization(false); // This will deserialize the entire object via Gson

        JsonReader jsonReader = new JsonReader(new FileReader(arenaDataFile));

        JsonElement arenasElement = gsonManager.getJsonParser().parse(jsonReader);
        if (!(arenasElement instanceof JsonObject)) {
            throw new JsonParseException(arenaDataFile.getName() + " JSON is a JSON Object!");
        }

        JsonObject arenasObject = (JsonObject) arenasElement;

        FFAArena ffaArena = gson.fromJson(arenasObject.getAsJsonObject(FFA_ARENA_KEY), FFAArena.class);
        ArrayList<Ranked1v1Arena> ranked1v1Arenas = gson.fromJson(arenasObject.getAsJsonArray(RANKED_1V1_ARENAS_KEY),
                RANKED_1V1_ARENAS_TYPE);
        ArrayList<Team2v2Arena> team2v2Arenas = gson.fromJson(arenasObject.getAsJsonArray(TEAM_2V2_ARENAS),
                TEAM_2V2_ARENAS_TYPE);

        arenaManager.setFFAArena(ffaArena);
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
