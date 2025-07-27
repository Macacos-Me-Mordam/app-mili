import 'package:fetin/common/constants/colors.dart';
import 'package:fetin/common/constants/theme_controller.dart';
import 'package:flutter/material.dart';
import 'package:fetin/common/constants/routes.dart';
import 'package:fetin/screens/home_page.dart';
import 'package:fetin/screens/splash_page.dart';

final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    return ValueListenableBuilder<ThemeMode>(
      valueListenable: ThemeController.themeMode,
      builder: (context, themeMode, _) {
        return MaterialApp(
          navigatorKey: navigatorKey,
          debugShowCheckedModeBanner: false,
          theme: _lightTheme(),
          darkTheme: _darkTheme(),
          themeMode: themeMode,
          initialRoute: NamedRoute.initial,
          routes: {
            NamedRoute.initial: (context) => const SplashPage(),
            NamedRoute.home: (context) => const HomePage(),
          },
        );
      },
    );
  }

  ThemeData _lightTheme() {
    return ThemeData(
      brightness: Brightness.light,
      scaffoldBackgroundColor: AppColors.grey100,
      appBarTheme: const AppBarTheme(
        backgroundColor: AppColors.grey900,
        foregroundColor: AppColors.white,
      ),
      cardColor: AppColors.white,
      iconTheme: const IconThemeData(color: AppColors.grey700),
      colorScheme: const ColorScheme.light(
        primary: AppColors.grey900,
        secondary: AppColors.standartGreen,
        tertiary: AppColors.grey600,
        onPrimary: AppColors.white,
        onSecondary: AppColors.white,
      ),
      textTheme: const TextTheme(
        bodyMedium: TextStyle(color: AppColors.grey700),
        bodySmall: TextStyle(color: AppColors.grey500),
        titleMedium: TextStyle(color: AppColors.grey900),
      ),
    );
  }

  ThemeData _darkTheme() {
    return ThemeData(
      brightness: Brightness.dark,
      scaffoldBackgroundColor: AppColors.grey900,
      appBarTheme: const AppBarTheme(
        backgroundColor: AppColors.grey800,
        foregroundColor: AppColors.white,
      ),
      cardColor: AppColors.grey800,
      iconTheme: const IconThemeData(color: AppColors.grey300),
      colorScheme: const ColorScheme.dark(
        primary: AppColors.grey100,
        secondary: AppColors.standartRed,
        tertiary: AppColors.grey600,
        onPrimary: AppColors.black,
        onSecondary: AppColors.white,
      ),
      textTheme: const TextTheme(
        bodyMedium: TextStyle(color: AppColors.grey300),
        bodySmall: TextStyle(color: AppColors.grey500),
        titleMedium: TextStyle(color: AppColors.grey100),
      ),
    );
  }
}
