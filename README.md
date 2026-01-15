# QuestBound – JavaFX Text-Based Adventure

**QuestBound** is a text-based adventure game built using **JavaFX** where the player explores a world through typed commands, interacts with rooms and items, and gradually uncovers the story.

The primary goal of this project is to deepen my understanding of Java, with a strong focus on object-oriented design, application structure, and GUI development using JavaFX.

It also serves as a modern continuation of a text-based game I built during my education, [The Game](https://github.com/Swirl86/AvancJava-Kurs/tree/main/Project%20-%20The%20Game). I found the assignment both fun and challenging, which inspired me to challenge myself further by creating a larger and more structured version of the original idea.

## 🚀 Features (current / planned)

### **Implemented**

- Text-based adventure gameplay with typed commands and buttion actions
- Room-based world structure with descriptions and navigation
- Item interaction (inspect, pick up, drop)
- Player state management (inventory)
- Centralized command parsing and validation
- Dynamic system and ambient messages for improved immersion
- JavaFX-based GUI with input field and output log
- Modular game structure designed for future expansion

### **Planned / Future Enhancements**

- Expanded world with additional rooms and branching paths
- Quest system with objectives and rewards
- NPC interactions and dialogue
- Basic combat mechanics- 
- Player state management (health, mana, stamina)
- Save and load game state
- Improved UI/UX (styling, layout polish)
- Optional enhancements using ControlsFX / BootstrapFX
- Refactoring towards clearer separation of game logic and UI

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
