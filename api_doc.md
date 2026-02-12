# Huihui Server API

## Base
- Base URL: `http://<host>:<port>`
- Content-Type: `application/json`

## Endpoints

### GET /
Request
- Method: `GET`
- Path: `/`
- Body: none

Response
- Status: `200`
- Body: `"hello"`

---

### POST /auth/login
Request
- Method: `POST`
- Path: `/auth/login`
- Body:
```json
{
  "username": "string",
  "password": "string"
}
```

Response (success)
- Status: `200`
- Body:
```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "token": "<jwt>"
  }
}
```

Response (error)
- Status: `200`
- Body:
```json
{
  "code": 500,
  "message": "SqlError(...) or JwtError(...)"
}
```

---

### POST /auth/register
Request
- Method: `POST`
- Path: `/auth/register`
- Body:
```json
{
  "email": "user@example.com",
  "username": "string",
  "password": "string"
}
```

Response (success)
- Status: `200`
- Body:
```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "token": "<jwt>"
  }
}
```

Response (error)
- Status: `200`
- Body:
```json
{
  "code": 500,
  "message": "SqlError(...) or JwtError(...)"
}
```

---

### GET /auth/me
Request
- Method: `GET`
- Path: `/auth/me`
- Headers:
- `Authorization: Bearer <jwt>`
- Body: none

Response (success)
- Status: `200`
- Body:
```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "name": "username"
  }
}
```

Response (error)
- Status: `200`
- Body:
```json
{
  "code": 500,
  "message": "SqlError(...) or JwtError(...)"
}
```

---

### GET /food/recommendation
Request
- Method: `GET`
- Path: `/food/recommendation`
- Headers:
- `Authorization: Bearer <jwt>`
- Params: none
- Body: none

Response (success)
- Status: `200`
- Body:
```json
{
  "code": 200,
  "message": "ok",
  "data": [
    {
      "id": 10,
      "restaurant_id": 2,
      "name": "Spicy Chicken",
      "description": "...",
      "image": "https://..."
    }
  ]
}
```

Response (error)
- Status: `200`
- Body:
```json
{
  "code": 500,
  "message": "SqlError(...) or JwtError(...)"
}
```

Notes
- No pagination.
- Repeats are allowed in MVP.
- Client calls this endpoint again when cards are exhausted.

---

### POST /food/recommendation/reaction
Request
- Method: `POST`
- Path: `/food/recommendation/reaction`
- Headers:
- `Authorization: Bearer <jwt>`
- Body:
```json
{
  "food_id": 10,
  "reaction": "like",
  "source": "food_tab",
  "occurred_at": 1739330100
}
```

`reaction` enum
- `like`
- `skip`
- `dislike`

Response (success)
- Status: `200`
- Body:
```json
{
  "code": 200,
  "message": "ok",
  "data": 123
}
```

Response (error)
- Status: `200`
- Body:
```json
{
  "code": 500,
  "message": "SqlError(...) or JwtError(...)"
}
```

---

### POST /food/consecutiveSuggest
Request
- Method: `POST`
- Path: `/food/consecutiveSuggest`
- Headers:
- `Authorization: Bearer <jwt>`
- Body:
```json
{
  "food_ids": [1, 2, 3, 4],
  "selected_food_ids": [1, 2]
}
```

Response (success)
- Status: `200`
- Body:
```json
{
  "code": 200,
  "message": "ok",
  "data": [
    {
      "id": 10,
      "restaurant_id": 2,
      "name": "Spicy Chicken",
      "description": "...",
      "image": "https://..."
    }
  ]
}
```

Response (error)
- Status: `200`
- Body:
```json
{
  "code": 500,
  "message": "SqlError(...) or JwtError(...)"
}
```

## Notes
- The API wraps responses in `ApiResponse`.
- Errors use `code=500` in JSON while HTTP status remains `200`.
- JWTs use `JWT_SECRET` (fallback: `dev-secret-change-me`) and expire in 24 hours.
