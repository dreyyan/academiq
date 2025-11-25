# Project: Movie Recommendation System using KNN and KD-Tree

## 1. Project Overview
- **Objective**: Build a recommendation system that suggests movies to users based on their past ratings and the ratings of similar users.
- **Input**: User ID
- **Output**: Top N recommended movies (not rated yet by the user)
- **Dataset**: MovieLens 100K or 1M dataset (users × movies × ratings)
- **Algorithm**: K-Nearest Neighbors (KNN) search on user vectors, optimized with KD-Tree

## 2. Key Components & Data Structures

| Component          | Data Structure                              | Purpose                                                                 |
|--------------------|---------------------------------------------|-------------------------------------------------------------------------|
| User ratings       | 2D array or dictionary of dictionaries     | Store user × movie ratings                                              |
| User vectors       | 1D array per user                           | Represent a user’s ratings as a vector in high-dimensional space       |
| KD-Tree            | Tree nodes with arrays                      | Fast nearest neighbor search in high-dimensional rating space          |
| Priority queue / max-heap | Heap                                   | Keep top K nearest neighbors efficiently during traversal               |
| Cache              | Hash map / dictionary                       | Store computed similarities to avoid recomputation (optional memoization) |
