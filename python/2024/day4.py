import sys
from collections import defaultdict

with open(sys.argv[1], "r") as f:
    lines = [ line.strip() for line in f.readlines()]


grid = dict()
co_ordinate_X = set()

for r, row in enumerate(lines):
    for c, val in enumerate(row):
        grid[(r, c)] = val
        if val == "X":
            co_ordinate_X.add((r, c))

up         = [[-1, 0], [-2, 0], [-3, 0]]
down       = [[1, 0], [2, 0], [3, 0]]
right      = [[0, 1], [0, 2], [0, 3]]
left       = [[-1, 0],[-2, 0],[-3, 0]]
up_right   = [[-1, 1], [-2, 2], [-3, 3]]
up_left    = [[-1, -1], [-2, -2], [-3, -3]]
down_right = [[1, 1], [2, 2], [3, 3]]
down_left  = [[1, -1], [2, -2], [3, -3]]

directions = [up, down, left, right, up_right, up_left, down_right, down_left]

# print(grid)
print(len(co_ordinate_X))

def check(x, y, grid, dirs):
    s = "".join(grid.get((x+dx, y+dy), ".") for dx, dy in dirs)
    return s == "MAS"

xmas_count = 0
for x, y in co_ordinate_X:
    for direction in directions:
        xx = "".join(grid.get((x+dx, y+dy), ".") for dx, dy in direction)
        print(xx)
        if "MAS" in xx:
            xmas_count += 1


# part1 = xmas_count
# print(f"part1: {part1}")

char_map = defaultdict(set)
for r, row in enumerate(lines):
    for c, val in enumerate(row):
        char_map[val].add((r, c))

part1 = 0
for r, c in char_map["X"]:
    for dr, dc in [
        (-1, -1),
        (-1, 0),
        (-1, 1),
        (0, -1),
        (0, 1),
        (1, -1),
        (1, 0),
        (1, 1),
    ]:
        for i, char in enumerate("MAS", 1):
            if (r + (dr * i), c + (dc * i)) not in char_map[char]:
                break
        else:
            part1 += 1
print(f"Part 1: {part1}")
