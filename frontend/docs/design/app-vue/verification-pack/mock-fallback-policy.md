# Mock fallback policy

Generated policy date: 2026-05-12.

## Final rule

Mock data is allowed only for local demo mode when `VITE_USE_API` is not `true`. API-mode completion evidence must use the backend runtime and must report `usedMockFallback: false`.

## Runtime modes

| Mode | Trigger | Allowed fallback behavior | Completion claim |
| --- | --- | --- | --- |
| Demo mode | `VITE_USE_API` unset or not `true` | Local static data may render pages so screenshots can be inspected without backend services. | Demo-only; not API-complete. |
| API mode | `VITE_USE_API=true` | No silent mock replacement after a real API error. Pages may show empty/error UI, but evidence must disclose blockers. | API-complete only with backend evidence and `usedMockFallback: false`. |
| Visual auth capture | `VITE_USE_API=true` plus visual-token query injected by the capture script | Browser localStorage is seeded with demo login tokens only for local authenticated screenshots. Tokens must be redacted from evidence. | Visual evidence only; pixel parity remains separate. |

## Evidence requirements

- `run-api-runtime-check.mjs` is the source of truth for API-mode runtime evidence.
- `capture-visual-runtime.mjs` may use visual auth injection, but screenshots are still dimension/manual-review evidence unless a pixel-diff verifier is added.
- `NO_SCREENSHOT` routes use `no-screenshot-criteria.md` and cannot be promoted to pixel-perfect without a new reference screenshot.
- Final reports must state whether API evidence used mock fallback. The expected value for API-mode completion is `false`.
