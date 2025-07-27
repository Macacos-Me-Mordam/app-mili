import 'package:fetin/common/constants/text_style.dart';
import 'package:flutter/material.dart';

class InfoCard extends StatelessWidget {
  final IconData icon;
  final Color iconColor;
  final String title;
  final String subtitle;

  const InfoCard({
    super.key,
    required this.icon,
    required this.iconColor,
    required this.title,
    required this.subtitle,
  });

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Card(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      color: theme.cardColor,
      elevation: 2,
      child: ListTile(
        leading: Icon(icon, color: iconColor, size: 32),
        title: Text(
          title,
          style: AppTextStyles.subtitle.copyWith(
            color: theme.textTheme.titleMedium?.color,
          ),
        ),
        subtitle: Text(
          subtitle,
          style: AppTextStyles.body.copyWith(
            color: theme.textTheme.bodyMedium?.color,
          ),
        ),
      ),
    );
  }
}
