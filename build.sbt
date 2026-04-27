scalaVersion := "3.3.3"
name := "teoria-da-computacao"
version := "0.1.0"

// Flags do compilador para forçar boas práticas e avisar sobre comportamentos inseguros
scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Wunused:all",
  "-Ysafe-init" // Protege contra inicialização insegura de objetos
)

val circeVersion = "0.14.7"

libraryDependencies ++= Seq(
  // Biblioteca essencial para Programação Funcional (Teoria das Categorias)
  "org.typelevel" %% "cats-core" % "2.10.0",
  // Parser Combinators para o Parser de Regex
  "org.scala-lang.modules" %% "scala-parser-combinators" % "2.3.0",
  // Circe para manipulação de YAML e JSON
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-yaml" % "0.15.1"
)
