import sys

with open(sys.argv[1], "r") as f:
    lines = [list(map(int,line.split())) for line in f.readlines()]

list1, list2 = list(map(list,(zip(*lines))))

part1 = sum(abs(x - y) for x, y in list(zip(sorted(list1), sorted(list2))))

frequencies = {}

for element in list2:
    if frequencies.get(element) == None:
        frequencies[element] = 1
    else:
        frequencies[element] = frequencies.get(element) + 1;


part2 = sum(e * frequencies[e] for e in list1 if frequencies.get(e) != None)


print(f'Part1 : {part1}')
print(f'Part2 : {part2}')
