# EaterGame

A simple android maze game with the 15x15 maze shown in the following illustration:
```
############### 
#.............# 
#.##.##.##.##.# 
#.#.........#.# 
#...#.#.#.#...# 
#.###.#.#.###.# 
#.....#.#.....# 
####.##.##.#### 
#.....#.#.....# 
#.###.#.#.###.# 
#...#.#.#.#...# 
#.#.........#.# 
#.##.##.##.##.# 
#............@# 
###############

#=wall section; .=food; @=Eater
```
The playable character (the Eater) moves through the maze eating the dots. The Eater cannot move through the walls. Initially the whole maze is full of dots; when the Eater eats all of them the level is complete and the game shows your time and the best time achieved in the current session. You then tap on the screen to play the same maze again.

Achieve: Extending the android.view.View class and overriding its onDraw method.
