import 'package:fetin/common/constants/colors.dart';
import 'package:fetin/common/constants/routes.dart';
import 'package:fetin/common/constants/text_style.dart';
import 'package:flutter/material.dart';

class SplashPage extends StatefulWidget {
  const SplashPage({super.key});

  @override
  State<SplashPage> createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage> {
  @override
  void initState() {
    super.initState();
    _initializeSplash();
  }

  Future<void> _initializeSplash() async {
    await Future.delayed(const Duration(seconds: 2));
    Navigator.pushReplacementNamed(context, NamedRoute.home);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        alignment: Alignment.center,
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [
              AppColors.grey900,
              AppColors.grey800,
              AppColors.grey700,
            ],
          ),
        ),
        child: Text(
          'mili',
          style: AppTextStyles.heading1.copyWith(color: AppColors.white),
        ),
      ),
    );
  }
}
