# 🧬 Genetic Algorithm using Jade Framework

## Introduction

This project demonstrates the implementation of a genetic algorithm using the Jade framework. The purpose of this project is to showcase the application of genetic algorithms in solving optimization problems.

## Project Description

The project focuses on solving a specific optimization problem using a genetic algorithm. The optimization problem can be defined as finding the best solution (chromosome) that satisfies a given set of constraints. In this case, the problem is to find the closest approximation to a target solution.

## Genetic Algorithm

The genetic algorithm is a metaheuristic optimization algorithm inspired by the process of natural selection and genetics. It mimics the principles of biological evolution to find optimal solutions to complex problems.

The algorithm starts with an initial population of potential solutions (individuals) represented by chromosomes. Each chromosome encodes a potential solution to the problem. The genetic algorithm then iteratively performs selection, crossover, and mutation operations on the population to evolve and improve the solutions over generations.

### Selection
Selection is the process of choosing individuals from the current population to create the next generation. The selection is typically based on the fitness of each individual, where individuals with higher fitness have a higher chance of being selected.

<img src="captures/tournament_selection.jpg">

### Crossover
Crossover involves combining genetic material from two parent individuals to create offspring individuals. It mimics the reproduction process in nature. Crossover is performed at specific points in the chromosomes, exchanging segments of genetic material between parents to create new solutions.

<img src="captures/cross.png">

### Mutation
Mutation introduces random changes in the genetic material of individuals to maintain diversity in the population. It helps explore new areas of the search space and prevents premature convergence to suboptimal solutions.

<img src="captures/mut.png">

The genetic algorithm continues iterating through the selection, crossover, and mutation operations until a termination condition is met, such as reaching a maximum number of iterations or finding a satisfactory solution.

## Project Structure

The project is structured as follows:

- `MainAgentGA.java`: This class represents the main agent that coordinates the execution of the genetic algorithm. It handles the population management, selection, crossover, and mutation operations.

- `IndividualAgent.java`: This class represents the individual agents in the genetic algorithm. Each agent maintains its own chromosome, performs calculations, and communicates with the main agent to update its fitness.

- `GAUtils.java`: This class contains utility methods and constants used in the genetic algorithm, such as population size, chromosome size, fitness evaluation, and mutation probability.

## Results and Analysis

Upon running the genetic algorithm, the console output will provide information about each generation's fitness values and the best solution found. Analyzing the results, you can observe how the algorithm converges towards the optimal solution.

<img src="captures/GA_GIF.gif">

The execution visualizes the algorithm's performance, showcasing the fitness values across iterations. This visualization can help understand the convergence and efficiency of the genetic algorithm.

## Conclusion

Implementing this genetic algorithm using the Jade framework has allowed me to gain practical experience in solving optimization problems. Through this project, I have developed a solid understanding of genetic algorithms and their application in various domains.

I hope this project demonstrates my comprehension of genetic algorithms and highlights my ability to apply them to real-world problems. Thank you for reviewing my work.

