package code
package comet

import net.liftweb._
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
  def triggerBadJavascript(s:String) = {
    Call("alert", "This line won't transmit with a semicolon.") &
    Call("alert", "This line will transmit with a semicolon.")
  }
  
  override def render = bind("message" -> <span id="message">Whatever you feel like returning</span>)
  
  ActorPing.schedule(this, Message, 10000L)
  
  override def lowPriority = {
    case Message => {
      println("HAI")
      ActorPing.schedule(this, Message, 10000L)
    }
  }
}
