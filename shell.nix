{ pkgs ? import <nixpkgs> {} }:
pkgs.mkShell {
  buildInputs = with pkgs; [
    clojure
    nodejs
    python311
    python311Packages.python-lsp-server
    ocaml
    ocamlPackages.utop
  ];
}
