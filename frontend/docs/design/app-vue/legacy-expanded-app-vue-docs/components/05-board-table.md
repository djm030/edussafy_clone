# BoardTable

## Vue target

```text
components/board/BoardTable.vue
components/board/BoardPostRow.vue
```

## Used by

- 열린 게시판
- 익명 게시판
- 공지사항
- 멘토링 게시판
- 간담회 후기
- 1:1 문의 목록

## Structure

```text
BoardTable
├── TableHeader
└── BoardPostRow[]
```

## Navigation

- row title click -> detail route
- search -> current route query update
- category tab -> current route query update
- pagination -> current route page query update
