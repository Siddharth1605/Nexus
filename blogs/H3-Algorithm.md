Reference : https://h3geo.org/docs/

## **Problem and Algorithmic Solution:**

Before understanding h3 algo, it is important to understand the existing problem. 

Earth is a sphere. Each points can be marked only by lat and long, these two numbers precisely used to identify any locations on Earth. 

If there are 10k riders and they send their gps every few seconds once. How can we find riders near the restaurant location within 300 mts ? 

Need to go through every rider’s location in db, apply a formula to find distance between two points in sphere and find minimum. This involves square root, cos, sin, etc… and doing this maths for 10k riders will drastically affect the system. 

Instead of running the below brute-query, we are optimising things

SELECT * FROM drivers
WHERE lat BETWEEN x AND y
AND lon BETWEEN x AND y;

The solution is to stop working with continuous lat/long instead split the earth’s sphere as regions. 

So the question will change from who is nearby to who is in the restaurant region or neighbour region. → This is way faster than trigonometry

This is exactly what H3 does. It divides earth into hexagonal cells and gives each cell a 64 bit integer ID. So by this, every GPS co-ordinate maps to exactly one cell. Riders in same cell share same ID. 

**H3 converts geographic co-ordinates into discrete cell IDs so that nearby locations share the same ID or neighbouring IDs.** 

## Why Hexagons

Can divide earth-sphere into squares/triangle/hexagons.

H3 chooses hexagon - not randomly - but it is a mathematical property which directly affects the quality of searching nearest-neighbours. 

Based on side-vertice sharing and corner-edge sharing, 

triangle - 12 neighbours

square - 8 neighbour

hexagon - 6 neighbours

![image.png](attachment:f76414c7-9e4c-4322-b96d-a47bf2789b85:image.png)

In square : 

4 neighbours are direct horizontal/vertical neighbours - going to them from current/middle cell takes radius/diameter distance. 

But going to diagonal (other 4 neighbours) will take additional square root(2) distance. 

In triangle : 

Same , even more unequal distances for going to different neighbours

In hexagon: 

All 6 neighbour cells are exactly at same distance. 

**These things matters a lot. As we are doing a ring search : First we check in our region-ring 1 : all 6 cells are at equidistant from centre cell. Then only we move to next region.** 

**A secondary advantage: hexagons can tile a flat 2D surface with no gaps and no overlaps. The same is true for squares and equilateral triangles, but not for most other shapes (pentagons, octagons, circles — all leave gaps). The three shapes that tile perfectly are squares, triangles, and hexagons. Hexagons have the best equidistance property of the three, making them the natural choice.**

![image.png](attachment:ee5b18c9-ded0-4cf7-a37d-78cc6810f30a:image.png)
