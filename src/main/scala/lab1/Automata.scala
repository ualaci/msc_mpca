package lab1

// State representation
type State = String

// Core case classes for Automata
case class Transition(from: State, symbol: String, to: List[State])

trait Automaton {
  def alphabet: List[String]
  def states: List[State]
  def initial_state: State
  def final_states: List[State]
  def transitions: List[Transition]
}

case class NFAe(
  alphabet: List[String],
  states: List[State],
  initial_state: State,
  final_states: List[State],
  transitions: List[Transition]
) extends Automaton

case class NFA(
  alphabet: List[String],
  states: List[State],
  initial_state: State,
  final_states: List[State],
  transitions: List[Transition]
) extends Automaton

case class DFA(
  alphabet: List[String],
  states: List[State],
  initial_state: State,
  final_states: List[State],
  transitions: List[Transition]
) extends Automaton
