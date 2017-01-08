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
- Open-ended Authentication
- Switchable Session Store
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
    // Carbon currently use Thymeleaf for template engine,
    // and you cannot change template engine sorry.
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

code looks like

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

pojo mapping
``` java
@Setter // you must implement setter.
@Getter
public class Nest {
  String string; // = aeiou
  int integer;   // = 123
}

@Setter
@Getter
public class SampleProps {
  String hoge; // = piyo
  Nest nest;
  List<String> list; // fuga1, fuga2, fuga3
}

@Component
public class UsingProps {
  @Inject
  private ConfigHolder config;
  public void useConf() {
    SampleProps props = config.findOne("sample", SampleProps.class);
    // or if you want get Nest props directly
    Nest nest = config.findOne("sample.nest", Nest.class);
  }
}
```


# Open-ended Authentication
code looks like below, and carbon-sample contains more detailed code.
``` java
@Component
public class SampleSecurityConfigAdapter implements SecurityConfigAdapter {

	// -----------------------------------------------------
	//                                               for Basic Auth
	//                                               -------
	@Inject
	private BasicAuthRequestMapper basicMapper;
	@Inject
	private SampleBasicAuthIdentifier basicIdentifier;
	@Inject
	private BasicAuthEvent basicFinisher;

	// -----------------------------------------------------
	//                                               for Form Auth
	//                                               -------
	@Inject
	private FormAuthRequestMapper formMapper;
	@Inject
	private FormAuthIdentifier formIdentifier;
	@Inject
	private FormAuthEvent formFinisher;

	@Override
	public void configure(SecurityConfiguration config) {
		config
			.define()
				.identifier(basicIdentifier)
				.base("/basic/")
				.endPoint(HttpMethod.GET, "/basic/**")
				.logout("/basic/logout")
				.redirect("/basic")
				.requestMapper(basicMapper)
				.finisher(basicFinisher)
			.end()
			.define()
				.identifier(formIdentifier)
				.base("/form/")
				.endPoint(HttpMethod.POST, "/form/auth")
				.logout("/form/logout")
				.redirect("/form")
				.requestMapper(formMapper)
				.finisher(formFinisher)
			.end()
			;
	}
}
```

- ```SecurityConfigAdapter```  
you can extend authentication logic fully by implement SecurityConfigAdapter.class
- ```identifier(AuthIdentifier<AuthIdentity> identifier)```  
represent authentication info that correspond to authentication logic
- ```base(String path)```  
under the /basic is protected
- ```endPoint(HttpMethod method, String path)```  
authorization end point. special character '**' is match all
- ```logout(String logoutPath)```  
- ```redirect()```  
represent redirect url, if no authorization
- ```requestMapper(RequestMapper mapper)```  
RequestMapper has an obligation to map Request to AuthInfo(i.e. extract username and password)
- ```finisher(AuthEventListener authEventListener)```  
handle OnAuth event and OnFail event. (i.e. if you want set additional session info, you can set it by AuthEventListener)


# Switchable Session Store
By default, carbon use ```InMemorySessionStore``` for storing session info.
However, you can use any session store you want, by implementing SessionStore, and register dependency.
Carbon provide ```RedisSessionStore.class``` that implements ```SessionStore.class```.
carbon-sample's code likes

config.yaml
``` yaml
sample:
  redis:
    host: 127.0.0.1
    port: 26379
```

SampleConfiguration.java
``` java
@Configuration
public class SampleConfiguration {
    @Inject
    private ConfigHolder config;
    @Component
    public SessionStore redisSession() {
        RedisConfig redis = config.findOne("sample.redis", RedisConfig.class);
        return new RedisSessionStore(redis.getHost(), redis.getPort());
    }
}
```

# WebSocket Support
Annotation based and pub-sub pattern web socket.  
code looks likes.   
(you can find ```package org.carbon.sample.web.message``` in carbon-sample fully detail code.)

``` java
@Component
@Socket(url = "/message/socket/{userName}/{roomId}")
public class MessageSocket {

    @OnOpen
    public void onConnect() {
        // do something ...
    }

    @OnClose
    public void onClose() {
        // do something ...
    }

    @OnReceive
    public MessageDto onReceive(@PathVariable("roomId") String roomId, Message message) {
        return new MessageDto(message.getFrom().getKey(), message.getContent());
    }

    @Channeled
    public ChannelConfiguration config(@PathVariable("userName") String userName,
                                       @PathVariable("roomId") String roomId) {
        return ChannelConfiguration.simple(roomId, userName);
    }
}
```
- ```@Socket```  
Represent WebSocket endpoint.
Argument ```url``` can be set path variable, and variable inject into method parameter (annotated by ```@OnOpen```, ```@OnClose```, ```@OnReceive```, ```@Channeld```)
- ```@OnOpen```  
Called when WebSocket start.
- ```@OnClose```  
Called when WebSocket close
- ```@OnReceive```  
Called when message is received.
```Message.class`` contains sender info and content.
You can return Anything by this method.
if this annotated method return ```object``` excepting ```String.class```,
```object``` is converted to json string.
- ```@Channeld```  
Configure ChannelConfiguration.
ChannelConfiguration represents
- 'the WebSocket endpoint identity'
and
- 'to'.
'the WebSocket endpoint identity' is put on ```Message.class```.
'to' is used for Where channel message send.
