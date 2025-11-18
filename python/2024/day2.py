import sys

with open(sys.argv[1], "r") as f:
    reports = [list(map(int, line.split())) for line in f.readlines()]

def safe_reports(nums: list[int]) -> bool:
    diffs = [abs(x1 - x2) for x1, x2 in zip(nums, nums[1:])]
    if not all(1 <= d <= 3 for d in diffs):
        return False
    if all(x1 < x2 for x1, x2 in zip(nums, nums[1:])):
        return True
    if all(x1 > x2 for x1, x2 in zip(nums, nums[1:])):
        return True
    return False

def safe_reports2(nums: list[int]) -> bool:
    if safe_reports(nums):
        return True
    for i in range(len(nums)):
        if safe_reports(nums[:i] + nums [i+1:]):
            return True
    return False;

part1 = len([r for r in reports if safe_reports(r)])
part2 = len([r for r in reports if safe_reports2(r)])

print(f'Part 1: {part1}')
print(f'Part 2: {part2}')
