class Node(val value: String, val level: Int = 0, val parent: String) {
  import scala.collection.mutable.ListBuffer
  var children: ListBuffer[Node] = ListBuffer()

  override def equals(obj: Any): Boolean = obj match {
    case obj: Node => obj.value == this.value && obj.parent == this.parent
    case _         => false
  }
  override def hashCode: Int = 41 * value.hashCode * parent.hashCode

  def getLast(value: String): Option[Node] =
    if(this.value == value) return Some(this) else if(!children.isEmpty) return children.find(_.value == value) else None
}
object Node {
  def apply(value: String, level: Int, parent: String): Node = return new Node(value, level, parent)
  def attach(to: Node, what: Node): Node = { to.children += what; what }
}