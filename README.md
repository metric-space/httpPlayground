### Goal: place to fool around with http requests

Includes a luke-modified-combinator(somewhat) library to simplify operations on requests

### How to work it

1. open two terminals and navigate to the directory in both
2. run `activator run` in one to start the local server in one of the terminals @term1
3. run `activator console` in the other @term2
4. @term2 put in the following (assuming u=your port is at 9000)

```
>>> import weirdCombinators._

>>> val url:String = "http://localhost:9000" 

>>> val response = liftez(url) <*> ("egg","chicken") <||> "GET" <**> "status"

>>> println(response.statusText)
```
It should be noted that either of the following should work

```
>>> liftez(url) <*> ("egg","chicken") <||> "GET" <**> 

>>> liftez(url) <||> GET <*> ("egg","chicken") <*> ("smash","bros") <**>"

>>> liftez(url) <*> ("egg","chicken") <||> "GET" <*>("smash","bros") <**>

```
If you want even to extract a certain response field in the same expression without saving the response object, you could go

```
>>> liftez(url) <*> ("egg","chicken") <||> "GET" <*>("smash","bros") </~\> "status"

```

### Important

This mini library uses NingWSClient object and need to be shut down explicitly
so after you are done call `QUIT` and exit normally(Ctrl+D)
