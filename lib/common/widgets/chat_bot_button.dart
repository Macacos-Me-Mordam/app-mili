import 'package:fetin/common/constants/text_style.dart';
import 'package:flutter/material.dart';

class ChatbotButton extends StatelessWidget {
  final VoidCallback onPressed;

  const ChatbotButton({super.key, required this.onPressed});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton.icon(
        onPressed: onPressed,
        icon: const Icon(Icons.chat),
        label: Text("Conversar com o assistente", style: AppTextStyles.bodyBold),
        style: ElevatedButton.styleFrom(
          backgroundColor: theme.colorScheme.primary,
          foregroundColor: theme.colorScheme.onPrimary,
          padding: const EdgeInsets.symmetric(vertical: 14),
        ),
      ),
    );
  }
}
