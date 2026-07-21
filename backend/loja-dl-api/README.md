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