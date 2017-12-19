package com.github.whileloop.rest4j.handler;

import com.github.whileloop.rest4j.Handler;
import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;

import static com.github.whileloop.rest4j.HttpStatus.NOT_FOUND;
import static com.github.whileloop.rest4j.HttpStatus.OK;


public class FileHandler implements Handler {

    private String prefix;
    private String root;
    private Logger logger = LoggerFactory.getLogger(FileHandler.class);
    private FileSystem fs;

    public FileHandler(String root) throws IOException, URISyntaxException {
        this(root, "");
    }

    public FileHandler(String rootDir, String strippedPrefix) throws IOException, URISyntaxException {
        this.prefix = strippedPrefix;

        URI uri = new URI(rootDir);
        if (uri.getScheme() == null || !uri.getScheme().equals("jar")) { // regular fs
            fs = FileSystems.getDefault();
            String regex = "file:";
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // windows abs paths should not start with a `/`..
                // for example.. "/C:/Program Files/My/Program" would throw an error. drop the first `/`
                regex += "/";
            }
            root = rootDir.replaceFirst(regex, "");
        } else { // jar
            String[] paths = uri.toString().split("!");
            uri = URI.create(paths[0]);
            try {
                fs = FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException e) {
                fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
            }
            if (fs == null) {
                throw new IOException("failed to get jar file system");
            }
            root = paths[1];
        }
        logger.info("Serving static resources from: " + root);
    }

    private Path getPath(String path) {
        return fs.getPath(root, path);

    }

    public void close() {
        try {
            fs.close();
        } catch (IOException e) {
            logger.warn(e.toString(), e);
        }
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {
        String path = req.getUrl().getPath();
        if (path.startsWith(prefix)) {
            path = path.substring(prefix.length());
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (path.isEmpty() || path.equals("/")) {
            path = "index.html";
        }

        String content;
        Path dest = getPath(path);
        try {
            content = is2String(Files.newInputStream(dest));
        } catch (NoSuchFileException e) {
            resp.error(NOT_FOUND, "not found: " + path);
            return;
        }

        String ct = Files.probeContentType(dest.getFileName());
        if (ct == null || ct.isEmpty()) { // try again
            ct = URLConnection.guessContentTypeFromName(dest.getFileName().toString());
        }

        if (ct != null) {
            resp.headers.set("Content-Type", ct);
        }

        resp.writeHeader(OK);
        resp.write(content);
    }

    private static String is2String(InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is, StandardCharsets.UTF_8.name())) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
