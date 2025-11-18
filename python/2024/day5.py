import sys

with open(sys.argv[1], "r") as f:
    data = f.read()

rules, jobs = data.split("\n\n")

rules = [tuple(map(int,rule.split("|"))) for rule in rules.splitlines()]
jobs = [tuple(map(int, job.split(","))) for job in jobs.splitlines()]

print(rules)
print("---")
print(jobs)
