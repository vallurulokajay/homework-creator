# Homework Table Creator

A simple Android app to create homework tables for a specific date and export them as images.

## How to Open in Android Studio

1. Unzip the `HomeworkTableCreator.zip` file.
2. Open Android Studio.
3. Select **File > Open** and choose the `HomeworkTableCreator` folder.
4. Android Studio will sync the project and download necessary Gradle dependencies.

## Build Instructions

### Debug APK
To build the debug APK:
1. Open the **Gradle** tool window in Android Studio (usually on the right).
2. Navigate to **HomeworkTableCreator > app > Tasks > build > assembleDebug**.
3. Double-click `assembleDebug`.
4. The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`.

Alternatively, via terminal (if Gradle wrapper is generated):
```bash
./gradlew assembleDebug
```

### Installation
To install the APK on your device:
```bash
adb install -r app-debug.apk
```

## Usage Guide

1. **Select Date**: Tap the "Select Date" (or Edit icon) at the top to pick the date for the homework table.
2. **Add Entries**: Enter the Subject and Homework content, then tap "Add Entry". Repeat for all subjects.
3. **Edit/Delete**: Tap the Delete icon on a row to remove it.
4. **Create Image**: Tap "Create Table Image". This will generate the JPEG.
5. **Preview & Share**: A preview dialog will appear. Tap "Share" to send the image to WhatsApp, Gmail, or save it to Drive.

## Building without Android Studio (Cloud Build)

If you cannot install Android Studio, you can use **GitHub Actions** to build the APK for free in the cloud.

1. **Create a GitHub Account**: If you don't have one, sign up at [github.com](https://github.com).
2. **Create a New Repository**: Create a new empty repository.
3. **Upload Files**: Upload the contents of the unzipped `HomeworkTableCreator` folder to your repository.
   - You can use "Upload files" on the GitHub website or use Git command line.
4. **Wait for Build**: Once the files are pushed, click on the **Actions** tab in your repository.
   - You should see a workflow named "Android Build" running.
5. **Download APK**: 
   - Click on the completed build run.
   - Scroll down to the **Artifacts** section.
   - Click on `app-debug` to download the zip file containing your APK.
   - Unzip it to find `app-debug.apk` and install it on your phone.

## File Location
Generated images are saved in the app's external files directory (Android 10+ scoped storage) and also inserted into the System Gallery (Pictures/HomeworkTables) if possible.

## Testing
To run the included tests:
```bash
./gradlew connectedAndroidTest
```
Or right-click `ExampleInstrumentedTest` in Android Studio and select "Run".
