import 'package:flutter/material.dart';

class ChatbotPage extends StatelessWidget {
  const ChatbotPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Assistente Educativo")),
      body: const Center(
        child: Text(
          "Aqui ficará o chatbot educativo 👨‍🏫\n\nEle vai te ensinar a descartar corretamente cada tipo de lixo.",
          textAlign: TextAlign.center,
        ),
      ),
    );
  }
}
