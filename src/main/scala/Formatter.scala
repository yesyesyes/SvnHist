import scala.xml.{Elem, NodeSeq, PrettyPrinter}

object Graph {

  sealed abstract case class Graph[+A] { def toXML: Option[Elem] }
  case object E extends Graph[Nothing] { override def toXML = None }
  case class Node[+A](a: A, children: List[Graph[A]]) extends Graph[A] {
    override def toXML = Some(<elem><val>{ a }</val>{
      (children.foldLeft(Nil: NodeSeq)((x, y) =>
        x ++ y.toXML.getOrElse(NodeSeq.Empty))) match {
          case n@NodeSeq.Empty => n
          case n => <children>{n}</children>
        }}</elem>)
  }

  class GraphHlp[A](g: Graph[A]) {
    def aeq[B >: A](a: B) = (g, a) match {
      case (E, _) => false
      case (Node(a, _), b) => a == b
    }
  }
  implicit def GrapToGraphHlp[A](g: Graph[A]): GraphHlp[A] = new GraphHlp(g)
  
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
  
  def build(ls: List[String], sep: String) = ls.foldLeft(E: Graph[String]) { (x, y) => add(x, y split sep toList) }
  
  def XmlToString(e: Elem) = new PrettyPrinter(80, 2) format e

  def mkString[A](g: Graph[A], level: Int = 0, step: Int = 2): String = {
    val strS = (size: Int) => " " * size
    g match {
      case E => ""
      case Node(x, xs) => strS(level) + "|-" + x + "\n" + xs.foldLeft("") { (x, y) => x + mkString(y, level + step) }
    }
  }
  
}

