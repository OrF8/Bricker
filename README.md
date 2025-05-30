# 🧱 Bricker
Bricker is a brick-breaking arcade-style game implemented in Java as part of the Object-Oriented Programming ([**67125**](https://shnaton.huji.ac.il/index.php/NewSyl/67125/2/2025/)) course at the Hebrew University of Jerusalem ([HUJI](https://en.huji.ac.il/)). 

> 🎓 Final Grade: **95**

# ✨ Features
- 🧱 Classic brick-breaking gameplay with modern enhancements.
- 💥 Power-ups including extra paddles, multiple balls, and health bonuses.
- 🧠 Modular strategy pattern for extending game behavior dynamically.
- 🎨 Polished UI assets and sound effects to enhance the game experience.

# 🚀 Installation 
## Prerequisites
- ☕ Java 17+
- Gradle or an IDE like IntelliJ IDEA or Eclipse
- [DanoGameLab](https://danthe1st.itch.io/danogamelab) set as a dependency for the project.
## Steps
1. Clone the repository:
   ````
   git clone https://github.com/yourusername/Bricker.git
   cd Bricker
   ````
2. Open the project in your Java IDE.
3. Ensure DanoGameLab is included in your classpath or set up as a library dependency.
## 🎮 Usage
Run the game by executing the `BrickerGameManager.java` class:
````
# Example if using Gradle
./gradlew run
````
Or from your IDE, run the `BrickerGameManager` main class.

# 📁 Project Structure
````
Bricker/
├── assets/                 # Game assets (images, sounds, etc.)
├── src/bricker/            # Source code
│   ├── brick_strategies/  # Collision and power-up strategies
│   ├── gameobjects/       # Game objects (paddle, ball, bricks, etc.)
│   ├── main/              # Game launcher and core logic
├── README.md
├── LICENSE
````

# ©️ Credits
- This work was made using the [**DanoGameLab**](https://danthe1st.itch.io/danogamelab) library by Dan Nirel.
- Sound and image assets licensed as listed in [````assets/Attribution.txt````](https://github.com/OrF8/Bricker/blob/main/assets/Attribution.txt)

# 📄 License
This project is licensed under the MIT License – see the [**LICENSE**](https://github.com/OrF8/Bricker/blob/main/LICENSE) file for details.
