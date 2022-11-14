# NYC Schools

Modular Android architecture which showcases Kotlin, MVVM, Hilt, Coroutines, Flow, LiveData, Retrofit, Moshi, Room, ViewBinding, Unit testing and Kotlin Gradle DSL.

## Features

* Modular Android App Architecture.
* MVVM Architecture + Repository design Pattern.
* Jetpack Libraries and Architecture Components.
* Kotlin Gradle DSL.

## Modules

Modules are collection of source files and build settings that allow to divide a project into discrete units of functionality. The project is divided into the following Modules :

### App module
The `:app` module is a [com.android.application](https://developer.android.com/studio/build/configure-app-module), which is needed to create the app bundle.

### Domain module
The `:domain` module is a [com.android.library](https://developer.android.com/studio/projects/android-library) for serving network requests, and providing the data source for the many features that require it.

### Common module
The `:common` module is a [com.android.library](https://developer.android.com/studio/projects/android-library) only contains non UI code and resources which are shared between feature modules, to avoid dubplication Reusing this way resources, layouts, views, and components in the different features modules, without the need to duplicate code.

### UI Components module
The `:uicomponents` module is a [com.android.library](https://developer.android.com/studio/projects/android-library) only contains UI specific code like themes, icons, layouts, colors, reusable custom views and UI based extensions. Reusing this way all resources, layouts, views, and components in the different features modules.

### Feature module
The :nycschoolinfo module is an [com.android.library](https://developer.android.com/studio/projects/android-library) which is a module containing nyc school information screens, isolated from the rest in accordance with business logic.

![nyc_schools_app_architecture](https://user-images.githubusercontent.com/833213/201615297-6399dd4d-f9f8-4110-9289-a4d9724372d6.jpg)

## UX Flow

* `:app` module is the starter with `SplashActivity.kt` to launch the app. Theme from `:uicomponents` module.
* Prefetch [NYC SAT](https://dev.socrata.com/foundry/data.cityofnewyork.us/f9bf-2cp4) and [Schools](https://dev.socrata.com/foundry/data.cityofnewyork.us/s3k6-pzi2) info while in splash screen.
* Start `MainNavActivity.kt` in `:app`, which the app's navigator.
* Launch `NycSchoolInfoFragment.kt` from `:nycschoolsinfo` module, which is the parent fragment for all schools info
* Embed `NycSchoolSatsFragment.kt` and `NycSchoolDetailFragment.kt` for SAT scores and school detail.
* Both of the above fragments are updated based on data fetched from the repository, business logic housed in respective view models.
* Both of the above fragments are housed in a smooth scrolling `CoordinatorLayout`.
* The list of schools with SAT scores are displayed in a horizontal recycler view which snaps to center in `NycSchoolSatsFragment.kt`
* The more detailed info about the school is displayed in `NycSchoolDetailFragment.kt`, when selecting one the above recycler view items
* Basic search functionality is included in `NycSchoolSatsFragment.kt` to quickly search for specific schools by name.
* Communication between `NycSchoolSatsFragment.kt` and the parent `NycSchoolInfoFragment.kt` happens via a shared view model `MainNavSharedViewModel.kt`
* This shared view model is responsible for transfering list item selection information from child fragment to parent fragment
* Caching is done both in memory and to db for offline loads.

## Testing

Local unit testing is done for ViewModels and Repository via JUnit, Mockk, Robolectric and coroutine test dependencies
