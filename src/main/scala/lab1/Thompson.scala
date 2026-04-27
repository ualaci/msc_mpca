package lab1

object Thompson:

  def regexToNfae(regex: RegexAST): NFAe =
    val (nfa, _) = build(regex, 0)
    
    def getAlphabet(ast: RegexAST): Set[String] = ast match
      case Literal(c) => Set(c.toString)
      case Concat(l, r) => getAlphabet(l) ++ getAlphabet(r)
      case Union(l, r) => getAlphabet(l) ++ getAlphabet(r)
      case Star(i) => getAlphabet(i)
      case Plus(i) => getAlphabet(i)
      case Optional(i) => getAlphabet(i)

    val alpha = getAlphabet(regex).toList.sorted
    val allStates = nfa.states.distinct.sorted
    
    NFAe(
      alphabet = alpha,
      states = allStates,
      initial_state = nfa.initial,
      final_states = List(nfa.fin),
      transitions = nfa.trans
    )

  private case class PartialNFA(initial: State, fin: State, states: List[State], trans: List[Transition])

  private def build(ast: RegexAST, id: Int): (PartialNFA, Int) = ast match
    case Literal(c) =>
      val s1 = s"q$id"
      val s2 = s"q${id+1}"
      val t = Transition(s1, c.toString, List(s2))
      (PartialNFA(s1, s2, List(s1, s2), List(t)), id + 2)

    case Concat(left, right) =>
      val (n1, id1) = build(left, id)
      val (n2, id2) = build(right, id1)
      val t = Transition(n1.fin, "epsilon", List(n2.initial))
      (PartialNFA(n1.initial, n2.fin, n1.states ++ n2.states, n1.trans ++ n2.trans :+ t), id2)

    case Union(left, right) =>
      val (n1, id1) = build(left, id)
      val (n2, id2) = build(right, id1)
      val s0 = s"q$id2"
      val sf = s"q${id2+1}"
      val t1 = Transition(s0, "epsilon", List(n1.initial, n2.initial))
      val t2 = Transition(n1.fin, "epsilon", List(sf))
      val t3 = Transition(n2.fin, "epsilon", List(sf))
      (PartialNFA(s0, sf, n1.states ++ n2.states ++ List(s0, sf), n1.trans ++ n2.trans ++ List(t1, t2, t3)), id2 + 2)

    case Star(inner) =>
      val (n1, id1) = build(inner, id)
      val s0 = s"q$id1"
      val sf = s"q${id1+1}"
      val t1 = Transition(s0, "epsilon", List(n1.initial, sf))
      val t2 = Transition(n1.fin, "epsilon", List(n1.initial, sf))
      (PartialNFA(s0, sf, n1.states ++ List(s0, sf), n1.trans ++ List(t1, t2)), id1 + 2)

    case Plus(inner) =>
      val (n1, id1) = build(inner, id)
      val (n2, id2) = build(Star(inner), id1)
      val t = Transition(n1.fin, "epsilon", List(n2.initial))
      (PartialNFA(n1.initial, n2.fin, n1.states ++ n2.states, n1.trans ++ n2.trans :+ t), id2)

    case Optional(inner) =>
      val (n1, id1) = build(inner, id)
      val s0 = s"q$id1"
      val sf = s"q${id1+1}"
      val t1 = Transition(s0, "epsilon", List(n1.initial, sf))
      val t2 = Transition(n1.fin, "epsilon", List(sf))
      (PartialNFA(s0, sf, n1.states ++ List(s0, sf), n1.trans ++ List(t1, t2)), id1 + 2)
