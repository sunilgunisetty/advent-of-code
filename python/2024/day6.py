import sys

with open(sys.argv[1], "r") as f:
    grid = list(map(str.strip, f.readlines()))


num_rows = len(grid)
num_cols = len(grid[0])

def getStart():
    for r, row in enumerate(grid):
        for c, val in enumerate(row):
            if val == "^":
                return (r, c)

r, c = getStart()
dr, dc = -1, 0

visited = set()

while True:
    visited.add((r,c))
    if not (0 <= r + dr < num_rows and 0 <= c + dc < num_cols):
        break
    if grid[r + dr][c + dc] == "#":
        dc, dr = -dr , dc
    else:
        r += dr
        c += dc

print(len(visited))
