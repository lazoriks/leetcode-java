# Copilot / AI Agent Instructions

Purpose: short, actionable guidance so an AI coding agent can be immediately productive with this repository.

## Big picture
- This repository is a personal collection of standalone Java solutions to LeetCode problems. Each solution is implemented as a single Java source file and organized per problem under `solutions/<NNN>. Problem Title/`.
- There is no build system (Maven/Gradle) or test harness committed — files are plain Java source files intended to be run on LeetCode or locally via `javac`/an IDE.

## Repo layout (important files)
- `solutions/` — main content: folders like `1. Two Sum/`, `121. Best Time to Buy and Sell Stock/`.
- `README.md` — canonical index and human-facing table of problems, links, and topics. Keep this in sync when adding solutions.
- `.github/PULL_REQUEST_TEMPLATE.md` — currently empty; PRs should be small and focused.

## Conventions and patterns
- Folder naming: use the pattern `"<number>. <Problem Title>/"` (space after dot). Keep the title aligned with LeetCode problem title where possible.
- File naming: prefer descriptive filenames with an approach suffix, e.g. `TwoSum_HashTable.java`, `JumpGame_Greedy.java`, or `Permutations_Backtracking_Recursion.java` if multiple approaches exist.
- Class & method structure:
  - Typically one public class per file; the class name matches the filename (without `.java`).
  - Solution methods follow LeetCode method signatures (e.g., `public int[] twoSum(int[] nums, int target)`). Keep those signatures intact to make copying to LeetCode trivial.
- Comments and documentation:
  - Many files include a short "Approach" section plus `Time Complexity` / `Space Complexity` header comments near the top. Maintain this style for readability.
  - Keep explanations concise and include any tricky reasoning or edge cases.
- Packages: most solution files are in the default package (no `package` declaration). Avoid adding a package declaration to existing solution files unless you update the entire repository consistently (this breaks direct compilation/LeetCode copy-paste).

## Build, run, and verification
- There is no project-level build file. Typical local workflow:
  - Compile a single file: `javac solutions/1.\ Two\ Sum/TwoSum_HashTable.java`
  - If you add a `main` method in the file, run it with: `java TwoSum_HashTable` (or use IDE run configurations).
  - For multi-file compilation, compile all needed files or create a small test driver under `leetcode/`.
- If adding unit tests, prefer lightweight JUnit test classes in the same directory as the problem (not required by the current repo, but acceptable). Mention test running steps in README if you add them.

## How to add a new solution (step-by-step)
1. Create a folder `solutions/<NNN>. <Problem Title>/`.
2. Add source file named following existing patterns, e.g., `ProblemTitle_Approach.java` and ensure the public class name equals the filename.
3. Add top comments: short Approach, Time / Space complexity.
4. Add minimal example inputs as a `main` or a short JUnit test (optional but helpful).
5. Update `README.md` — add a table row under the appropriate topic section and add a link to the file exactly as used elsewhere.
6. Open a small PR with title like: `Add <NNN>. <Problem Title> — <Approach>` and include the LeetCode problem link and short reasoning in the PR body.

## Examples (from repo)
- `solutions/1. Two Sum/TwoSum_HashTable.java` — shows the naming pattern and the in-file `Time Complexity` / `Space Complexity` comments and LeetCode-friendly method signature.
- `solutions/46. Permutations/Permutations_Backtracking_Recursion.java` — example of multiple approaches separated into different files.

## What NOT to change / gotchas
- Avoid introducing a package declaration unless you also update build/IDE configuration; most files are intended to be standalone and copy-pasted to LeetCode.
- Don't alter the top-level README formatting (the table is used as the canonical index and has a consistent layout).

---
If you want, I can: (a) create a small PR template that reminds contributors to update `README.md`, or (b) add a simple `CONTRIBUTING.md` with these steps — tell me which you'd like next.