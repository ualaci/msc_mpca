package lab1

object Main:
  def main(args: Array[String]): Unit =
    if args.isEmpty then
      println("Usage:")
      println("  sbt \"run convert <input.yaml> <output.yaml>\"")
      println("  sbt \"run regex <regex_string> <output.yaml>\"")
      sys.exit(1)
      
    args(0) match
      case "convert" =>
        if args.length < 3 then
          println("Missing arguments for convert.")
          sys.exit(1)
        val input = args(1)
        val output = args(2)
        
        YamlIO.readYaml(input) match
          case Left(err) => 
            println(s"Error reading YAML: $err")
          case Right(nfae: NFAe) =>
            println(s"Read NFAe from $input. Converting to NFA...")
            val nfa = Converter.nfaeToNfa(nfae)
            println("Converting NFA to DFA...")
            val dfa = Converter.nfaToDfa(nfa)
            YamlIO.writeYaml(dfa, output)
            println(s"Saved resulting DFA to $output.")
          case Right(nfa: NFA) =>
            println(s"Read NFA from $input. Converting to DFA...")
            val dfa = Converter.nfaToDfa(nfa)
            YamlIO.writeYaml(dfa, output)
            println(s"Saved resulting DFA to $output.")
          case Right(dfa: DFA) =>
            println("Input is already a DFA. Writing it directly.")
            YamlIO.writeYaml(dfa, output)
          case Right(_) =>
            println("Unknown Automaton type.")
            
      case "regex" =>
        if args.length < 3 then
          println("Missing arguments for regex.")
          sys.exit(1)
        val regexStr = args(1)
        val output = args(2)
        
        RegexParser.parseRegex(regexStr) match
          case Left(err) =>
            println(s"Error parsing regex: $err")
          case Right(ast) =>
            println(s"Parsed Regex successfully into AST:\n$ast")
            val nfae = Thompson.regexToNfae(ast)
            YamlIO.writeYaml(nfae, output)
            println(s"Saved Thompson NFAe to $output.")
            
      case other =>
        println(s"Unknown command: $other")
