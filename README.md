Rest4j
==========

<p align="center">
  <a href="https://godoc.org/github.com/while-loop/rest4j"><img src="https://img.shields.io/badge/godoc-reference-blue.svg?style=flat-square"></a>
  <a href="https://travis-ci.org/while-loop/rest4j"><img src="https://img.shields.io/travis/while-loop/rest4j.svg?style=flat-square"></a>
  <a href="https://mvnrepository.com/artifact/com.github.while-loop/rest4j"><img src="https://maven-badges.herokuapp.com/maven-central/com.github.while-loop/rest4j/badge.svg?style=flat-square"></a>
  <a href="https://coveralls.io/github/while-loop/rest4j"><img src="https://img.shields.io/coveralls/while-loop/rest4j.svg?style=flat-square"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-Apache 2.0-blue.svg?style=flat-square"></a>
</p>

HTTP wrapper that sits on top of different HTTP Server implementations.
Bring Your Own Server (BYOS).

Inspiration from the Go stdlib http package.

Download
--------

#### Gradle
```gradle
dependencies {
    compile 'com.github.while-loop:rest4j:1.0.0-SNAPSHOT'
}
```

#### Maven
```maven
<dependency>
    <groupId>com.github.while-loop</groupId>
    <artifactId>rest4j</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Implemented Servers
-------------------

- com.sun.net.[HttpServer](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html)

Usage
-----

Example [UserService.java](src/test/java/UserService.java)

```java
public class Example {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        SunRouter r = new SunRouter();
        r.handle("/hello", (req, resp) -> {
            String raddr = req.remoteAddr();

            resp.headers.set("Content-Type", "text/plain");
            resp.write("hello " + raddr + "!!");
        });
        r.handle("/{uuid}", (req, resp) -> {
            String body = is2String(req);
            // do something with body

            resp.writeHeader(ACCEPTED);
        }).setMethods(PUT);

        server.createContext("/", r);
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
    }

    private static String is2String(InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is, StandardCharsets.UTF_8.name())) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }
}
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
