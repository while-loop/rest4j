Rest4j
==========

<p align="center">
  <a href="https://travis-ci.org/while-loop/rest4j"><img src="https://img.shields.io/travis/while-loop/rest4j.svg?style=flat-square"></a>
  <a href="https://mvnrepository.com/artifact/com.github.while-loop/rest4j"><img src="https://maven-badges.herokuapp.com/maven-central/com.github.while-loop/rest4j/badge.svg?style=flat-square"></a>
  <a href="https://coveralls.io/github/while-loop/rest4j"><img src="https://img.shields.io/coveralls/while-loop/rest4j.svg?style=flat-square"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-Apache 2.0-blue.svg?style=flat-square"></a>
</p>

HTTP wrapper that sits on top of different HTTP Server implementations.
Bring Your Own Server (BYOS).

Inspiration from the Go stdlib http package.

# Table of Contents
1. [Download](#download)
2. [Implemented Servers](#implemented-servers)
3. [Usage & Examples](#usage)
4. [Middleware](#middleware)
5. [Chaining Routers](#chaining-routers)
6. [Static resources](#static-resources)

Download
--------

#### Gradle
```gradle
dependencies {
    compile 'com.github.while-loop:rest4j:1.0.4'
}
```

#### Maven
```maven
<dependency>
    <groupId>com.github.while-loop</groupId>
    <artifactId>rest4j</artifactId>
    <version>1.0.4</version>
</dependency>
```

Implemented Servers
-------------------

- [com.sun.net.HttpServer](src/test/java/com/github/whileloop/rest4j/router/sun/SunRouterTest.java)
- [org.eclipse.jetty.server.Server](src/test/java/com/github/whileloop/rest4j/router/jetty/JettyRouterTest.java)

Usage
-----

For a full demo example, check the [integration](src/test/java/com/github/whileloop/rest4j/integration) package

```java
public class Example {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        Router r = new Router();
        r.handle("/", (req, resp) -> {
            String raddr = req.remoteAddr();

            resp.headers.set("Content-Type", "text/plain");
            resp.write("hello " + raddr + "!!");
        });
        r.handle("/:uuid", Example::update).setMethods(PUT);

        server.createContext("/", new SunRouter(r)); // BYOS
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
    }

    private static void update(HttpRequest req, HttpResponse resp) {
        String body = is2String(req);
        String uuid = req.getParam("uuid");
        // do something with body

        resp.writeHeader(ACCEPTED);
    }

    private static String is2String(InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is, StandardCharsets.UTF_8.name())) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
```

Middleware
----------

```java
public class LoggerMiddleware implements Middleware {
    private Logger logger;

    public LoggerMiddleware() {
        this(null);
    }

    public LoggerMiddleware(Logger logger) {
        if (logger == null) {
            logger = LoggerFactory.getLogger(LoggerMiddleware.class);
        }

        this.logger = logger;
    }

    @Override
    public Handler handle(Handler next) {
        return (req, resp) -> {
            // get the time before passing the request down the chain of middleware
            long start = System.currentTimeMillis();

            next.handle(req, resp); // apply the next handle

            long elapsed = System.currentTimeMillis() - start;
            logger.info(String.format("%-7s %-6s %d %s",
                    req.getMethod(), elapsed + "ms", resp.status.code(), req.getUrl().getPath()));
        };
    }
}
```

Chaining Routers
----------------


```java
Router r = new Router();
r.use(new LoggerMiddleware(),
        new JsonMiddleware(),
        new CorsMiddleware());

Router v1 = new Router();
Router usersR = new Router();
usersR.handle("/", this::getAll);                               // GET /v1/users
usersR.handle("/:uuid", this::updateUser).setMethods(PUT);     // PUT /v1/users/:uuid
usersR.handle("/:uuid", this::deleteUuser).setMethods(DELETE); // DELETE /v1/users/:uuid

Router postsR = new Router();
usersR.handle("/", this::getAll);                                   // GET /v1/posts
usersR.handle("/:postId", this::updatePost).setMethods(PUT);       // PUT /v1/posts/:uuid
usersR.handle("/:postId", this::deletePost).setMethods(DELETE);    // DELETE /v1/posts/:uuid

v1.handle("/users", usersR); // /v1/users
v1.handle("/posts", usersR); // /v1/posts
r.handle("/v1", v1); // /v1
```

Static Resources
----------------

```java
String dir = getClass().getClassLoader().getResource("www").toExternalForm();
FileHandler fh = new FileHandler(dir);
```

Changelog
---------

The format is based on [Keep a Changelog](http://keepachangelog.com/) 
and this project adheres to [Semantic Versioning](http://semver.org/).

[CHANGELOG.md](CHANGELOG.md)

License
-------
rest4j is licensed under the Apache 2.0 License.
See [LICENSE](LICENSE) for details.

Author
------

Anthony Alves
