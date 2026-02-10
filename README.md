# QuestBound – JavaFX Text-Based Adventure

**QuestBound** is a text-based adventure game built using **JavaFX** where the player explores a world through typed commands, interacts with rooms and items, and gradually uncovers the story.

The primary goal of this project is to deepen my understanding of Java, with a strong focus on object-oriented design, application structure, and GUI development using JavaFX.

It also serves as a modern continuation of a text-based game I built during my education, [The Game](https://github.com/Swirl86/AvancJava-Kurs/tree/main/Project%20-%20The%20Game). I found the assignment both fun and challenging, which inspired me to challenge myself further by creating a larger and more structured version of the original idea.

---

## 🎮 Gameplay Overview

The game is played by entering text commands or using available UI controls.  
The player can:

- Move between rooms
- Inspect the environment
- Pick up and drop items
- View inventory and player state
- Receive system and ambient messages that enhance immersion

The game logic is separated from the UI to make future expansion and refactoring easier.

---

## 🚀 Features (current / planned)

### **Implemented**

- Text-based adventure gameplay with typed commands and buttion actions
- Room-based world structure with descriptions and navigation
- Item interaction (inspect, pick up, drop)
- Player state management (inventory)
- Centralized command parsing and validation
- Dynamic system and ambient messages for improved immersion
- Modular game structure designed for future expansion
- Expanded game world featuring multiple interconnected rooms, alternative paths, and non-linear navigation

### **Planned / Future Enhancements**
- Quest system with objectives and rewards
- NPC interactions and dialogue
- Basic combat mechanics
- Extended player state (health, stamina, etc.)
- Save and load game state
- Improved UI and visual polish
- Optional UI enhancements using ControlsFX or BootstrapFX
- Additional refactoring to further separate game logic from UI

---

## 🧱 Architecture Overview

The project follows an MVC-inspired and layered structure:

- **Game logic** is handled independently from the UI
- **Command parsing** is centralized to ensure consistent behavior
- **Rooms, items, and player state** are modeled using OOP principles
- **JavaFX** acts as the presentation layer only

This structure is designed to support future features without large rewrites.

---

## 📦 Technologies

| Layer / Concern    | Technologies                        |
| ------------------ | ----------------------------------- |
| Language           | Java 17                             |
| UI                 | JavaFX 21                           |
| Architecture       | MVC-inspired / layered design       |
| Game Logic         | Object-Oriented Programming (OOP)   |
| State Management   | In-memory game state                |
| Build & Dependency | Maven                               |
| Styling (planned)  | JavaFX CSS, ControlsFX, BootstrapFX |
| Testing (planned)  | JUnit                               |
| Tooling            | IntelliJ IDEA                       |

---

## How to Run

Clone the repository:

```bash
git clone git@github.com:Swirl86/javafx-text-adventure.git
cd javafx-text-adventure
```

Build and run with Maven:
```bash
mvn clean compile exec:java
```

## 📌 Project Goals

- Reinforce core Java skills through a non-trivial, state-driven application
- Apply object-oriented principles such as encapsulation, responsibility separation, and extensibility
- Practice structuring a larger JavaFX application beyond simple demos
- Explore how game logic can be decoupled from UI concerns
- Build a portfolio project that demonstrates long-term maintainability and growth
