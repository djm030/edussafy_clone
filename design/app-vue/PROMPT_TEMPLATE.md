# Page implementation prompt template

에이전트에게 페이지 단위 구현을 맡길 때 아래 템플릿을 사용한다.

```text
작업: [route] 페이지를 Vue/Vite로 구현해줘.

프로젝트:
- /Users/baeggwan-yeol/Desktop/eduSSAFY

반드시 먼저 확인할 것:
1. design/app-vue/README.md
2. design/app-vue/05-screenshot-reference-map.md의 [route] 항목
3. 해당 항목의 primary/secondary screenshot 이미지 파일
4. FRAME.md의 해당 page/route/click 이동 섹션
5. DESIGN.md의 필요한 visual token/component 섹션

중요:
- screenshot 파일명만 보고 추측하지 말고 실제 이미지를 열어 확인해라.
- FRAME.md/DESIGN.md 내용을 app-vue 문서에 다시 복사하지 마라.
- Vue 구현에서 필요한 파일 매핑/예외만 app-vue 문서 기준으로 참고해라.
- PrimeVue reference는 필요할 때만 참고하고, SSAFY visual 기준은 screenshot/DESIGN.md를 우선해라.

구현 범위:
- page component: [예: src/pages/classroom/WeeklyCurriculumPage.vue]
- shared components: [필요한 컴포넌트]
- mock data: [필요한 src/data 파일]
- style: token CSS를 사용하고, hex 값을 component에 직접 쓰지 않는다.

검증:
1. npm run build 또는 현재 프로젝트의 검증 명령 실행
2. 브라우저에서 같은 viewport로 캡처
3. 기준 screenshot과 header, hero, card/list density, spacing, state UI를 비교
4. 큰 차이가 있으면 수정
```

## 빠른 예시

```text
작업: /classroom/curriculum 페이지를 Vue/Vite로 구현해줘.

반드시 먼저 확인할 것:
1. design/app-vue/05-screenshot-reference-map.md의 /classroom/curriculum 항목
2. edu_screnshot/02_classroom_weekly_curriculum.png
3. edu_screnshot/12_classroom_curriculum_week_toggle_attempt.png
4. FRAME.md의 classroom curriculum/page skeleton/click 이동 섹션
5. DESIGN.md의 nav, hero, card, badge, button, spacing token 섹션

구현 후 screenshot과 비교해서 dark navy section tabs, hero height, phase stepper, week carousel, timeline card spacing을 맞춰줘.
```
