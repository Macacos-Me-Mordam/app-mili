# AppFetin - Chat com EcoBot

## Descrição

O AppFetin agora inclui um sistema de chat minimalista com o EcoBot, um assistente virtual especializado em questões ambientais e sustentabilidade.

## Funcionalidades do Chat

### 🎯 Características Principais

- **Interface Minimalista**: Design limpo e moderno usando Material Design 3
- **Navegação Intuitiva**: Botão "Conversar com bot" na tela principal
- **Avatar e Nome**: Cada mensagem exibe o nome e avatar do remetente
- **Respostas Inteligentes**: O EcoBot responde com base no conteúdo da mensagem

### 🤖 EcoBot - Assistente Ambiental

O EcoBot é especializado em:
- **Reciclagem**: Dicas sobre separação de resíduos
- **Redução de Plástico**: Informações sobre impacto ambiental
- **Economia de Energia**: Conselhos para uso eficiente
- **Conservação de Água**: Dicas para redução do consumo
- **Sustentabilidade**: Orientações gerais sobre meio ambiente

### 💬 Como Usar o Chat

1. **Acessar**: Clique no botão "Conversar com bot" na tela principal
2. **Conversar**: Digite sua mensagem no campo de texto
3. **Enviar**: Clique no botão de enviar (ícone de avião de papel)
4. **Receber Resposta**: O EcoBot responderá automaticamente

### 🎨 Interface

- **Mensagens do Usuário**: Aparecem à direita com fundo azul
- **Mensagens do Bot**: Aparecem à esquerda com fundo cinza
- **Avatars**: 
  - 🤖 Para o EcoBot
  - 👤 Para o usuário
- **Nomes**: Exibidos acima de cada mensagem
- **Auto-scroll**: A conversa rola automaticamente para a última mensagem

## Estrutura do Projeto

```
app/src/main/java/com/example/appfetin/
├── MainActivity.kt              # Activity principal (apenas navegação)
├── model/
│   └── ChatMessage.kt          # Modelos de dados do chat
├── navigation/
│   └── AppNavigation.kt        # Sistema de navegação
└── ui/
    ├── HomeScreen.kt           # Tela principal (Home)
    └── ChatScreen.kt           # Tela do chat
```

## Tecnologias Utilizadas

- **Jetpack Compose**: Interface moderna do Android
- **Navigation Compose**: Navegação entre telas
- **Material Design 3**: Design system atualizado
- **Coroutines**: Operações assíncronas para respostas do bot

## Palavras-chave para Respostas

O EcoBot reconhece e responde a:
- "reciclagem", "reciclar"
- "plástico"
- "energia", "economizar"
- "água"
- "ajuda", "como"

## Próximos Passos

- [ ] Integração com IA real (OpenAI, Google AI, etc.)
- [ ] Histórico de conversas
- [ ] Notificações push
- [ ] Compartilhamento de dicas
- [ ] Personalização do avatar do usuário

## Como Contribuir

1. Clone o repositório
2. Abra no Android Studio
3. Execute o projeto
4. Teste o chat clicando no botão "Conversar com bot"
5. Faça suas contribuições

---

**Desenvolvido com ❤️ para promover a sustentabilidade e conscientização ambiental**
