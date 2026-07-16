# GangFights - Juego Multijugador de Peleas Local 2D 

<p align="center">
  <b>Proyecto Final de 6to Año - Especialidad Computación</b><br>
  <b>E.T. N°35 "Ing. Eduardo Latzina"</b><br>
<br>
  <b>Lafflitto - Sivale - Torrez</b>

</p>

---
##  -  Descripción de la Propuesta
**GangFights** es un videojuego de acción, peleas y disparos en 2D en perspectiva lateral (estilo arcade retro pixel art), diseñado para partidas multijugador 1vs1 en red local (LAN).

Inspirado fuertemente en el clásico ***Superfighters***, el juego sitúa a los jugadores en arenas cerradas y dinámicas llenas de obstáculos, plataformas y desniveles. El objetivo principal es derrotar a los oponentes utilizando una combinación de:
* **Combate cuerpo a cuerpo:** Golpes básicos y empujones para desestabilizar al rival.
* **Armas de fuego e ítems:** Aparición dinámica de armamento en el mapa (pistolas, escopetas, ametralladoras) y elementos arrojadizos.
* **Interacción con el entorno:** Uso de coberturas, barriles y plataformas para obtener ventajas tácticas.

---
## - Tecnologias y herramientas
El ecosistema de desarrollo ha sido seleccionado para garantizar un rendimiento óptimo en la renderización y la comunicación en red:

* **Lenguaje:** Java 21 (JDK 21).
* **Framework Multimedia:** [LibGDX](https://libgdx.com/) (Módulos: *Core* para lógica compartida y *LWJGL3* para el backend de escritorio).
* **Motor de Físicas:** Box2D (integrado en LibGDX) para colisiones precisas y dinámicas de entornos.
* **Gestor de Dependencias y Construcción:** Gradle.
* **Arquitectura de Software:** Orientada a Objetos (POO) y Patrón de Diseño Estado (State Pattern) para las máquinas de estado de los personajes (Idle, Running, Jumping, Attacking, Hurt, Dead).

---
## - Estructura General del Proyecto
La solución generada con LibGDX Liftoff sigue un patrón de desarrollo multi-módulo que separa la lógica del motor de la ejecución en plataformas específicas:

```
GangFights/
├── assets/             # Recursos compartidos (Texturas, fuentes, mapas Tiled, audio)
├── core/               # LÓGICA PRINCIPAL (Entidades, pantallas, física, red, colisiones)
├── lwjgl3/             # Lanzador específico para PC (Windows, macOS, Linux)
├── html/               # Módulo de compatibilidad web (GWT)
├── build.gradle        # Script de configuración del proyecto y dependencias de Gradle
└── settings.gradle     # Definición de módulos del proyecto
```
---
## -  Cómo Compilar y Ejecutar de forma Local
**Requisitos Previos:** 

<p> - Tener instalado el Java Development Kit (JDK) 21 o superior. </p>
 - IDE recomendado: IntelliJ IDEA o Android Studio con soporte Gradle.

**Pasos para Ejecutar:**
**1 .Clonar el Repositorio:**
```
<p>Bash<p/>
git clone [https://github.com/RodriTor/GangFights.git](https://github.com/RodriTor/GangFights.git)
```
**2. Importar en el IDE:**

-Abrí tu IDE y seleccioná Open o Import.

-Elegí el archivo **build.gradle** que está en la raíz de la carpeta del proyecto.

-Seleccioná Open as Project y esperá a que Gradle descargue todas las dependencias necesarias.

**3. Correr el Videojuego:**

Desplegá la carpeta del módulo lwjgl3.

Buscá la clase Lwjgl3Launcher.java (en lwjgl3/src/main/java/...).

Hacé clic derecho sobre la clase y seleccioná Run 'Lwjgl3Launcher.main()'.

---
## Enlace a la Wiki del Proyecto (Propuesta Completa)
* [Ver la Propuesta Completa del Proyecto aquí](https://github.com/RodriTor/GangFights/wiki/Home)

## - Estado de avances actual del proyecto
Actualmente el proyecto se encuentra en la Fase 1: Configuración inicial y arquitectura base.
```
[x] Generación del proyecto base con LibGDX Liftoff.

[x] Configuración del repositorio remoto en GitHub.

[x] Redacción inicial de la propuesta de desarrollo.
```
---

# Tutor del Proyecto:
Prof. Pablo Jasinski
