sealed abstract case class Graph[+A] {
  def toStr: String
  def eqv[A](g: A): Boolean
}
case object E extends Graph[Nothing] {
  override def toStr = "."
  override def eqv[A](g: A) = false
}
case class Node[+A](a: A, children: List[Graph[A]]) extends Graph[A] {
  override def toStr = a + " " + children.map(_.toStr) + " "
  override def eqv[A](g: A) = a == g 
}

object Graph {

  def addd[A](g: Graph[A], ls: List[A]): Graph[A] = (g, ls) match {
    case (E, y :: ys) => Node(y, addd(E, ys) :: Nil);
    case (E, Nil) => E
    case (n: Node[A], Nil) => n
    case (Node(x, xs), y :: ys) =>
      val (head, tail) = if (x == y) (ys.head, ys.tail) else (y, ys)
      xs.find(_ eqv head) match {
        case None => Node(x, Node(head, addd(E, tail) :: Nil) :: xs)
        case Some(n) => Node(x, addd(n, tail) :: xs.filterNot(_ eqv head))
      }
  }
  
  def build(ls: List[String]) = ls.foldLeft(E: Graph[String]) { (x, y) => addd(x, y split "/" toList) }
}

