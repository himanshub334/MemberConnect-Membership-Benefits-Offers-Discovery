# API-first development notes

A useful team workflow for this demo:
1. Agree on resource names, payloads, error codes, and customer identifier semantics.
2. Update `openapi/openapi.yaml` before implementation and validate it in review.
3. Build a mock consumer against the spec to surface integration issues early.
4. Implement service and BFF routes, then add JUnit/contract tests.
5. Review pull requests for correctness, edge cases, observability, accessibility, and test coverage.
6. Maintain a prioritised backlog and deliver in small increments.

This repository includes a starter OpenAPI document and basic automated checks; it does not assert that peer reviews or three sprints occurred.
