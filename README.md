# Rick & Morty Characters — MVI clean architecture

A state-of-the-art Android application that consumes the public Rick & Morty API, showcasing high-performance reactive presentation layouts, type-safe navigation, clean modular separation of concerns, and robust unit testing.

---

## 🛠️ Technology Stack

| Component | Technology | Role & Advantages |
| :--- | :--- | :--- |
| **Language** | **Kotlin** | Clean, concise, and safe programming language with coroutines first-class support. |
| **Presentation (UI)** | **Jetpack Compose** | Fully declarative, atomic, stateless layout design with custom Glassmorphism themes. |
| **Architecture** | **Clean Architecture + MVI** | Strict domain-data separation paired with one-way reactive state machines. |
| **DI Engine** | **Koin DI** | Pure Kotlin constructor-reference dependency injection without annotation processing overhead. |
| **Navigation** | **Type-Safe Compose Navigation** | Native `@Serializable` navigation route declarations with decoupled callbacks. |
| **Networking** | **Retrofit 2 + Gson** | Safe, robust suspend-driven HTTP requests with auto-serialization. |
| **Image Loading** | **Coil (AsyncImage)** | Highly efficient image loading with smart memory/disk caching and custom placeholders. |
| **Pagination** | **Paging 3** | Smooth infinite scroll indexing with cache flow streams bound to presentation lifecycle. |
| **Validation** | **JUnit 4 + kotlinx-coroutines-test** | Unit tests for MVI action validation and state emission streams using `UnconfinedTestDispatcher`. |

---

## 📐 Architecture Blueprints

The application is engineered around two core software engineering patterns:

### 1. Three-Tier Clean Isolation
- **Domain Layer:** Pure Kotlin package containing the business entity [Character](app/src/main/java/com/example/rickmortychallenge/domain/model/Character.kt) and the repository contract [CharacterRepository](app/src/main/java/com/example/rickmortychallenge/domain/repository/CharacterRepository.kt). It has absolutely no dependency on databases or networking libraries.
- **Data Layer:** Implements domain contracts via [NetworkCharacterRepository](app/src/main/java/com/example/rickmortychallenge/data/repository/NetworkCharacterRepository.kt), coordinating pagination through `CharacterPagingSource` and remote fetching via Retrofit `CharacterApiService`.
- **Presentation Layer:** view state flow collection using the decoupled Root/Screen pattern.

### 2. Unidirectional MVI State Machine
Every screen utilizes an explicit state, action (intention), and event channel structure:
```
[ stateless Screen Composable ] ────( Action / Intention )────> [ ViewModel ]
              ▲                                                      │
              │                                                ( Processes Logic )
              │                                                      │
      ( StateFlow State ) <────────────[ New State ] <───────────────┘
```

---

## 📈 GitFlow Branching Structure

The repository is built strictly around the **GitFlow** branching model, with each implementation phase decoupled into its own individual feature branch, merged sequentially back into `develop` with standard no-fast-forward (`--no-ff`) commits:

- **`main`:** Production-ready releases.
- **`develop`:** Latest integration development.
- **`feature/setup-config`:** Shared dependency catalog and base project config.
- **`feature/domain-layer`:** Establishes domain models and repository contracts.
- **`feature/data-layer`:** Implements PagingSource, Retrofit API endpoints, and repositories.
- **`feature/di-navigation`:** Standard Koin DI modules and serializable navigation routes.
- **`feature/presentation-ui`:** Stateless list/detail Compose screens and reactive ViewModels.
- **`feature/unit-testing`:** Decoupled ViewModel unit test cases.
- **`feature/show-detail-url`:** Interactive clickable profile hyperlinks in character details.
- **`feature/composable-previews`:** Exhaustive Compose Previews mapping out Loading, Success, and Error states.

---

## 🚀 How to Run & Verify

### Compilation & Run:
Open the project in **Android Studio Hedgehog** or superior. Sync gradle files and run. No API keys are required.

### Executing Tests:
To compile the codebase and run all unit tests locally, execute in your terminal:
```powershell
./gradlew test
```

---

## 🤖 Agentic AI & Engineering Tools

This project was developed and audited using state-of-the-art agentic coding tools and engineering guides:

1. **Antigravity CLI (DeepMind AI Pair Programmer):** The entire application structure, migrations, and clean architectural refactoring were pair-programmed using the Antigravity CLI, Google DeepMind's advanced agentic developer.
2. **Official Android Architecture Skills:** All architectural models, Koin DI bindings, MVI workflows, and type-safe navigation pipelines were built in strict compliance with official Android architecture and presentation guidelines.
3. **Android CLI:** Project compilation, validation tests, and dependency graph checks were orchestrated locally using the Android command line utilities.

---

## 💡 Future Roadmaps & Potential Improvements

To scale this application into a commercial-grade product, the following future roadmap items could be integrated:

1. **Offline-First Support (Room + RemoteMediator):** Cache characters locally in a Room database using Paging 3's `RemoteMediator` to serve as a Single Source of Truth, enabling full offline navigation.
2. **Compose Shared Element Transitions:** Animate character images (`AsyncImage`) seamlessly when navigating between list and details screens to provide a premium, smooth visual UX.
3. **Skeletal Shimmer Loading:** Replace default progress indicators with custom skeletons mimicking cards layout while downloading network data.
4. **Dynamic Search Bar:** Add `CharacterAction.OnSearchQueryChange(query)` to filter Paging 3 query flows reactively in real time.
5. **Multi-Pane Adaptive Layouts:** Support large screens, table landscapes, and foldables by showcasing a List-Detail split layout using Compose Window Size Classes.
6. **Linting and Format Checkers:** Configure detekt and ktlint within Gradle to automate static code analysis and codebase format checks.
