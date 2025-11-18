import sys
import re


with open(sys.argv[1], "r") as f:
    line = f.read()

# instructions = re.findall("mul\(([0-9]{1,3}),([0-9]{1,3})\)",line)
# part1 = sum(int(x1) * int(x2) for x1, x2 in instructions)

part1 = 0
part2 = 0
enable = True;
for inst in re.findall(r"mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)",line):
    match inst:
        case "do()":
            enable = True
        case "don't()":
            enable = False
        case _:
            x1, x2 = tuple(map(int, inst[4:-1].split(',')))
            part1 += x1 * x2
            if enable:
                part2 += x1 * x2


print(f'Part 1: {part1}')
print(f'Part 2: {part2}')
