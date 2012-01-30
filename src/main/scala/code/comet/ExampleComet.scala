package code
package comet

import net.liftweb._
  import common._
  import http._
    import js._
      import JE._
      import JsCmds._
    import SHtml._
  import builtin.snippet._
  import actor._
  import util._
    import Helpers._

case class TriggerBadJavascript(callback:(String)=>JsCmd) extends JsCmd {
  override val toJsCmd =
    Function(
      "triggerBadJavascript", List(),
      ajaxCall(JsRaw("Array.prototype.slice.call(arguments).join('|')"), callback)._2
    ).toJsCmd
}

case object Message

class ExampleComet extends CometActor {
  
  override def defaultPrefix = Full("comet")
  
  def triggerBadJavascript(s:String) = {
    Call("alert", "This line won't transmit with a semicolon.") &
    Call("alert", "This line will transmit with a semicolon.")
  }
  
  override def render = Noop
  
  override def localSetup = {
    println("COMET SETUP")
    Schedule.schedule(this, Message, 5000L)
  }
  
  override def lowPriority = {
    case Message => {
      println("HAI")
      partialUpdate(TriggerBadJavascript(triggerBadJavascript))
      Schedule.schedule(this, Message, 5000L)
    }
  }
}
