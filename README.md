# 🏆 Trivia Master - Práctica Final DAM

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![POO](https://img.shields.io/badge/Paradigma-POO-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Versi%C3%B3n%20Final-brightgreen?style=for-the-badge)

**Trivia Master** es una aplicación de consola interactiva desarrollada en Java como proyecto final para el primer año de **Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**. El juego desafía a los jugadores con diferentes tipos de retos, desde preguntas de conocimiento general hasta cálculos matemáticos y retos de precisión temporal.

---

## ✨ Características Principales

- **🎮 Experiencia de Usuario Premium**: Interfaz de consola rediseñada con colores ANSI y efectos visuales para una mejor inmersión.
- **🧠 Motor Matemático Personalizado**: Implementación propia de un evaluador de expresiones matemáticas dinámicas para el módulo de preguntas de cálculo.
- **⏱️ Retos de Cronómetro**: Desafía tu percepción temporal con el "Cronómetro Mental".
- **🏆 Sistema de Ranking y Evolución**: Seguimiento de puntuaciones históricas y ranking global de jugadores.
- **🔥 Sistema de Rachas**: Premios por encadenar respuestas correctas consecutivas.
- **🤖 Oponentes CPU**: Juega solo o con amigos contra inteligencias artificiales.
- **📁 Persistencia de Datos**: Almacenamiento local de histórico de partidas y ranking en archivos planos.

---

## 🚀 Instalación y Ejecución

### Requisitos

- **Java JDK 17** o superior.
- Terminal compatible con códigos de escape ANSI (Linux/macOS recomentado, o Windows Terminal).

### Ejecución

1. Clona el repositorio:
   ```bash
   git clone https://github.com/angelgmx/PracticaFinal.git
   ```
2. Compila los archivos (desde la carpeta `src`):
   ```bash
   javac juego/main/Juego.java
   ```
3. Inicia el juego:
   ```bash
   java juego.main.Juego
   ```

---

## 🛠️ Estructura del Proyecto

La arquitectura sigue los principios de la **Programación Orientada a Objetos (POO)** con una clara separación de responsabilidades:

- `juego.main`: Punto de entrada y gestión del menú principal.
- `juego.model`: Entidades principales (Jugador, Partida, CPUs).
- `juego.service`: Lógica de negocio (Ranking, Histórico, Gestor de Jugadores).
- `juego.questions`: Diferentes tipos de preguntas y generadores.
- `juego.config`: Configuración global y constantes.

---

## 🎨 Capturas de Pantalla

_Próximamente..._ (El diseño actual usa colores vibrantes en terminal para distinguir turnos, aciertos y errores).

---

## 👤 Autor

**Ángel Gómez**

- LinkedIn: [angelgmx](https://linkedin.com/in/angelgmx)
- GitHub: [angelgmx](https://github.com/angelgmx)

---

> _Este proyecto ha sido mejorado y pulido para demostrar habilidades avanzadas en lógica de programación, manejo de colecciones y diseño de interfaces en consola._
