# Design

## Source of truth
- Status: Active
- Last refreshed: 2026-05-12
- Primary product surfaces:
  - Authenticated Edu SSAFY portal clone: dashboard, My Campus, Classroom, Community, Help Desk, Mentoring, notifications, auth, and error pages.
  - Frontend implementation: `frontend/src/router/index.js`, `frontend/src/pages/**/*Page.vue`, `frontend/src/components/layout/*.vue`, `frontend/src/components/ui/*.vue`, and `frontend/src/styles/*.css`.
- Evidence reviewed:
  - `frontend/docs/DESIGN.md` — detailed visual tokens, typography, component rules, and do/don't guidance.
  - `frontend/docs/FRAME.md` — app shell, route/page skeletons, navigation, click-through behavior, and implementation priority.
  - `frontend/docs/design/README.md` — design-doc hierarchy and screenshot-first instruction.
  - `frontend/docs/design/app-vue/README.md` — Vue/Vite adapter rules and reading strategy.
  - `frontend/docs/design/app-vue/canonical-contract.md` — route, screenshot, API, and verification evidence matrix.
  - `frontend/docs/design/app-vue/05-screenshot-reference-map.md` — page-to-screenshot mapping.
  - `frontend/docs/edu_screnshot/*.png` — captured reference screens for density, spacing, layout, and state UI.
  - `frontend/src/styles/tokens.css` and related CSS files — implemented token and component styling surface.
  - `frontend/src/router/index.js` — route authority and auth-layout branching.

This root file is the repo-level design contract. Keep detailed token values and page-by-page skeletons in the existing frontend docs, but cite this file first for design governance and decision order.

## Brand
- Personality:
  - Institutional, calm, practical, and learning-focused.
  - Corporate LMS rather than marketing landing page.
  - Dense Korean education portal with clear hierarchy and reliable status feedback.
- Trust signals:
  - SSAFY-like blue/navy palette, stable table/list layouts, predictable navigation, and clear account/session state.
  - Conservative spacing, readable Korean typography, and explicit empty/error states.
- Avoid:
  - Generic AI-dashboard gradients, playful emoji badges, excessive shadows, oversized cards, and SaaS-marketing visual drama.
  - PrimeVue default aesthetics when they conflict with the captured SSAFY EDU screens.
  - Adding new design-system layers when existing CSS tokens/components can be extended.

## Product goals
- Goals:
  - Clone the authenticated SSAFY EDU portal experience closely enough that page structure, density, navigation, and core UI states match the captured references.
  - Preserve route and screenshot traceability for every implemented page.
  - Support both demo/mock mode and API-backed mode without changing the visible design contract.
- Non-goals:
  - Rebrand SSAFY EDU or create a new visual identity.
  - Replace the existing Vue/CSS implementation with another UI framework.
  - Treat reference PrimeVue documents as clone-design authority.
- Success signals:
  - Route/page/component behavior follows `frontend/docs/FRAME.md` and `frontend/docs/design/app-vue/canonical-contract.md`.
  - Visual density and state UI are checked against `frontend/docs/edu_screnshot/*.png` before implementation claims.
  - `npm run build` and route/screenshot verification artifacts remain reproducible where relevant.

## Personas and jobs
- Primary personas:
  - SSAFY trainee checking classes, curriculum, quests, attendance, learning resources, notices, and personal campus status.
  - Administrator or reviewer validating clone fidelity and API/frontend contract coverage.
  - Developer agent implementing or reviewing UI pages from route, screenshot, and token evidence.
- User jobs:
  - Navigate quickly between My Campus, Classroom, Community, Help Desk, and Mentoring sections.
  - Read dense lists/tables, submit forms, inspect details, and understand empty or restricted states.
  - Confirm attendance, points, curriculum progress, quests, documents, pledges, and notifications.
- Key contexts of use:
  - Desktop-first education portal usage.
  - Korean content with compact information density.
  - Authenticated-session flows with occasional login/session-required redirects.

## Information architecture
- Primary navigation:
  - My Campus
  - Classroom
  - Community
  - Help Desk
  - Mentoring
  - Notifications and profile utilities in the global header.
