sealed abstract case class Graph[+A] 
case object E extends Graph[Nothing] 
case class Node[+A](a: A, children: List[Graph[A]]) extends Graph[A]

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
      val (head, tail) = if (x == y) (ys.head, ys.tail) else (y, ys)
      xs.find(_ aeq head) match {
        case None => Node(x, Node(head, add(E, tail) :: Nil) :: xs)
        case Some(n) => Node(x, add(n, tail) :: xs.filterNot(_ aeq head))
      }
  }
  
  def build(ls: List[String]) = ls.foldLeft(E: Graph[String]) { (x, y) => add(x, y split "/" toList) }
  
}

