package lab1

object Converter:

  def epsilonClosure(state: State, transitions: List[Transition]): Set[State] =
    def loop(current: Set[State], visited: Set[State]): Set[State] =
      val unvisited = current.diff(visited)
      if unvisited.isEmpty then visited
      else
        val nextStates = unvisited.flatMap { s =>
          transitions.filter(t => t.from == s && t.symbol == "epsilon").flatMap(_.to)
        }
        loop(nextStates, visited ++ unvisited)
    
    loop(Set(state), Set.empty)

  def epsilonClosure(states: Set[State], transitions: List[Transition]): Set[State] =
    states.flatMap(s => epsilonClosure(s, transitions))

  def nfaeToNfa(nfae: NFAe): NFA =
    val eClosures: Map[State, Set[State]] = nfae.states.map { s => 
      s -> epsilonClosure(s, nfae.transitions)
    }.toMap

    val newTransitions = nfae.states.flatMap { s =>
      val eClosure = eClosures(s)
      nfae.alphabet.flatMap { a =>
        val reachAfterA = eClosure.flatMap { sEps =>
          nfae.transitions.filter(t => t.from == sEps && t.symbol == a).flatMap(_.to)
        }
        val reachAfterAEps = epsilonClosure(reachAfterA, nfae.transitions)
        if reachAfterAEps.nonEmpty then
          Some(Transition(s, a, reachAfterAEps.toList.distinct.sorted))
        else
          None
      }
    }

    val newFinals = nfae.states.filter { s =>
      eClosures(s).intersect(nfae.final_states.toSet).nonEmpty
    }

    NFA(
      alphabet = nfae.alphabet,
      states = nfae.states,
      initial_state = nfae.initial_state,
      final_states = newFinals.sorted,
      transitions = newTransitions
    )

  def nfaToDfa(nfa: NFA): DFA =
    val initialSet = Set(nfa.initial_state)

    def getTargetSet(currentSet: Set[State], symbol: String): Set[State] =
      currentSet.flatMap { s =>
        nfa.transitions.filter(t => t.from == s && t.symbol == symbol).flatMap(_.to)
      }

    def explore(queue: List[Set[State]], visited: Set[Set[State]], trans: List[(Set[State], String, Set[State])]): (Set[Set[State]], List[(Set[State], String, Set[State])]) =
      queue match
        case Nil => (visited, trans)
        case currentSet :: rest =>
          if visited.contains(currentSet) then
            explore(rest, visited, trans)
          else
            val newVisited = visited + currentSet
            val nextSteps = nfa.alphabet.map { a =>
              (a, getTargetSet(currentSet, a))
            }.filter(_._2.nonEmpty)
            
            val newTrans = trans ++ nextSteps.map { case (a, targetSet) => (currentSet, a, targetSet) }
            val newQueue = rest ++ nextSteps.map(_._2).filterNot(newVisited.contains)
            
            explore(newQueue, newVisited, newTrans)

    val (allSets, setTransitions) = explore(List(initialSet), Set.empty, List.empty)
    
    val sortedSets = allSets.toList.sortBy(s => (s.size, s.toList.sorted.mkString(",")))
    
    val stateNameMap = sortedSets.zipWithIndex.map { case (s, idx) =>
      if s == initialSet then s -> nfa.initial_state
      else s -> s"qd$idx"
    }.toMap

    val dfaStates = stateNameMap.values.toList.sorted
    val dfaInitialState = stateNameMap(initialSet)
    val dfaFinalStates = allSets.filter(s => s.intersect(nfa.final_states.toSet).nonEmpty).map(stateNameMap).toList.sorted
    val dfaTransitions = setTransitions.map { case (from, sym, to) =>
      Transition(stateNameMap(from), sym, List(stateNameMap(to)))
    }.sortBy(t => (t.from, t.symbol))

    DFA(
      alphabet = nfa.alphabet,
      states = dfaStates,
      initial_state = dfaInitialState,
      final_states = dfaFinalStates,
      transitions = dfaTransitions
    )
