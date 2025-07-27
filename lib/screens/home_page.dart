import 'dart:io';
import 'package:fetin/common/widgets/chat_bot_button.dart';
import 'package:flutter/material.dart';
import 'package:geocoding/geocoding.dart';
import 'package:geolocator/geolocator.dart';
import 'package:image_picker/image_picker.dart';
import 'package:fetin/common/constants/text_style.dart';
import 'package:fetin/common/widgets/info_card.dart';
import 'package:fetin/common/widgets/image_submission_card.dart';
import 'package:fetin/common/widgets/theme_toggle_button.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  File? _selectedImage;
  String? _cityAndState;

  @override
  void initState() {
    super.initState();
    _getLocation();
  }

  Future<void> _getLocation() async {
    try {
      bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) return;

      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.deniedForever ||
            permission == LocationPermission.denied)
          return;
      }

      final position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.low,
      );
      final placemarks = await placemarkFromCoordinates(
        position.latitude,
        position.longitude,
      );

      if (placemarks.isNotEmpty) {
        final place = placemarks.first;
        setState(() {
          _cityAndState = "${place.locality}, ${place.administrativeArea}";
        });
      }
    } catch (_) {
      // Ignorar falhas
    }
  }

  Future<void> _pickImage() async {
    final picker = ImagePicker();
    final picked = await picker.pickImage(source: ImageSource.gallery);
    if (picked != null) {
      setState(() => _selectedImage = File(picked.path));
    }
  }

  void _submitImage() {
    if (_selectedImage == null) return;

    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text("Imagem enviada com sucesso!")),
    );

    setState(() => _selectedImage = null);
  }

  void _goToChatbot() {
    Navigator.pushNamed(context, '/chatbot');
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return Scaffold(
      backgroundColor: theme.scaffoldBackgroundColor,
      appBar: AppBar(
        elevation: 0,
        centerTitle: false,
        title: Text(
          "mili",
          style: AppTextStyles.heading2.copyWith(
            color:
                theme.appBarTheme.foregroundColor ??
                theme.colorScheme.onPrimary,
          ),
        ),
        actions: const [ThemeToggleButton()],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (_cityAndState != null)
              Row(
                children: [
                  Icon(
                    Icons.location_on,
                    size: 16,
                    color: theme.iconTheme.color,
                  ),
                  const SizedBox(width: 4),
                  Text(
                    _cityAndState!,
                    style: AppTextStyles.caption.copyWith(
                      color: theme.textTheme.bodySmall?.color,
                    ),
                  ),
                ],
              ),
            const SizedBox(height: 8),
            Text(
              "Sua participação é fundamental no combate ao descarte inadequado de resíduos. Juntos, podemos promover uma cidade mais limpa e consciente.",
              style: AppTextStyles.body.copyWith(
                color: theme.textTheme.bodyMedium?.color,
              ),
              textAlign: TextAlign.justify,
            ),
            const SizedBox(height: 24),
            InfoCard(
              icon: Icons.warning_amber_rounded,
              iconColor: Colors.red.shade400,
              title: "Por que denunciar?",
              subtitle:
                  "O descarte inadequado de resíduos pode gerar impactos à saúde, atrair vetores de doenças e causar danos ao meio ambiente.",
            ),
            const SizedBox(height: 12),
            InfoCard(
              icon: Icons.recycling,
              iconColor: Colors.green.shade400,
              title: "Como descartar corretamente?",
              subtitle:
                  "Descarte seus resíduos de forma responsável, utilizando os pontos de coleta apropriados ou serviços públicos de recolhimento.",
            ),
            const SizedBox(height: 24),
            ImageSubmissionCard(
              selectedImage: _selectedImage,
              onPickImage: _pickImage,
              onSubmitImage: _submitImage,
            ),
            const SizedBox(height: 80),
          ],
        ),
      ),
      bottomNavigationBar: Padding(
        padding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
        child: ChatbotButton(onPressed: _goToChatbot),
      ),
    );
  }
}