- Core routes/screens:
  - Dashboard: `/dashboard`
  - My Campus: level/points, attendance, elearning, bookmarks, documents, pledges, education status, profile, password
  - Classroom: replay lists, curriculum, quests, required learning, resources, detail pages
  - Community: surveys, open board, anonymous board, class roster, write/detail flows
  - Help Desk: notices, FAQ, inquiries, academic rules
  - Mentoring: stories, Q&A, notices, meetup apply/info/reviews
  - Auth/errors: login, 403, 404
- Content hierarchy:
  - Global header and service shortcuts first.
  - Optional hero and dark section tabs for section pages.
  - Main container uses list/table/form/detail patterns.
  - Footer and floating quick action are persistent authenticated-page utilities.

## Design principles
- Principle 1: Screenshot-first fidelity.
  - When a route has a screenshot in `frontend/docs/design/app-vue/05-screenshot-reference-map.md`, inspect that image before changing layout, density, or component proportions.
- Principle 2: Contract-first implementation.
  - Routes, screenshots, APIs, and board codes must stay aligned with `frontend/docs/design/app-vue/canonical-contract.md`.
- Principle 3: Reuse existing primitives.
  - Prefer `components/layout`, `components/ui`, `styles/tokens.css`, and existing page CSS before introducing new abstractions.
- Tradeoffs:
  - Favor clone fidelity and predictable portal density over visual novelty.
  - Favor explicit route/screenshot evidence over broad aesthetic preference.
  - Preserve compact Korean UI density, but not at the cost of readability or focus accessibility.

## Visual language
- Color:
  - Use the blue/navy/white/gray institutional palette defined in `frontend/docs/DESIGN.md` and implemented in `frontend/src/styles/tokens.css`.
  - Primary blue and navy are for navigation, active states, CTA emphasis, and portal identity.
  - Semantic danger/success/status colors should match existing token usage and remain accessible.
- Typography:
  - Korean-first sans stack: Pretendard, Noto Sans KR, Apple SD Gothic Neo, Inter/system fallback.
  - Body copy should generally stay at 14px or larger; smaller metadata/microcopy must remain legible.
- Spacing/layout rhythm:
  - Use centered content containers around the existing 1180px width convention.
  - Preserve dense list/table rhythm from reference screenshots; avoid oversized marketing-card spacing.
- Shape/radius/elevation:
  - Radius is restrained, usually 4px to 6px, with pill only for badges/circular controls.
  - Shadows are minimal and purposeful; do not add depth to every card or icon.
- Motion:
  - Keep motion minimal and functional. Prefer simple hover/focus feedback over decorative animation.
- Imagery/iconography:
  - Use screenshots as visual density references, not as decorative assets.
  - Icons should clarify status/action, not become generic emoji-style decoration.

## Components
- Existing components to reuse:
  - Layout: `AppShell`, `AppHeader`, `AppFooter`, `FloatingQuickAction`.
  - UI primitives: `BoardTable`, `DashboardCard`, `EmptyState`, `FormTable`, `PageHero`, `PaginationBar`, `SearchFilterBar`, `SectionTabs`, `StatusBadge`.
  - Data/service helpers: route constants, board codes, enum constants, and existing service modules.
- New/changed components:
  - Add only when a repeated UI pattern cannot be expressed with existing primitives.
  - Keep new components small, route-evidence-backed, and paired with the screenshot or FRAME section that justifies them.
- Variants and states:
  - Lists/tables: loading, empty, filtered-empty, error, paginated, selected/active, detail navigation.
  - Forms: default, validation error, disabled/submitting, success/return-to-list.
  - Auth/session: login, session-required redirect, token refresh failure redirect.
  - Board/detail pages: open, anonymous, notice, Q&A, review, FAQ/rule variants.
- Token/component ownership:
  - Visual tokens live in `frontend/docs/DESIGN.md` and `frontend/src/styles/tokens.css`.
  - Page structure lives in `frontend/docs/FRAME.md`.
  - Vue-specific mapping and exceptions live in `frontend/docs/design/app-vue/*`.

## Accessibility
- Target standard:
  - Aim for WCAG 2.1 AA-level contrast, focus visibility, and keyboard usability for portal flows.
