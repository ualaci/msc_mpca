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

// Biblioteca essencial para Programação Funcional (Teoria das Categorias)
libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0"