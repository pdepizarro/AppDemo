# DemoApp -- Weather

DemoApp es una aplicación Android desarrollada en Kotlin que integra
datos meteorológicos en tiempo real mediante **OpenWeather API**,
organizada bajo una arquitectura modular siguiendo los principios de
**Clean Architecture**.
El proyecto pone especial énfasis en la robustez, la escalabilidad y el
correcto uso de corrutinas, animaciones y patrones arquitectónicos
modernos como **MVVM**.

------------------------------------------------------------------------

## 🗝️ Configuración de la API Key (OpenWeather)

Para ejecutar correctamente la aplicación es necesario añadir tu clave
de OpenWeather en el archivo `local.properties`:

    OPEN_WEATHER_API_KEY={tu_api_key}

------------------------------------------------------------------------

## 🧱 Arquitectura & Modularización

El proyecto está dividido en módulos para mejorar la mantenibilidad,
escalabilidad y testabilidad:

    DemoApp
    ├── app
    ├── core
    │   ├── data
    │   ├── domain
    │   ├── uicomponents
    │   └── uinavigation
    │    
    ├── feature
        ├── details
        ├── forecast
        └── shared
    

### 📊 Diagrama de módulos

``` mermaid
graph TD

    App[app] --> FeatureForecast[feature:forecast]
    App --> FeatureDetails[feature:details]
    App --> FeatureShared[feature:shared]

    %% UI infra
    App --> UIComponents[uicomponents]
    App --> UINavigation[uinavigation]

    FeatureForecast --> UIComponents
    FeatureDetails --> UIComponents
    FeatureShared --> UIComponents

    FeatureForecast --> UINavigation
    FeatureDetails --> UINavigation
    FeatureShared --> UINavigation

    %% Clean modules
    FeatureForecast --> CoreDomain[core:domain]
    FeatureDetails --> CoreDomain
    FeatureShared --> CoreDomain

    %% Correct direction
    CoreData[core:data] --> CoreDomain


```

------------------------------------------------------------------------

### 🧬 Comunicación entre capas (Clean Architecture)

- **Domain**: Casos de uso, modelos de negocio y contratos de
  repositorio.
- **Data**: Implementaciones de repositorios, DTOs, mappers, capa
  remota y base de datos local.
- **UI / Feature Modules**: Pantallas Compose, ViewModels y
  navegación.

------------------------------------------------------------------------

### 🔁 Diagrama de comunicación entre módulos

``` mermaid
flowchart LR
    UI[UI / Features\ (Compose + ViewModel)] --> UseCases[core:domain\ - UseCases]
    UseCases --> RepoInterface[core:domain\ - Repository Interfaces]

    RepoInterface --> RepoImpl[core:data\ - Repository Implementations]
    RepoImpl --> RemoteDS[Remote Data Source\ (OpenWeather API via Ktorfit)]
    RepoImpl --> LocalDS[Local Data Source\ (DB / Cache)]

    LocalDS --> RepoImpl
    RepoImpl --> FlowBack[Flows / Result]
    FlowBack --> UI

```

------------------------------------------------------------------------

## 🎨 Patrón Arquitectónico: ¿Por qué **MVVM**?

- Integración natural con Compose.
- Menos boilerplate (Repetición/Reutilización constante de plantillas de codigo estandarizada) que MVP.
- Más simple y menos verboso que MVI.
- Recomendación oficial de Google.

------------------------------------------------------------------------

## 📦 Estrategia de Caché: DB + Refresh Automático

1. La app refresca datos desde la API al arrancar.
2. Guarda los datos en la BD local.
3. La UI observa la BD y no depende directamente de la red para acceder a los datos.

------------------------------------------------------------------------

## ❗ Gestión de Errores con `Result`

- API clara y nativa de Kotlin.
- Perfecta para corrutinas.
- Se descartó `Either` por simplicidad.

------------------------------------------------------------------------

## 🔧 CI/CD: Lint con ktlint

- Ejecutado automáticamente en cada commit/PR.
- Mantiene un estilo uniforme.

------------------------------------------------------------------------

## 🌀 Corrutinas & Animación del Carrusel

- Corrutinas estructuradas.
- Cancelación segura.
- Animación fluida y robusta.

------------------------------------------------------------------------

## 🌐 Networking: ¿Por qué **Ktorfit**?

- Basado en Ktor (multiplatform-ready).
- Menos dependencias que Retrofit.
- DSL moderno y eficiente.

------------------------------------------------------------------------

## 🗡️ Dependency Injection: Hilt

- Mejor integración que Dagger2.
- Más rendimiento que Koin en proyectos grandes.
- Estándar actual en Android.

------------------------------------------------------------------------

## 🧪 Testing: mockK

- Soporte nativo para corrutinas.
- Mock de clases finales y funciones suspend.
- API clara y orientada a Kotlin.

------------------------------------------------------------------------

## 🚀 Escalabilidad & Navegación

- Navegación modular por feature.
- Cada módulo define su propio grafo.
- Fácil expansión hacia nuevas features.


------------------------------------------------------------------------

## 📄 Licencia

Este proyecto se distribuye bajo la licencia incluida en el archivo
`LICENSE`.