- Keyboard/focus behavior:
  - Navigation, tabs, buttons, board actions, pagination, forms, and auth flows must be keyboard reachable.
  - Focus states should be visible and consistent with the existing blue/navy token system.
- Contrast/readability:
  - Use accessible variants for status, danger, success, and primary actions when text is placed on color.
  - Korean body text should not be reduced below readable sizes to force more density.
- Screen-reader semantics:
  - Preserve semantic headings, form labels, table/list structure, and button/link roles.
  - Empty/error states should explain the state and next action where possible.
- Reduced motion and sensory considerations:
  - Avoid nonessential animation. Any future motion should be suppressible or subtle enough not to distract.

## Responsive behavior
- Supported breakpoints/devices:
  - Desktop-first, with responsive support through `frontend/src/styles/responsive.css`.
  - Tablet/narrow desktop should preserve navigation clarity and table readability.
- Layout adaptations:
  - Multi-column dashboard/page grids may collapse to fewer columns.
  - Tables should use existing responsive conventions rather than inventing route-local behavior.
  - Global header and section tabs must stay navigable at reduced widths.
- Touch/hover differences:
  - Hover affordances should not be the only indication of interactivity.
  - Touch targets must remain practical for dense Korean portal screens.

## Interaction states
- Loading:
  - Prefer stable layout skeletons or conservative loading indicators that do not alter page density unexpectedly.
- Empty:
  - Use `EmptyState` and screenshot-specific empty layouts for required learning, inquiries, bookmarks, documents, elearning, and meetup info.
- Error:
  - Use explicit error pages or inline messages. Do not silently hide API or route failures.
- Success:
  - Use restrained confirmation copy/actions; return-to-list and detail navigation should follow FRAME route rules.
- Disabled:
  - Disabled actions must be visually distinct and remain understandable from copy/context.
- Offline/slow network, if applicable:
  - Treat as an explicit loading/error state; do not replace failed API-mode evidence with unlabelled mock content.

## Content voice
- Tone:
  - Direct, formal, and education-portal appropriate.
  - Korean UI copy should stay concise and familiar to SSAFY EDU conventions.
- Terminology:
  - Preserve domain labels such as My Campus, Classroom, Quest/평가, HELP DESK, Mentoring, 출석, 장학포인트, 학습자료, 공지사항, and 1:1 문의.
- Microcopy rules:
  - Avoid filler explanations and generic marketing copy.
  - Empty/error copy should say what happened and what the user can do next.
  - Button labels should match the target action or route.

## Implementation constraints
- Framework/styling system:
  - Vue 3 + Vite frontend.
  - CSS is organized under `frontend/src/styles/*.css`; use existing CSS variables and component classes first.
  - Do not add new dependencies without an explicit request.
- Design-token constraints:
  - `frontend/src/styles/tokens.css` is the implemented token layer.
  - `frontend/docs/DESIGN.md` remains the detailed token reference; update both docs and CSS intentionally when token meaning changes.
- Performance constraints:
  - Keep pages lightweight and avoid unnecessary runtime visual libraries.
  - Preserve Vite build health after frontend changes.
- Compatibility constraints:
  - Demo/mock mode and API mode must not diverge visually except for real data and explicit failure states.
  - Dynamic routes need representative route-smoke evidence before completion claims.
- Test/screenshot expectations:
  - For UI changes, run `npm run build` at minimum.
  - For visual fidelity work, follow `frontend/docs/design/app-vue/05-screenshot-reference-map.md` and capture comparable runtime evidence.
  - For contract-sensitive routes, use verification artifacts under `frontend/docs/design/app-vue/verification-pack/` where applicable.

## Open questions
- [ ] Confirm whether this root `DESIGN.md` should replace or only govern `frontend/docs/DESIGN.md` in future prompts / owner: project maintainer / impact: documentation hierarchy and agent routing.
- [ ] Define exact browser/device support targets beyond desktop-first responsive behavior / owner: project maintainer / impact: responsive QA scope.
- [ ] Decide whether `omx_wiki/` should mirror this design summary for searchable lifecycle knowledge / owner: project maintainer / impact: long-term knowledge retrieval.
