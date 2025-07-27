import 'package:flutter/material.dart';

class ThemeController {
  static final themeMode = ValueNotifier<ThemeMode>(ThemeMode.light);

  static void toggleTheme() {
    themeMode.value = themeMode.value == ThemeMode.light
        ? ThemeMode.dark
        : ThemeMode.light;
  }
}