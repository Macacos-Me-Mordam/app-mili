import 'dart:io';

import 'package:fetin/common/constants/text_style.dart';
import 'package:flutter/material.dart';

class ImageSubmissionCard extends StatelessWidget {
  final File? selectedImage;
  final VoidCallback onPickImage;
  final VoidCallback onSubmitImage;

  const ImageSubmissionCard({
    super.key,
    required this.selectedImage,
    required this.onPickImage,
    required this.onSubmitImage,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      elevation: 2,
      color: theme.cardColor,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              "Envie uma foto de descarte irregular",
              style: AppTextStyles.subtitle.copyWith(
                color: theme.textTheme.titleMedium?.color,
              ),
            ),
            const SizedBox(height: 12),
            Text(
              "Encontrou resíduos descartados de forma irregular? Tire uma foto e contribua para uma cidade mais limpa.",
              style: AppTextStyles.body.copyWith(
                color: theme.textTheme.bodyMedium?.color,
              ),
            ),
            const SizedBox(height: 12),
            if (selectedImage != null)
              ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: Image.file(selectedImage!, height: 180),
              )
            else
              Text(
                "Nenhuma imagem selecionada.",
                style: AppTextStyles.caption.copyWith(
                  color: theme.textTheme.bodySmall?.color,
                ),
              ),
            const SizedBox(height: 12),
            Row(
              children: [
                ElevatedButton.icon(
                  onPressed: onPickImage,
                  icon: const Icon(Icons.photo),
                  label: Text("Selecionar Foto", style: AppTextStyles.bodyBold),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: theme.colorScheme.tertiary,
                    foregroundColor: theme.colorScheme.onSecondary,
                  ),
                ),
                const SizedBox(width: 10),
                ElevatedButton(
                  onPressed: onSubmitImage,
                  child: Text("Enviar", style: AppTextStyles.bodyBold),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.green.shade600,
                    foregroundColor: Colors.white,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
