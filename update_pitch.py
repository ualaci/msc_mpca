import re

with open("pitch_nox.tex", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Insert NOX Overview
nox_overview = r"""
\begin{frame}{NOX Overview}
    O \textit{Network Operating System} em detalhes.
    
    \vspace{0.5cm}
    
    \begin{itemize}            
        \item \textbf{Componentes Principais:} Base framework (núcleo C++), \textit{Network View} (estado global da rede) e Aplicações de Gerenciamento.
        \item \textbf{Granularidade de Controle:} Baseado em fluxo (\textit{flow-level control}). O controle se aplica ao primeiro pacote do fluxo, delegando o resto ao hardware.
        \item \textbf{Abstração do Switch (OpenFlow):} Switches deixam de ter inteligência e passam a ter tabelas de fluxo simples com: \textit{Header} (Match), \textit{Counters} e \textit{Actions}.
    \end{itemize}
\end{frame}

"""

content = content.replace(r"\begin{frame}{Arquitetura e Comportamento}", nox_overview + r"\begin{frame}{Arquitetura e Comportamento}")

# 2. Insert Programmatic Interface
programmatic_interface = r"""
\begin{frame}{Programmatic Interface}
    Como as aplicações interagem com o sistema operacional da rede.
    
    \vspace{0.5cm}
    
    \begin{itemize}            
        \item \textbf{High-Level Services:} O NOX fornece funções essenciais prontas, como construção da \textit{Network View}, serviços de roteamento e resolução de nomes (identidade para endereço).
        \item \textbf{Modelo Baseado em Eventos:} Aplicações reagem a eventos da rede gerados pelos switches (ex: \textit{PacketIn}, alteração de topologia, queda de link).
        \item \textbf{Limitações de Runtime:}
        \begin{itemize}
            \item Os \textit{handlers} de eventos não podem bloquear o processamento (\textit{non-blocking}).
            \item Uma aplicação mal estruturada pode paralisar o controlador central, sendo um desafio de interface e arquitetura.
        \end{itemize}
    \end{itemize}
\end{frame}

"""

content = content.replace(r"\begin{frame}{Exemplo Prático (POX/Mininet)}", programmatic_interface + r"\begin{frame}{Exemplo Prático (POX/Mininet)}")

# 3. Insert Example Applications
example_apps = r"""
\begin{frame}{Example Applications (Seção 4)}
    Aplicações de controle e segurança rodando sobre o NOX.
    
    \vspace{0.5cm}
    
    \begin{itemize}            
        \item \textbf{Controle de Acesso Baseado no Usuário:} Diferente de roteadores que usam regras rígidas de IP/MAC, o NOX pode gerenciar tráfego baseado na identidade (autenticação).
        \item \textbf{Mobilidade da Política:} Se o usuário muda de switch físico na empresa, a rede inteira se reconfigura para segui-lo.
        \item \textbf{Implementação Prática (Ethane):} A reconstrução da arquitetura Ethane como uma aplicação NOX comprovou que é possível abstrair políticas complexas com muito menos código.
    \end{itemize}
\end{frame}

"""

content = content.replace(r"%------------------------------------------------\n\section{Resultados Obtidos}", example_apps + r"%------------------------------------------------" + "\n" + r"\section{Resultados Obtidos}")

# 4. Insert Related Work and Open Issues
open_issues = r"""
\begin{frame}{Related Work and Open Issues}
    Desafios e mitigações levantados pelos autores.
    
    \vspace{0.5cm}
    
    \begin{itemize}            
        \item \textbf{Ponto Único de Falha e Gargalo:} Centralizar o controle cria um potencial gargalo de performance e risco de indisponibilidade total.
        \item \textbf{A Solução/Contorno:} Uso do \textit{hardware} do switch para cache dos fluxos aprovados (evitando chamar o controlador toda hora), processamento paralelo e a possibilidade de clusters de controladores distribuídos (garantindo consistência).
        \item \textbf{Segurança e Confiança:} Switches confiam cegamente no controlador. É imperativo adotar canais autenticados (TLS) para evitar o sequestro do plano de controle ou falsificação da topologia.
    \end{itemize}
\end{frame}

"""

content = content.replace(r"%------------------------------------------------\n\section{Conclusões}", open_issues + r"%------------------------------------------------" + "\n" + r"\section{Conclusões}")


with open("pitch_nox.tex", "w", encoding="utf-8") as f:
    f.write(content)
