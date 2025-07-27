import 'package:fetin/common/constants/theme_controller.dart';
import 'package:flutter/material.dart';

class ThemeToggleButton extends StatelessWidget {
  const ThemeToggleButton({super.key});

  @override
  Widget build(BuildContext context) {
    return IconButton(
      icon: Icon(
        ThemeController.themeMode.value == ThemeMode.light
            ? Icons.dark_mode
            : Icons.light_mode,
      ),
      onPressed: ThemeController.toggleTheme,
      tooltip: 'Alternar tema',
    );
  }
}
