```
  ___   _   ___ ___  ___  _  _
 / __| /_\ | _ | _ )/ _ \| \| |
| (__ / _ \|   | _ | (_) | .` |
 \___/_/ \_|_|_|___/\___/|_|\_|
```
# Carbon

Carbon is a java web framework inspired by Spring.

# Function
- Dependency Injection
- Mvc Application
- Type Safe Config
- Open-ended Security
- Switchable Session Store
- Template Engine Support
- Persistence Support
- WebSocket Support

# Dependency Injection
- class register
- method register

Register by Class
``` java
@Component
public class SomeComponent {
  // ...
}

@Component
public class Parent {
  @Inject
  private SomeComponent component;
}
```

Register by Method
``` java
@Configuration
public class Configuration {

  @Component
  public SomeComponent someComponent() {
    // ...
  }
}
```

# Mvc Application
code looks like
``` java
@Controller
public class IndexController {

    // service
    @Inject
    private BookService bookService;

    // === action[GET] ==========
    // Html Response Style
    @Action(url = "/books/{bookId}", method = HttpMethod.GET)
    public HtmlResponse booksDetailGet(@PathVariable("bookId") String bookId) {
        HtmlResponse response = new HtmlResponse("/path/to/template");

        // do something ...
        Book book = bookService.findBook(bookId);
        response.putData("key", book);

        return response;
    }

    // or json Style. you can return anything!
    @Action(url = "/api/books/{bookId}", method = HttpMethod.GET)
    public Object booksApiDetailsGet (@PathVariable("bookId") String bookId) {
        Book book = bookService.findBook(bookId);
        return book;
    }

    // === action[POST, PUT, DELETE] ==========
    @Action(url = "/api/books", method = HttpMethod.POST)
    public Object booksApiPost(@RequestCookie CookieEntity cookie,
                               @RequestBody RequestForm form) {
        // do something ...
        String code = cookie.getTrackingCode();
        LocalDate publishDate = form.getPublishDate();
    }

```

# Type Safe Config
 powerd by [snakeyaml](http://www.yahoo.co.jp/)
 config.yaml
 ``` yaml
 # common settings
 web:
  port: 7927
  resourceDirectory: static
  resourceOutPath: static
  maxHeaderSize: 512000 #500KB
  maxContentSize: 2097152 #2MB
persistent:
  implementation: jooq # jooq or hibernate
  dataSources:
    source:
      url: 127.0.0.1
      db: carbondb
      user: root
      password: password
      port: 23306
  autoddl: create # none, create-only, drop, create, create-drop, validate, and update
Logger:
  name: slf4j

# you can add any setting
sample:
  hoge: piyo
  nest:
    string: aeiou
    integer: 123
  list:
    - fuga1
    - fuga2
    - fuga3

 ```

map to pojo
``` java
public class Nest {
  String string; // = aeiou
  int integer;   // = 123
}
public class SampleProps {
  String hoge; // = piyo
  Nest nest;
  List<String> list; // fuga1, fuga2, fuga3
}
```

# Open-ended Security

# Switchable Session Store

# Template Engine Support

# Persistence Support

# WebSocket Support
