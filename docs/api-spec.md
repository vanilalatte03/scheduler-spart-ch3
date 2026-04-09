## 일정 생성

### 기능

일정을 생성한다.

### Method / URL

```
POST /schedules
```

### Request Body

```
{
  "title":"회의 준비",
  "content":"프로젝트 회의 자료 정리",
  "writer":"지호",
  "password":"1234"
}
```

### Response Body

```
{
  "id":1,
  "title":"회의 준비",
  "content":"프로젝트 회의 자료 정리",
  "writer":"지호",
  "createdAt":"2026-04-08T13:00:00",
  "modifiedAt":"2026-04-08T13:00:00"
}
```

### 상태 코드

`201 Created`

---

## 전체 일정 조회

### 기능

전체 일정을 조회한다.

작성자명은 선택 조건이다.

### Method / URL

```
GET /schedules
GET /schedules?writer=지호
```

### Query Parameter

`writer` : 선택값
있으면 해당 작성자의 일정만 조회
없으면 전체 조회

### Response Body 예시

```
[
  {
    "id":3,
    "title":"운동",
    "content":"하체 운동",
    "writer":"지호",
    "createdAt":"2026-04-08T10:00:00",
    "modifiedAt":"2026-04-08T12:30:00"
  },
  {
    "id":1,
    "title":"회의 준비",
    "content":"프로젝트 회의 자료 정리",
    "writer":"지호",
    "createdAt":"2026-04-08T09:00:00",
    "modifiedAt":"2026-04-08T09:00:00"
  }
]
```

### 상태 코드

`200 OK`

---

## 선택 일정 조회

### 기능

ID로 단건 일정 조회

### Method / URL

```
GET /schedules/{id}
```

### Path Variable

`id` : 일정 ID

### Response Body

```
{
  "id":1,
  "title":"회의 준비",
  "content":"프로젝트 회의 자료 정리",
  "writer":"지호",
  "createdAt":"2026-04-08T09:00:00",
  "modifiedAt":"2026-04-08T09:00:00"
}
```

### 상태 코드

`200 OK`

### 예외

- 해당 ID가 없으면 `404 Not Found`

---

## 일정 수정

### 기능

선택한 일정의 `제목`, `작성자명`만 수정한다.

수정 요청 시 비밀번호를 함께 받는다.

### Method / URL

```
PUT /schedules/{id}
```

### 수정 가능 필드

- `title`
- `writer`

### 수정 불가 필드

- `content`
- `createdAt`

### Request Body

```
{
  "title":"회의 자료 최종 점검",
  "writer":"지호",
  "password":"1234"
}
```

### Response Body

```
{
  "id":1,
  "title":"회의 자료 최종 점검",
  "writer":"지호",
  "modifiedAt":"2026-04-08T14:10:00"
}
```

### 상태 코드

`200 OK`

### 예외

- 일정 없음 → `404 Not Found`
- 비밀번호 불일치 → `403 Forbidden`

---

## 일정 삭제

### 기능

선택한 일정을 삭제한다.

삭제 요청 시 비밀번호를 함께 받는다.

### Method / URL

```
DELETE /schedules/{id}
```

### Request Body

```
{
  "password":"1234"
}
```

### Response

```
없음
```

### 상태 코드

`204 No Content`

### 예외

- 일정 없음 → `404 Not Found`
- 비밀번호 불일치 → `403 Forbidden`