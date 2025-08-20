# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FormoKaizen is an Android application for managing "Tarjetas Rojas" (Red Cards) - a continuous improvement system for tracking workplace issues and improvements. The app includes user authentication with role-based access (admin/user) and CRUD operations for red cards.

## Build Commands

```bash
# Build the project
./gradlew build

# Run debug build
./gradlew assembleDebug

# Run tests
./gradlew test
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Architecture

### Database Layer (Room)
- **AppDatabase**: Main Room database with entities `User` and `TarjetaRoja`
- **UserDao**: User operations including authentication queries
- **TarjetaRojaDao**: Red card CRUD operations
- **Database file**: `formokaizen_db` created at runtime

### Authentication System
- **LoginActivity**: Email/password authentication with role-based redirection
- **RegisterActivity**: User registration with automatic admin assignment for first user
- **SessionManager**: SharedPreferences-based session management storing user ID
- **User roles**: "admin" and "user" with different capabilities

### Core Models
- **User**: id, name, email, password, role
- **TarjetaRoja**: id, sector, descripcion, motivo, destinoFinal, fotoUri

### Activity Flow
1. **LoginActivity** (launcher) → **HomeActivity** (role-based features)
2. **RegisterActivity** ↔ **LoginActivity** (navigation between auth screens)
3. **HomeActivity** → **NuevaTarjetaActivity** | **ListadoTarjetasActivity**

### Key Implementation Details
- Uses View Binding for all activities
- Room database operations run in coroutines using `lifecycleScope.launch`
- First registered user automatically becomes admin
- Photo URIs stored as strings in database
- Session persists user ID only, not full user data

### Dependencies
- Room database (runtime, ktx, compiler with kapt)
- Lifecycle components (ViewModel, LiveData)
- Kotlinx Coroutines (Android, Core)
- Material Design Components
- ViewBinding enabled

### Testing
- Unit tests: `app/src/test/`
- Instrumented tests: `app/src/androidTest/`
- Uses JUnit and Espresso frameworks