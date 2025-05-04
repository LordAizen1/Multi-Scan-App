# MultiScan App

MultiScan is an Android application that combines document scanning and landmark recognition capabilities. Built with Jetpack Compose for a modern UI and TensorFlow Lite for landmark recognition, this project showcases advanced Android development skills.

## Features

- **Document Scanner**: Scan documents and save them as PDF files
- **Landmark Recognition**: Recognize famous landmarks using TensorFlow Lite models

## Integration Instructions

This project has been set up with the basic integration structure, but you need to complete the following steps to fully enable the landmark recognition functionality:

1. **Copy TensorFlow Lite models from Landmark-Recognition2 project**:
   
   Copy the following .tflite model files from Landmark-Recognition2/app/src/main/assets to MultiScan/app/src/main/assets:
   
   - africa.tflite
   - asia.tflite
   - europe.tflite
   - northamerica.tflite
   - oceania_antarctica.tflite
   - southamerica.tflite

2. **Copy needed classifier implementation classes**:

   Copy the following files from Landmark-Recognition2/app/src/main/java/com/elsharif/landmarkrecognition/data to similar package structure in MultiScan:
   
   - TFLiteLandmarkClassifierOfAfrica.kt
   - TFLiteLandmarkClassifierOfAsia.kt
   - TFLiteLandmarkClassifierOfEurope.kt
   - TFLiteLandmarkClassifierOfNorthAmerica.kt
   - TFLiteLandmarkClassifierOfOceaniaAntarctica.kt
   - TFLiteLandmarkClassifierOfSouthAmerica.kt

3. **Copy domain classes**:
   
   Copy the domain classes from Landmark-Recognition2/app/src/main/java/com/elsharif/landmarkrecognition/domain to your project.

4. **Enhance the LandmarkScreen implementation**:

   Update the basic LandmarkScreen.kt to use the classifiers and implement camera functionality similar to the Landmark-Recognition2 project.

## Project Structure

- **app/src/main/java/com/example/multi_scan/navigation**: Contains navigation-related code
- **app/src/main/java/com/example/multi_scan/ui/screens/home**: Contains the home screen and document scanner screen
- **app/src/main/java/com/example/multi_scan/ui/screens/landmark**: Contains the landmark recognition screen
- **app/src/main/assets**: Should contain the TensorFlow Lite models

## Building and Running

This project uses Gradle to build. Open the project in Android Studio and run as a regular Android application.

## Some Images to Show the App Working/UI

### Home Screen
![MultiScan Home Screen](https://raw.githubusercontent.com/LordAizen1/Multi-Scan-App/main/home-screen.jpg)
*The welcome screen with options for Document Scanner and Landmark Recognition.*

### Landmark Recognition Options
![Landmark Recognition Options](https://raw.githubusercontent.com/LordAizen1/Multi-Scan-App/main/landmark-options.jpg)
*Selecting a region (e.g., Asian Landmarks) for recognition.*

### Landmark Recognition Result (India Gate)
![India Gate Recognition](https://raw.githubusercontent.com/LordAizen1/Multi-Scan-App/main/landmark-result.jpg)
*Recognizing India Gate with 89% confidence using TensorFlow Lite.*

### Document Scanner
![Document Scanner](https://raw.githubusercontent.com/LordAizen1/Multi-Scan-App/main/document-scanner.jpg)
*Document scanner showing saved PDFs (e.g., baka.pdf, sussy.pdf).*

## Portfolio
Explore more of my projects, including MultiScan, on my portfolio: [Nivedita Tailors Portfolio](https://LordAizen1.github.io/nivedita-tailors)
