# Venda DL - API de E-commerce

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Security](https://img.shields.io/badge/Security-JWT-blue)](https://jwt.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Mockito](https://img.shields.io/badge/Tests-Mockito%20%26%20JUnit5-green?logo=junit5)](https://site.mockito.org/)

Uma API RESTful completa e robusta desenvolvida para gerenciar os ecossistemas de e-commerce. O projeto conta com autenticação segura, rotinas automatizadas em segundo plano, integrações com gateways de pagamento via Webhooks e uma cobertura rigorosa de testes unitários.

---

## Principais Funcionalidades & Regras de Negócio

A API foi desenhada seguindo as melhores práticas de desenvolvimento de software e os princípios **RESTful**, entregando as seguintes regras:

* **Segurança & Autenticação (JWT):** Gerenciamento completo de usuários com cadastro, login e obtenção de dados do perfil autenticado (`/me`), protegendo rotas críticas com tokens JWT dinâmicos.
* **Carrinho de Compras:** Adição, visualização e remoção de itens vinculados ao contexto do usuário logado antes do fechamento do pedido.
* **Gestão Avançada de Produtos & Estoque:** Operações de busca textual customizada, filtros refinados por categoria com suporte a paginação de alto desempenho (`Pageable`). Possui controle ativo de reabastecimento.
* **Ciclo de Vida do Pedido (Checkout & Webhooks):** Processo completo de checkout, pagamento simulado, cancelamento de pedidos e atualização de status automática via Webhook integrado ao **Abacate Pay**.
* **Cupons & Favoritos:** Sistema integrado para criação e listagem de cupons de desconto, além de um módulo completo de gerenciamento de produtos favoritos por usuário.
* **Dashboard Administrativo:** Central de inteligência do e-commerce com endpoints dedicados para emitir resumos gerais de vendas financeiras e alertas inteligentes de produtos com estoque baixo.
* **Agendamento Automatizado (`@Scheduled`):** Rotina assíncrona rodando em segundo plano que identifica pedidos não pagos dentro do prazo de expiração, executa o cancelamento automático e faz a recomposição imediata do estoque dos produtos.
* **Testes Unitários Cobertos (Mockito & JUnit 5):** Bateria rigorosa de testes cobrindo fluxos de sucesso, tratamento de exceções customizadas (`BusinessException`) e paginação nas regras de negócio do `ProdutoService` e `PedidoService`.

---

## Estrutura de Endpoints (Mapeamento Completo API RESTful)

Abaixo estão listados todos os recursos mapeados automaticamente pela interface do Swagger:

### Autenticação & Usuários (`autenticacao-controller` & `usuario-controller`)
*   `POST /api/auth/cadastro` — Cadastro de novos usuários no ecossistema.
*   `POST /api/auth/login` — Autenticação de credenciais com geração de token JWT.
*   `GET /api/auth/me` — Recupera os detalhes básicos de autenticação do usuário logado.
*   `GET /api/usuarios/me` — Retorna os dados completos do perfil do usuário logado.
*   `PUT /api/usuarios/me` — Atualiza as informações cadastrais do perfil do usuário.

### Catálogo (`produto-controller` & `categoria-controller`)
*   `GET /api/produtos` — Listagem paginada de todos os produtos cadastrados.
*   `POST /api/produtos` — Criação de um novo produto (Privado/Admin).
*   `GET /api/produtos/buscar` — Mecanismo de busca textual e filtros de produtos.
*   `GET /api/produtos/filtrar-categoria` — Filtra produtos com base em uma categoria específica.
*   `PUT /api/produtos/{id}/reabastecer` — Atualiza e adiciona unidades ao estoque do produto.
*   `GET /api/categorias` — Listagem de todas as categorias de produtos disponíveis.
*   `POST /api/categorias` — Criação de novas categorias no catálogo.
*   `PUT /api/categorias/{id}` — Edição e atualização de dados de uma categoria existente.
*   `DELETE /api/categorias/{id}` — Exclusão permanente de uma categoria.