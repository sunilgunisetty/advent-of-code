import sys

with open(sys.argv[1], "r") as f:
    lines = list(map(str.strip, f.readlines()))

def check_valid(target, nums):
    if len(nums) == 1:
        return target == nums[0]

    num = nums.pop()

    if target / num == target // num:
        if check_valid(target // num, nums[:]):
            return True

    if target - num >= 0:
        if check_valid(target-num, nums[:]):
            return True

    return False

part1 = 0
for line in lines:
    target = int(line.split(': ')[0])
    nums = list(map(int, line.split(': ')[1].split(' ')))
    if check_valid(target, nums):
        part1 += target


print(part1)
