# automated-snake-game
Control a snake to move and collect as many apples as possible in a map within finite steps (minimized path cost) by implementing A* pathfinding algorithm.

### Date created
May 2022

### Project Title
Automated Snake Game Using A* Algorithm

### Pseudocode
`OPEN LIST , CLOSED LIST
Initialize game
Add the start node to OPEN LIST 
Loop
Start Moving
f(n) = g(n) + h(n)
current = node in OPEN LIST with lowest f(n)
remove current from OPEN LIST
add current to CLOSED LIST
if (Meet Apple?)
{
Increase snake's length
} 
else 
{
For each neighbor of the current node
set f(n) of neighbor
set parent of neighbor to current
if neighbor is not in OPEN LIST
add neighbor to OPEN LIST
}
if (Game over?) 
{
Initilize game 
} 
else 
{
keep moving
}
Exit `
