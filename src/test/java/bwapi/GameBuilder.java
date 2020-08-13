package bwapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.InflaterOutputStream;

public class GameBuilder {

    public final static String DEFAULT_MAP_FILE = "(2)Benzene.scx";
    public final static String DEFAULT_BUFFER_PATH = "src/test/resources/" + DEFAULT_MAP_FILE + "_frame0_buffer.bin";

    public static Game createGame() throws IOException {
        return createGame(DEFAULT_MAP_FILE);
    }

    public static Game createGame(String mapName) throws IOException {
        final ByteBuffer buffer = binToBuffer(DEFAULT_BUFFER_PATH);
        return createGame(new Client(buffer));
    }

    public static Game createGame(Client client) {
        final Game game = new Game(client.clientData());
        game.init();
        return game;
    }

    public static Game createGameUnchecked() {
        try {
            return GameBuilder.createGame();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static ByteBuffer binToBuffer(String binLocation) throws IOException {
        final byte[] compressedBytes = Files.readAllBytes(Paths.get(binLocation));
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final InflaterOutputStream zin = new InflaterOutputStream(out);
        zin.write(compressedBytes);
        zin.flush();
        zin.close();
        final byte[] bytes = out.toByteArray();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.put(bytes);
        return buffer;
    }

    public static ByteBuffer binToBufferUnchecked(String binLocation) {
        try {
            return binToBuffer(binLocation);
        } catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
