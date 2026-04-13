# 🎮 HobbyCatalog API

REST API для интернет-магазина хобби и настольных игр. Проект предоставляет полный функционал для управления каталогом товаров, корзиной покупок, кошельками пользователей и системой платежей с JWT аутентификацией.

## 📋 Оглавление
- [О проекте](#о-проекте)
- [Основные возможности](#основные-возможности)
- [Технологии](#технологии)
- [Запуск проекта](#запуск-проекта)
- [API Эндпоинты](#api-эндпоинты)

---

## О проекте
HobbyCatalog - бэкенд для магазина хобби и настольных игр.

### Бизнес-логика
1. Регистрация → получение JWT
2. Управление кошельками/картами
3. Пополнение баланса
4. Работа с корзиной
5. Оформление покупки

### Роли
- **USER**: покупки, корзина, кошельки
- **ADMIN**: управление каталогом

---

## Основные возможности
🔐 JWT аутентификация | 👤 Профиль пользователя | 💳 Кошельки | 💰 Баланс | 🛒 Корзина | 🎲 Каталог с фильтрами | 📊 Типы хобби | 🛍 Покупки | 🔄 Refresh токена

---

## Технологии
Java 17 | Spring Boot 3.2 | Spring Security | Spring Data JPA | PostgreSQL 14+ | JWT 0.11.5 | MapStruct 1.5.5 | Lombok | Flyway

---

## API Эндпоинты

### 🔐 Аутентификация (`/users/info`)
| Метод | Путь | Описание | Тело / Параметры | Ответ |
|:---|:---|:---|:---|:---|
| `POST` | `/regist` | Регистрация нового пользователя | `{username, email, password}` | `String` (успех) |
| `POST` | `/login` | Авторизация | `{email, password}` | `{accessToken, refreshToken, expiresIn}` |
| `POST` | `/refresh` | Обновление токена | `{refreshToken}` | Новые токены доступа |

### 👤 Пользователи (`/users/adress`) 🔒
| Метод | Путь | Описание | Тело / Параметры | Ответ |
|:---|:---|:---|:---|:---|
| `GET` | `/` | Список адресов текущего пользователя | — | `List<UserAdress>` |
| `GET` | `/{id}` | Адрес по ID | Path: `id` | `UserAdress` |
| `POST` | `/` | Добавить новый адрес | Body: `{city, street, building, apartment, postalCode}` | Созданный адрес |
| `PUT` | `/{id}` | Обновить адрес | Path: `id`, Body: изменяемые поля | Обновлённый адрес |
| `DELETE` | `/{id}` | Удалить адрес | Path: `id` | Сообщение об успехе |

### 💳 Кошельки (`/users/wallet`) 🔒
| Метод | Путь | Описание | Тело / Параметры | Ответ |
|:---|:---|:---|:---|:---|
| `GET` | `/` | Список кошельков пользователя | — | `List<WalletDTO>` |
| `GET` | `/{walletId}` | Кошелек по ID | Path: `walletId` | `WalletDTO` |
| `GET` | `/balance` | Общий баланс всех кошельков | — | `int` |
| `POST` | `/` | Создать новый кошелек | Body: `{name, balance, currency}` | Созданный кошелек |
| `PUT` | `/{walletId}` | Обновить данные кошелька | Path: `walletId`, Body: поля | Обновлённый кошелек |
| `DELETE` | `/{walletId}` | Удалить кошелек | Path: `walletId` | `204 No Content` |
| `PUT` | `/{walletId}/add-money` | Пополнить баланс | Path: `walletId`, Query: `amount` | Сообщение об успехе |

### 🛒 Корзина (`/users/cart`) 🔒
| Метод | Путь | Описание | Тело / Параметры | Ответ |
|:---|:---|:---|:---|:---|
| `GET` | `/` | Просмотр содержимого корзины | — | `CartDTO` |
| `POST` | `/{hobbyId}` | Добавить товар | Path: `hobbyId`, Query: `quantity` (по умолч. 1) | Обновлённая корзина |
| `PUT` | `/{hobbyId}` | Изменить количество | Path: `hobbyId`, Query: `quantity` | Обновлённая корзина |
| `DELETE` | `/{hobbyId}` | Удалить товар | Path: `hobbyId` | Корзина без товара |
| `DELETE` | `/clear` | Полная очистка корзины | — | Пустая корзина |
| `POST` | `/purchase` | Оформление заказа | Body: `{walletId, addressId, comment}` | `PurchaseResponseDTO` |

### 🎯 Каталог хобби (`/hobbies/catalog`)
| Метод | Путь | Описание | Тело / Параметры | Ответ |
|:---|:---|:---|:---|:---|
| `GET` | `/` | Поиск с фильтрами и пагинацией | Query: `name, minPrice, maxPrice, typeName, page, size, sortBy, sortDirection` | `PagedHobbiesResponseDTO` |
| `GET` | `/{id}` | Детальная информация о товаре | Path: `id` | `HobbyDTO` |
| `POST` | `/` | Создать хобби (ADMIN) | Body: `{name, description, price, typeId, imageUrl, inStock}` | Созданное хобби |
| `PUT` | `/{id}` | Обновить хобби (ADMIN) | Path: `id`, Body: изменяемые поля | Обновлённое хобби |
| `DELETE` | `/{id}` | Удалить хобби (ADMIN) | Path: `id` | Сообщение об успехе |

### 🏷️ Типы хобби (`/hobbies/types`)
| Метод | Путь | Описание | Тело / Параметры | Ответ |
|:---|:---|:---|:---|:---|
| `POST` | `/` | Создать тип (ADMIN) | Body: `{name, description}` | Созданный тип |
| `GET` | `/` | Список всех типов | — | `List<TypeHobbiesDTO>` |
| `GET` | `/{id}` | Тип по ID | Path: `id` | `TypeHobbiesDTO` |
| `PUT` | `/{id}` | Обновить тип (ADMIN) | Path: `id`, Body: изменяемые поля | Обновлённый тип |
| `DELETE` | `/{id}` | Удалить тип (ADMIN) | Path: `id` | `204 No Content` |

> 🔒 **Примечание:** Все эндпоинты, помеченные значком, требуют заголовок `Authorization: Bearer <token>`. Эндпоинты с пометкой `(ADMIN)` проверяют роль пользователя через `@PreAuthorize`.

---

## 📦 DTO Reference
```java
RegistUserDTO, AuthRequestDTO, AuthResponseDTO
UserAddressDTO, UpdateUserAddressDTO, WalletDTO, CartDTO
PurchaseDTO, PurchaseResponseDTO
HobbyDTO, UpdateHobbyDTO, PagedHobbiesResponseDTO
TypeHobbiesDTO, UpdateTypeHobbyDTO