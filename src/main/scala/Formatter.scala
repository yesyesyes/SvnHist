import scala.xml.{Elem, NodeSeq, PrettyPrinter}

sealed abstract case class Graph[+A] { def toXML: Elem }
case object E extends Graph[Nothing] { override def toXML = <end></end> }
case class Node[+A](a: A, children: List[Graph[A]]) extends Graph[A] {
  override def toXML = <elem><val>{a}</val><children>{children.foldLeft(Nil: NodeSeq)((x, y) => x ++ y.toXML)}</children></elem>
}
object Graph {

  class GraphEq[A](g: Graph[A]) {
    def aeq[B >: A](a: B) = (g, a) match {
      case (E, _) => false
      case (Node(a, _), b) => a == b
    }
  }
  implicit def GrapToGraphEq[A](g: Graph[A]): GraphEq[A] = new GraphEq(g)
  
  def add[A](g: Graph[A], ls: List[A]): Graph[A] = (g, ls) match {
    case (E, y :: ys) => Node(y, add(E, ys) :: Nil);
    case (E, Nil) => E
    case (n: Node[A], Nil) => n
    case (Node(x, xs), y :: ys) =>
      val (head, tail) = if (x == y) (ys head, ys tail) else (y, ys)
      xs find(_ aeq head) match {
        case None => Node(x, Node(head, add(E, tail) :: Nil) :: xs)
        case Some(n) => Node(x, add(n, tail) :: xs.filterNot(_ aeq head))
      }
  }
  
  def build(ls: List[String], sep: Char = '/') = ls.foldLeft(E: Graph[String]) { (x, y) => add(x, y split sep toList) }
  
  def XmlToString(e: Elem) = new PrettyPrinter(80, 2) format e
}

