import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class AppTextStyles {
  AppTextStyles._();

  static final heading1 = GoogleFonts.inter(
    fontSize: 32.0,
    fontWeight: FontWeight.w700,
  );

  static final heading2 = GoogleFonts.inter(
    fontSize: 24.0,
    fontWeight: FontWeight.w700,
  );

  static final subtitle = GoogleFonts.inter(
    fontSize: 18.0,
    fontWeight: FontWeight.w600,
  );

  static final body = GoogleFonts.inter(
    fontSize: 16.0,
    fontWeight: FontWeight.w400,
  );

  static final bodyBold = GoogleFonts.inter(
    fontSize: 16.0,
    fontWeight: FontWeight.w600,
  );

  static final caption = GoogleFonts.inter(
    fontSize: 14.0,
    fontWeight: FontWeight.w400,
  );

  static final small = GoogleFonts.inter(
    fontSize: 12.0,
    fontWeight: FontWeight.w400,
  );
}
