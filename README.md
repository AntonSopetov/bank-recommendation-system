# Star Bank - Модуль Персональных Рекомендаций

Система автоматического подбора банковских продуктов для клиентов на основе анализа их транзакционной активности и текущих открытых продуктов. Поддерживает как статические жесткие правила (MVP), так и динамически настраиваемые правила через REST API.

## Стек технологий
* **Язык разработки:** Java 17
* **Фреймворк:** Spring Boot 3.x (Web, Data JPA)
* **Базы данных:**
    * H2 Database (Файловая read-only база транзакций клиентов)
    * PostgreSQL (Read/Write база для хранения динамических правил)
* **Миграции БД:** Liquibase
* **Кэширование:** Caffeine Cache

## Ссылки на документацию (Project Wiki)
Для успешной защиты курсовой работы вся аналитическая и техническая документация вынесена в репозиторий Wiki:
* [Главная страница проекта](https://github.com/AntonSopetov/bank-recommendation-system/wiki)
* [Бизнес-требования (User Stories и Use Case)](https://github.com/AntonSopetov/bank-recommendation-system/wiki/Requirements)
* [Архитектура системы (Диаграммы Компонентов и Деятельности)](https://github.com/AntonSopetov/bank-recommendation-system/wiki/Architecture)
* [Инструкция по развёртыванию (Deployment Guide)](https://github.com/AntonSopetov/bank-recommendation-system/wiki/Deployment)