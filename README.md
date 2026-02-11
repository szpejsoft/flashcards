# Offline Flashcards App (Android)
## Overview

This project is a **fully offline Android flashcard application designed** to demonstrate modern Android architecture, local data persistence, and robust UI state management without relying on any backend services.
The app allows users to create, organize, and study flashcards entirely offline, with a focus on data consistency, performance, and maintainability.

## Key Features

- Create, edit, and delete flashcards and decks
- Offline-only data storage (no network dependency)
- Study mode with card flipping and writing answers
- Resilient state handling across configuration changes

## Tech Stack

- Language: Kotlin
- UI: Jetpack Compose
- Architecture: MVVM + Clean Architecture principles
- Async: Kotlin Coroutines + Flow
- Persistence: Room (SQLite)
- Dependency Injection: Hilt
- Testing: JUnit, Turbine (Flows), Mockk
- Screen testing: Espresso

## Architecture

The project follows a layered architecture to enforce separation of concerns and testability.

ui/            → Compose UI  
presentation/  → ViewModels  
domain/        → Use cases and business logic  
data/          → Room entities, DAOs, repositories  

## Data Flow

UI observes state exposed by the ViewModels via StateFlow
ViewModel executes domain use cases
Repository coordinates local persistence via Room
Updates are emitted reactively back to the UI
This unidirectional flow keeps UI logic predictable and lifecycle-safe.

## Offline Design

- Room is the single source of truth
- All reads and writes occur locally
- No implicit assumptions about connectivity
- Database operations are optimized using:
  - Proper indexing
  - Transactions for multi-step updates
  - Immutable UI models

This design mirrors real-world mobile constraints where reliability matters more than connectivity.

## State Management

- UI state is modeled explicitly using immutable data classes
- Loading, empty, and error states are handled intentionally
- Compose recomposition is controlled via stable state holders
- No logic inside composables beyond rendering

## Testing Strategy

- Unit tests for:
  - Use cases
  - ViewModels
  - Repository logic
  - Flow emissions verified using the Turbine library

- Konsist tests for
  - Keeping app architecture
  - Ensuring new components have tests
   
User interface tests are deliberately limited to checking that the correct state is displayed
and that the appropriate ViewModel methods are called, as the main focus is on state correctness and business logic.

## Trade-offs & Decisions

- Compose over XML: Improves UI-state alignment and reduces boilerplate
- Clean Architecture: Slight overhead, but scales better for feature growth
- No background sync: Out of scope for this project

## What I’d Improve With More Time

- Polishing UI - I'm currently focusing on logic 
- Accessibility improvements (TalkBack, larger text)
- Spaced repetition scheduling algorithm
- Deck import/export
- UI tests for critical study flows
- Performance profiling for large datasets
- Migrate project to KMP with desktop target

## Why This Project

This project intentionally focuses on core Android engineering challenges:
- Local persistence
- Lifecycle-aware state
- Architecture clarity
- Testability

It reflects the type of decisions and trade-offs required when owning a production Android feature.
