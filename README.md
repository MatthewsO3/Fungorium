# 🍄 Fungorium

A strategic board game implementation developed in Java as part of the Software Project Laboratory course at university.

## 📋 Table of Contents

- [About](#about)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Development](#development)
- [Project Structure](#project-structure)
- [Documentation](#documentation)
- [Team](#team)
- [License](#license)

## 🎯 About

Fungorium is a Java-based strategic board game developed by a 5-person team over the course of a semester. The project demonstrates comprehensive software engineering practices including design, modeling, and implementation phases. The development process is thoroughly documented across approximately 240 pages, detailing design decisions, implementation methods, and project milestones.

## ✨ Features

- **Strategic Gameplay**: Engaging board game mechanics
- **Java Implementation**: Built using modern Java practices
- **Swing GUI**: User-friendly graphical interface using Java Swing
- **Comprehensive Documentation**: Detailed project documentation covering all development phases
- **Team Collaboration**: Developed using collaborative software engineering methodologies

## 🔧 Requirements

- **Java Development Kit (JDK)**: Version 21 or newer
- **Operating System**: Cross-platform (Windows, macOS, Linux)

## 📦 Installation

### Prerequisites
Ensure you have JDK 21+ installed on your system. You can verify this by running:
```bash
java -version
```

### Clone the Repository
```bash
git clone https://github.com/MatthewsO3/Fungorium.git
cd Fungorium
```

## 🚀 Usage

### Compilation and Execution

#### Using PowerShell (Windows)
```powershell
# Compile the project
javac -d out -cp src (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })

# Create JAR file
jar cfm fung.jar MANIFEST.MF -C out .

# Run the game
java -jar fung.jar
```

#### Using Command Line (Unix/Linux/macOS)
```bash
# Compile the project
find src -name "*.java" -type f > sources.txt
javac -d out -cp src @sources.txt

# Create JAR file
jar cfm fung.jar MANIFEST.MF -C out .

# Run the game
java -jar fung.jar
```

## 🛠 Development

This project was developed following software engineering best practices:

- **Phase-based Development**: Structured development phases with clear milestones
- **Team Collaboration**: 5-person development team
- **Design-First Approach**: Comprehensive planning and modeling before implementation
- **Documentation**: Extensive documentation covering all aspects of development

### Development Phases
1. **Planning & Specification**
2. **Design & Modeling**
3. **Implementation**
4. **Testing & Validation**
5. **Documentation & Delivery**

## 📁 Project Structure

```
Fungorium/
├── src/                    # Source code files
├── out/                    # Compiled classes (generated)
├── MANIFEST.MF            # JAR manifest file
├── fung.jar              # Executable JAR file (generated)
└── docs/                 # Project documentation (~240 pages)
```

## 📖 Documentation

The project includes comprehensive documentation (~240 pages) that covers:

- Project specification and requirements
- Design decisions and architecture
- Implementation details and code structure
- Testing methodologies and results
- Development process and team collaboration
- User manual and gameplay instructions

## 👥 Team

This project was developed as part of the Software Project Laboratory course by a collaborative 5-person team, demonstrating effective teamwork and software engineering practices.

## 🎮 How to Play

[Add specific gameplay instructions here once you provide more details about the game mechanics]

## 🐛 Known Issues

[List any known issues or limitations]

## 🤝 Contributing

This project was developed as part of a university course. For academic integrity, please do not submit this code as your own work.

## 📄 License

This project was developed for educational purposes as part of a university course. Please respect academic integrity guidelines when referencing or using this code.

---

**Note**: This project demonstrates the complete software development lifecycle from conception to delivery, including comprehensive documentation and collaborative development practices.
