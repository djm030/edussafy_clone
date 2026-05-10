# FormTable

## Vue target

```text
components/form/FormTable.vue
components/form/FormRow.vue
```

## Used by

- 게시글 작성
- 1:1 문의 작성
- 서류제출 작성
- 간담회 후기 작성

## Structure

```text
FormTable
└── FormRow[]
    ├── LabelCell
    └── ControlCell
```

## Actions

- 취소/목록 -> list route
- 등록/저장 -> submit mutation 후 list 또는 detail route
- 첨부파일 -> upload component, route 이동 없음
