# 2048

![2048 Logo](./icon.png)

Java implementation of the popular 2048 game using Maven. The game features a grid where players can combine tiles with the same number to reach the 2048 tile. The project includes gameplay mechanics, scoring, adjustable grid sizes, high score management, and unit tests.

## Features

- **Gameplay**: Play the classic 2048 game with intuitive controls.
- **Scoring**: Keep track of the current score and high scores.
- **Adjustable Grid Size**: Start games with different grid sizes.
- **High Score Management**: Save and retrieve high scores for different grid configurations.
- **Multiple Games**: Play multiple games in a single session.

## Setup Instructions

### JAR

1. Download the latest JAR file from the [Releases](https://github.com/boxboxjason/2048/releases) page.
2. Open a terminal and navigate to the directory where the JAR file is located.
3. Run the game using the command: `java -jar 2048-<version>.jar`

### Source Code

1. Clone the repository: `git clone https://github.com/boxboxjason/2048.git`
2. Navigate to the project directory: `cd 2048`
3. Build the project using Maven: `mvn clean install`
4. Run the game: `mvn exec:java -Dexec.mainClass="com.boxboxjason.games._2048.App"`

## Gameplay Instructions

- Use the arrow keys to move the tiles. (You can change the controls in the settings.)
- Combine tiles with the same number to create a new tile with their sum.
- The goal is to reach the 2048 tile, but you can continue playing to achieve higher scores.

## Development

- To run the unit tests, use the following Maven command: `mvn test`
- To run static code analysis, code coverage, dependency checks and generate reports, use: `mvn verify`
