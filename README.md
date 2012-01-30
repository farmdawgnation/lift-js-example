# Semicolons Gone Missing

This project demonstrates behavior we've observed where we had expected Lift to send down semicolons
with some custom JsCmds, and it did not. This may not be unintended behavior, but it wasn't something
we expected - so this might be a case where some added documentation or the like may be appropriate.

## Running this Project

This project was built with SBT 0.7.

As expected, updating the internal sbt goodness is doable with `sbt update` and to build and run the project, 
simply run `sbt compile && sbt` and then `jetty-run` at the prompt. Navigate to localhost:8080 and open up
the Javascript Console and do whatever is required to turn on Network Monitoring for your browser.

For Chrome this is as simple as opening up the developer tools and clicking over to the tabs to make sure they
are turned on. (If they aren't you'll get a message telling you about how to turn them on.) Then from the Javascript
console type the following command and press enter:

```javascript
triggerBadJavascript();
```

This will trigger an AJAX request that will return two alert commands back to the browser, both lacking a semicolon
on the end of the line. You'll be able to view the returned content in the network tab.

## Our Investigation

On [line 197 of JsCmds.scala](https://github.com/lift/framework/blob/master/web/webkit/src/main/scala/net/liftweb/http/js/JsCommands.scala#L197)
semicolons are manually added onto the end of the toJsCmd vals.

We've determined that when we implement custom JsCmds such as BadJs in this example project, we can avoid running into
semicolon issues by changing our toJsCmd val to call the `cmd` method first.

So, in our project we have this line in BadJs:

```scala
val toJsCmd = Call("alert", text).toJsCmd
```

And changing it to this eradicates the bug:

```scala
val toJsCmd = Call("alert", text).cmd.toJsCmd
```
