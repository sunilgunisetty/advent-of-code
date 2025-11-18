import sys

with open(sys.argv[1], "r") as f:
    data = f.read().strip().split("\n\n")


largest = [0,0,0]

for elf in data:
    total = sum(int(x) for x in elf.split("\n") if x)
    largest = sorted(largest + [total], reverse=True)

print(f"part1: {largest[0]}")
print(f"part2: {sum(largest[:3])}")
