# UI Test Plan

This file records UI test cases for the `test-ui` project skill.

Format: a single JSON code block (```json ... ```). The JSON object has a "tests" array; each test must include:
- id: short identifier
- aim: what the test checks
- command: command to run (shell command)
- input: string to send to stdin (optional, empty string means no input)
- expected: expected stdout (use "\n" for newlines)

Example test cases:

```json
{
  "tests": [
    {
      "id": "tc-echo-hello",
      "aim": "Verify echo prints Hello",
      "command": "cmd /c echo Hello",
      "input": "",
      "expected": "Hello\n"
    }
  ]
}
```

Add or edit tests as needed. Save this file and run the skill to execute the tests.
