# Registro de Cambios (Changelog)

Todos los cambios relevantes de **GangFights** se documentarán en este archivo.

## [0.1.1] - 2026-08-26

### Corregido
- Unificada definitivamente la configuración de Java a la versión 21 en el submódulo `lwjgl3`.
- Eliminado por completo el módulo web `html` para evitar conflictos de compatibilidad de versiones.
- Sincronizada la versión del proyecto en `gradle.properties` con el changelog.
- Corregida la fecha inválida anterior en la documentación del CHANGELOG.
- Corregido el comando de clonación de Git y añadidos los comandos de ejecución desde terminal en el `README.md`.
- Agregada la declaración explícita de LibGDX v1.14.2 en la documentación.

### Añadido
- Enlace directo a la Wiki del proyecto en la sección de documentación del `README.md`.
- Subida de bocetos, diagramas e imágenes ilustrativas a la Wiki del repositorio.

## [0.1.0] - 2026-07-16

### Añadido
- Estructura base del videojuego generada mediante LibGDX Liftoff.
- Configuración de dependencias y extensiones: FreeType, Box2D, gdx-ai, Controllers y Box2DLight.
- Integración del módulo ejecutable `lwjgl3` para desarrollo y ejecución en PC.
- Creación de archivos de control y documentación inicial: `.gitignore`, `README.md` y `CHANGELOG.md`.

## [0.0.1] - 2026-06-20

### Añadido
- Definición formal de la propuesta del proyecto (Introducción, alcance y objetivos de GangFights).
- Definición de la modalidad de red local (LAN) y temas a investigar.
- Descripción de mecánicas de juego, vistas 2D estilo retro pixel art, física y elementos interactivos.
- Tácticas de trabajo, planificación de tareas y asignación de roles.
- Carga inicial de bocetos e ilustraciones en la Wiki.
