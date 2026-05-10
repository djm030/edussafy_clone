# eduSSAFY design docs

이 폴더는 더 이상 최상위 디자인 기준서가 아니다. 현재 클론 작업의 기준은 루트 문서다.

## 기준 우선순위

1. `../FRAME.md` — 화면 프레임, 라우팅, 클릭/이동 연결, 페이지 skeleton
2. `../DESIGN.md` — 색상, 타이포그래피, spacing, component visual rule
3. `app-vue/` — 위 두 문서를 실제 Vue/Vite 구현 구조로 번역한 구현 가이드
4. `reference-primevue/` — PrimeVue/Vue 라이브러리 참고 자료

상충하는 내용이 있으면 항상 `FRAME.md`와 `DESIGN.md`를 우선한다.

## 폴더 역할

```text
design/
├── app-vue/              # eduSSAFY Vue 구현 가이드
└── reference-primevue/   # PrimeVue/Vue 참고 문서, 구현 기준 아님
```

## 작업 흐름

```text
1. FRAME.md에서 페이지 구조와 이동 경로 확인
2. DESIGN.md에서 시각 규칙 확인
3. design/app-vue에서 Vue 파일/컴포넌트 매핑 확인
4. 필요할 때만 reference-primevue에서 라이브러리 사용법 참고
```

## 주의

- `reference-primevue/`는 SSAFY EDU 클론의 디자인 기준이 아니다.
- PrimeVue 기본 스타일을 그대로 쓰지 말고, `../DESIGN.md`의 SSAFY EDU token과 visual rule을 우선 적용한다.
- 기존 한 페이지 대시보드 문서는 `app-vue/`에서 최신 전체 프레임 기준으로 재정리한다.
## Screenshot-first implementation rule

페이지 구현을 에이전트에게 맡길 때는 `app-vue/05-screenshot-reference-map.md`에서 해당 route의 screenshot을 찾고, 반드시 이미지를 직접 확인하게 한다.

- `FRAME.md`: 구조/route/클릭 이동 기준
- `DESIGN.md`: visual token/component 기준
- `edu_screnshot/`: 실제 배치/밀도/간격/상태 UI 기준
- `app-vue/`: Vue 파일 매핑과 구현 예외만 기록

긴 문서를 매번 전체로 읽지 말고, 구현할 page에 해당하는 섹션과 screenshot만 읽는다.
