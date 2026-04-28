{
  description = "Scala Functional Programming environment for Teoria da Computação Lab 1";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-23.11";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux"; # Adjust if you are on macOS (e.g. x86_64-darwin, aarch64-darwin)
      pkgs = import nixpkgs { inherit system; };
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = with pkgs; [
          jdk21
          sbt
          scala_3
        ];
      };
    };
}
