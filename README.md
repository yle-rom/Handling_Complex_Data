# Handling Complex Data

![Language](https://img.shields.io/badge/Language-Java-blue.svg)
![Topic](https://img.shields.io/badge/Topic-Data_Management-green.svg)

## Project Overview

This repository contains Java implementations of advanced data management and spatial indexing algorithms. The projects were developed to process complex datasets efficiently without relying on external database management systems. The focus is on low-level relational database operations, spatial data structures, and optimized query execution techniques.

## Assignments Overview

### Assignment 1: Relational Data Processing

This assignment focuses on implementing standard database operations on raw CSV files using memory-efficient algorithms.

*   **Merge Sort & Aggregation:** Implements an external-style recursive merge sort algorithm that sorts data by a specific grouping attribute. During the merge phase, it simultaneously applies an aggregation function (SUM, MIN, or MAX) to a target attribute when duplicate grouping keys are encountered. The reduced, sorted, and aggregated dataset is saved to a new file.
*   **Natural Join (Merge Join):** Combines two external datasets by simulating a relational Merge Join. It iterates through two files using buffered readers and, when the designated keys match, merges the columns into a new unified record.
*   **Composite Query Execution:** A custom query processor that evaluates a composite condition across two tables. It sequentially scans the data to find matching keys between the tables while specifically checking for inclusion/exclusion conditions on a third attribute (e.g., skipping records where column C equals 7), and dynamically calculates the sum of the valid records.

### Assignment 2: Spatial Data Indexing (R-Tree)

This assignment focuses on indexing 2D spatial data to optimize coordinate-based querying.

*   **Sort-Tile-Recursive (STR) Construction:** Implements the STR bulk-loading algorithm to build an R-Tree. It sorts 2D points by their X-coordinate, divides them into vertical stripes based on the square root of the data size, and then sorts each stripe by the Y-coordinate to pack points into Minimum Bounding Rectangles (MBRs) with minimal spatial overlap.
*   **Hardware-Constrained Node Capacities:** The algorithm strictly enforces node capacities based on a theoretical 1024-byte hardware page limit. Leaf nodes are capped at 51 points (calculating line number, X, and Y bytes), while internal nodes are capped at 28 MBR entries.
*   **Incremental Best-First Nearest Neighbor (BFNN):** A spatial search algorithm utilizing a Min-Heap priority queue to find the 'K' nearest neighbors to a geometric query point. It calculates distances using Euclidean math for exact points and a custom `mindist` function for MBRs. The search is incremental, allowing the system to seamlessly fetch the K+1 and K+2 neighbors by maintaining the queue's state without restarting the search.

### Assignment 3: Top-K Join Processing

This project implements and compares two different strategies for executing a Top-K Join query. The goal is to find the top 'K' matching couples from two datasets based on a maximized combined instance weight. Both algorithms dynamically filter out individuals under 18 years old or with specific marital statuses before processing.

*   **Optimized Threshold Join (Algorithm A):** Implements an early-terminating Top-K algorithm similar to Fagin's Threshold Algorithm (TA). It sequentially interleaves reads between both datasets, hashing valid entries by age. It maintains the maximum weights seen so far to calculate a dynamic threshold score (using the formula T = 0.5 * p1 + 0.5 * p2). The algorithm drastically reduces I/O operations by halting execution the moment the top joined couple's score in the Max-Heap meets or exceeds the threshold.
*   **Baseline Hash Join (Algorithm B):** Acts as a performance baseline. It sequentially reads and fully materializes the valid records from the first dataset into a Hash Map. It then scans the second dataset, exhaustively generating every possible valid join, pushing them into a Priority Queue, and extracting the top K. This highlights the execution time improvements and memory efficiency achieved by Algorithm A.

## What was Learned

*   How to implement foundational database algorithms from scratch, such as Merge Sort and Merge Join, for processing structured external data.
*   Translating hardware memory constraints into software logic by calculating exact byte capacities for spatial tree nodes to optimize disk I/O.
*   The mechanics of bulk-loading spatial data into an R-Tree and utilizing Priority Queues to perform efficient Incremental Nearest Neighbor searches using `mindist` calculations.
*   The mechanics of Top-K query optimization and how to apply early-termination mathematical bounds (Thresholding) to avoid exhaustive dataset scans.
*   Benchmarking and comparing the execution time (in milliseconds) and memory efficiency of a Rank Join versus a Naive Hash Join.

## How to Run

```bash
# Clone the repository
git clone [https://github.com/YourUsername/Handling_Complex_Data.git](https://github.com/YourUsername/Handling_Complex_Data.git)

# Navigate to the directory
cd Handling_Complex_Data

# Compile all Java files
javac *.java

# --- RUNNING ASSIGNMENT 1 ---

# Run Merge Sort with Aggregation
# Arguments: <input_csv> <group_attribute_index> <function_attribute_index> <aggregation_function>
java MergeSort S.csv 1 2 sum

# Run Merge Join
java MergeJoin

# Run Composite Query
java CompositeQuery

# --- RUNNING ASSIGNMENT 2 ---

# Run STR R-Tree Construction
# Arguments: <input_points_file>
java STR points.txt

# Run Incremental BFNN Search
# Arguments: <tree_file> <query_x> <query_y> <k_neighbors>
java IncrementalBFNN tree.txt 45.0 50.0 3

# --- RUNNING ASSIGNMENT 3 ---

# Run the Optimized Threshold Algorithm
# Argument: <K_number_of_results>
java algorithmA 10

# Run the Baseline Hash Join
# Argument: <K_number_of_results>
java algorithmB 10
