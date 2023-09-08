# Go4Lunch

## Table of Contents
1. [General Info](#general-info)
2. [Demo](#demo)
3. [Utilized Skills](#utilized-skills)
4. [Installation](#installation)
5. [Configuration](#configuration)
6. [Run and compile](#run-and-compile)

### General Info
***
Go4Lunch is a convenient lunchtime gathering application designed for colleagues. 
It simplifies the process of planning lunch breaks by allowing users to see where their colleagues intend to dine, making it easier to meet up and enjoy a meal together.
#### Logo
<img src="https://github.com/Rudyboyy/Go4Lunch/blob/main/app/src/main/res/drawable/restaurant_logo.png" alt="Logo" width="300" height="300">

### Demo
***

https://github.com/Rudyboyy/Go4Lunch/assets/96139750/9cad5c70-1254-4e8e-a762-756c25b9bcef

### Utilized Skills
***
- **Programming Language:** Java
- **Android Architecture:** MVVM (Model-View-ViewModel)
- **User Interface:** XML for creating the user interface
- **Integration of Google Maps API**
- **Firebase:** User authentication, Firestore, Firebase Realtime Database
- **Notifications**
- **Google Sign-In and Facebook Login Integration**
- **RecyclerView and LiveData Usage**
- **Retrofit for API Requests**
- **RxJava and RxAndroid for Reactive Programming**
- **Navigation Component for Fragment Navigation**
- **Material Design Components**
- **Unit Testing with JUnit and Mockito**
- **UI Testing with Espresso**
- **Version Control:** Use of Git for version control
- **Compatibility with Android Versions:** Compatible with Android versions 21 (minSdkVersion) to 33 (compileSdkVersion)
- **Third-party Libraries:** Glide for image loading, FirebaseUI for Firebase integration, Retrofit for HTTP requests, RxJava for reactive programming.


### Installation
***
A little intro about the installation. 
#### Option 1
Use the terminal of Android Studio, copy and execute the command line :
```
$ git clone https://github.com/Rudyboyy/Go4Lunch.git
```
#### Option 2
* Download the ZIP folder -> https://github.com/Rudyboyy/Go4Lunch/archive/refs/heads/main.zip
* Open it in Android Studio

### Configuration
***
To make this application work correctly, you need to add your own Google Maps API key. Follow the steps below to set up the API key:
1. Go to [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing one.
3. In the project dashboard, navigate to the "APIs & Services" section and click on "Library" to search for and enable the "Maps SDK for Android" API.
4. Once the API is enabled, click on "Credentials" in the "APIs & Services" section to create an API key. Select "Create credentials" > "API key."
5. Follow the instructions to generate the Android API key. When prompted, add the application information such as the package name. You can find the package name in your `build.gradle` file (usually under `defaultConfig -> applicationId`).
6. Once the key is generated, copy it.
7. In your Android project, open the `local.properties` file and add a line with your API key like this (replace `YOUR_API_KEY` with the key you copied):

   ```properties
   MAPS_API_KEY=YOUR_API_KEY

### Run and compile
***
* Choose a device (virtual or physical)
* Click on Run or use <kbd>Shift</kbd>+<kbd>F10</kbd>
