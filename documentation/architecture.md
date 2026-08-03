# Architecture

## Architectural Style

Helix uses a modular Android architecture with a small application shell, feature modules, shared UI, shared models, and a data layer.

The application follows a practical MVVM style:

- Compose screens render state and send user actions.
- ViewModels own screen state and launch coroutines.
- Repositories coordinate local and remote data sources.
- Services contain domain, AI, networking, and platform integrations.
- Koin creates and supplies dependencies.

## Gradle Modules

```text
:app
  Application shell, MainActivity, navigation, Koin startup

:feature:dashboard
  Dashboard, food log, scanner UI, coach UI, dashboard ViewModels

:feature:tracking
  Progress screen and weight tracking ViewModel

:feature:onboarding
  Profile setup screen and ViewModel

:feature:settings
  User settings, API key settings, permissions, developer tools

:core:data
  Room, repositories, APIs, AI services, workers, preferences

:core:model
  Pure Kotlin shared models and constants

:core:ui
  Compose theme and shared UI components

:baselineprofile
  Baseline profile generation for startup and runtime performance
```

## Dependency Direction

```text
app
  -> feature modules
  -> core:data, core:model, core:ui

feature modules
  -> core:data, core:model, core:ui

core:data
  -> core:model

core:ui
  -> core:model
```

The application shell does not directly implement business logic. It creates the navigation graph and starts dependency injection.

## Application Startup

`HealthTrackerApplication` starts Koin and loads:

- `coreDataModule`
- `dashboardModule`
- `onboardingModule`
- `trackingModule`
- `settingsModule`

`MainActivity` installs the splash screen, enables edge-to-edge rendering, and displays `HealthTrackerApp` inside `HealthTrackerTheme`.

The app checks `UserPreferences.isOnboarded` before choosing its start destination:

- New user: onboarding
- Existing user: main tab screen

## Navigation

The main navigation has three tabs:

- Dashboard
- Progress
- Settings

Navigation uses Jetpack Compose Navigation and `NavigationSuiteScaffold`. The scaffold adapts the navigation presentation to the device layout.

## State Flow

The ViewModels expose `StateFlow` values. Compose collects these flows and redraws the affected UI when data changes.

The dashboard combines:

- Selected date
- Weight for that date
- Profile for that date
- Current preferences
- Food entries for the date
- Recent food entries

The result is a `DashboardState` containing totals, targets, current weight, selected date, and recent entries.

## Dependency Injection

Koin provides singleton services such as:

- `AppDatabase`
- Room DAOs
- `UserPreferences`
- Retrofit and OkHttp
- `FoodRepository`
- `WeightRepository`
- `ProfileRepository`
- `VisionRepository`
- `DistilBertFoodClassifier`
- `FoodVisionService`
- `CoachRepository`
- `WorkManager`

Feature modules provide their ViewModels. This keeps ViewModel constructors explicit while avoiding manual creation in the activity.

## Core Data Flow

```text
Compose screen
    -> ViewModel
    -> Repository or service
    -> Room, DataStore, HTTP API, or AI model
    -> Flow or result
    -> ViewModel state
    -> Compose screen
```

## Food Entry Flow

When food is added, `DashboardViewModel`:

1. Creates a `FoodEntry`.
2. Stores it through `FoodRepository` and `FoodDao`.
3. Resets scanner state.
4. Builds a `FoodContext` using the entry and current target information.
5. Runs the local DistilBERT classifier.
6. Enqueues `InvisibleCoachWorker` if the risk threshold is met.

## Persistence

Room stores:

- Food entries
- Weight entries
- Profile entries
- Cached barcode products

DataStore stores:

- Onboarding state
- Goals and calorie settings
- Personal profile preferences
- Target weight
- Coach response and timestamp
- Coach API-key validity state

The Room database uses destructive migration fallback for this demonstration project.
