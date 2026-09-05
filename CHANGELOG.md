# Registro de Cambios (Changelog)

Todos los cambios relevantes de **GangFights** se documentarán en este archivo.

## [0.2.5] - 2026-09-04

### Añadido
- Integración de los gráficos y sprites oficiales del personaje **Schepis** para la personalización visual y diferenciación total del segundo jugador en pantalla.
- Implementación de la mecánica de combate bidireccional y eliminación instantánea al contacto físico y de proximidad:
    - El Jugador 1 acciona el golpe al presionar la tecla `1`, evaluando la distancia respecto al rival para eliminarlo.
    - El Jugador 2 acciona el ataque al presionar la tecla `N`, provocando la destrucción del cuerpo físico del contrincante al impactar.

### Cambiado
- Actualización de los métodos de comprobación de ataques (`comprobarAtaque`) para validar de manera segura la existencia de las entidades y sus cuerpos físicos antes de calcular los rangos de alcance.

### Corregido
- Solución definitiva al error de tipo `NullPointerException` en el ciclo de renderizado provocado al intentar leer la posición de un cuerpo de Box2D previamente destruido tras recibir un golpe.
- Sincronización de los estados de activación de los combatientes (`isActivo()`) para evitar llamadas huérfanas en el `SpriteBatch` y el `ShapeRenderer`.

## [0.2.4] - 2026-09-02

### Añadido
- Alta y estructuración del segundo jugador (`jugador2`) dentro de la pantalla de juego con soporte de movimiento independiente mediante las flechas direccionales del teclado (*UP, DOWN, RIGHT, LEFT*).
- Implementación preliminar de las funciones de lógica espacial para la futura detección de colisiones y alcances de combate entre ambos contrincantes.
- Resolución de conflictos en el mapeo de teclas concurrentes para evitar que la lectura de entradas del primer jugador interfiera con el desplazamiento del segundo en simultáneo.

### Cambiado
- Duplicación y adaptación de la instanciación de entidades en `PantallaJuego` para manejar de manera concurrente dos personajes activos simultáneamente en el escenario utilizando temporalmente los recursos base.

## [0.2.3] - 2026-08-31

### Añadido
- Incorporación de la primera hoja de sprites completa del personaje **Jasinski** para el primer jugador, configurando las animaciones detalladas de:
    - Estado quieto con bucle de respiración.
    - Desplazamiento de carrera fluida.
    - Transición y frame estático de salto.
    - Postura de agachado.
    - Animación de ataque de golpe de mano (`GolpeMano`).
- Implementación del sistema de inversión horizontal de los `TextureRegion` (`flip`) para reflejar dinámicamente la orientación del personaje según la dirección de su mirada (izquierda o derecha).
- Resolución de conflictos visuales en los sprites al invertir la dirección del personaje, evitando parpadeos o inversión incorrecta de las texturas en pantalla.

### Cambiado
- Reestructuración de la máquina de estados lógicos en el método de obtención del frame actual (`getFrameActual`), priorizando las animaciones de acción y movimiento por sobre el estado estático predeterminado.

## [0.2.2] - 2026-08-30

### Añadido
- Integración del sistema de desplazamiento inicial para el primer jugador utilizando los controles de teclado WASD (`W`, `A`, `S`, `D`).
- Soporte para la aplicación de fuerzas y velocidades lineales horizontales sobre el cuerpo físico de Box2D del jugador.

### Cambiado
- Ajuste en la lectura de eventos de entrada del teclado para diferenciar de manera fluida entre acciones continuas de movimiento y activaciones de pulso único.

### Corregido
- Optimización en el procesamiento de las velocidades del cuerpo físico para evitar deslizamientos indeseados al soltar las teclas de dirección.


## [0.2.1] - 2026-08-29

### Añadido
- Creación de la clase `Jugador` para gestionar la lógica individual de los personajes, el control de estados físicos y las animaciones dentro del mundo de Box2D.
- Implementación de la clase `Mapa` y el sistema de plataformas (`Plataforma`) para definir los límites estáticos del escenario de combate.
- Estructuración inicial de la clase `PantallaJuego` como el entorno principal donde interactúan la física, el mapa y los combatientes.

### Cambiado
- Configuración inicial de las dimensiones base del mundo de juego para estandarizar la escala de renderizado de las entidades frente al mapa preliminar.

### Corregido
- Sincronización inicial de las coordenadas de posición entre los objetos lógicos de creación y las dimensiones reales de los sprites en pantalla.

## [0.2.0] - 2026-08-28

### Añadido
- Clase `PantallaInicio` con transición temporalizada automatizada hacia el menú principal.
- Clase `MenuInicial` estructurada como punto de partida para la interfaz del usuario.
- Paquete `Utilidades` para estandarizar el código del proyecto:
    - `Render`: Gestor estático de `SpriteBatch` y encapsulamiento de limpieza OpenGL (`limpiarPantalla`).
    - `Imagen`: Envoltorio personalizado de `Texture` y `Sprite` con soporte para manipulación de opacidad (`setTrasparencia`), coordenadas y lectura de dimensiones.
    - `Recursos`: Diccionario estático de rutas para centralizar el llamado a los assets gráficos.

### Cambiado
- La clase raíz del juego (`Principal`) ahora extiende `Game` en lugar de `ApplicationAdapter` para habilitar el manejo múltiple de pantallas.
- La lógica matemática del desvanecimiento visual (fade) en `PantallaInicio` ahora se calcula en segundos reales utilizando la variable `delta`, reemplazando la ejecución por iteración de frames.

### Corregido
- Superposición gráfica de pantallas solucionada agregando la limpieza del buffer (`glClearColor`) en los métodos de renderizado.
- Prevención de conflictos de compilación en Android/HTML mediante la eliminación de dependencias incompatibles de Java clásico (`java.awt.*`).

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
