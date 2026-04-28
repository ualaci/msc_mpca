package lab1

import io.circe._
import io.circe.generic.semiauto._
import io.circe.yaml.parser
import io.circe.yaml.syntax._
import io.circe.syntax._
import java.io.File
import scala.io.Source

object YamlIO:

  case class TransitionDto(from: String, symbol: String, to: List[String])
  case class AutomatonDto(`type`: String, alphabet: List[String], states: List[String], initial_state: String, final_states: List[String], transitions: List[TransitionDto])

  implicit val transitionDecoder: Decoder[TransitionDto] = deriveDecoder
  implicit val transitionEncoder: Encoder[TransitionDto] = deriveEncoder

  implicit val automatonDecoder: Decoder[AutomatonDto] = deriveDecoder
  implicit val automatonEncoder: Encoder[AutomatonDto] = deriveEncoder

  def readYaml(filePath: String): Either[Error, Automaton] =
    val source = Source.fromFile(filePath)
    val yamlStr = try source.mkString finally source.close()
    
    parser.parse(yamlStr).flatMap(_.as[AutomatonDto]).map { dto =>
      val trans = dto.transitions.map(t => Transition(t.from, t.symbol, t.to))
      dto.`type` match
        case "nfae" => NFAe(dto.alphabet, dto.states, dto.initial_state, dto.final_states, trans)
        case "nfa"  => NFA(dto.alphabet, dto.states, dto.initial_state, dto.final_states, trans)
        case "dfa"  => DFA(dto.alphabet, dto.states, dto.initial_state, dto.final_states, trans)
        case other  => throw new IllegalArgumentException(s"Unknown automaton type: $other")
    }

  def writeYaml(automaton: Automaton, filePath: String): Unit =
    val tpe = automaton match
      case _: NFAe => "nfae"
      case _: NFA  => "nfa"
      case _: DFA  => "dfa"

    val dto = AutomatonDto(
      `type` = tpe,
      alphabet = automaton.alphabet,
      states = automaton.states,
      initial_state = automaton.initial_state,
      final_states = automaton.final_states,
      transitions = automaton.transitions.map(t => TransitionDto(t.from, t.symbol, t.to))
    )

    val yamlStr = dto.asJson.asYaml.spaces2
    val pw = new java.io.PrintWriter(new File(filePath))
    try pw.write(yamlStr) finally pw.close()
