# Chat with AI
Приложение, позволяющее взаимодействовать с LLM-моделью через API, включая поддержку шаблонов RAG, а также множества других функций, описанных ниже.
Является тестовым заданием для отбора в RTUItLab. 

## Описание проекта
### Функционал
- Отправка запросов к LLM-модели с возможностью ввода текста.
- Использование шаблонов RAG для автоматического заполнения контекста.
- Возможность создавать, редактировать и удалять шаблоны RAG.
- Поиск по шаблонам по ключевым словам.
- Просмотр истории запросов и ответов.
- Поиск по истории запросов по ключевым словам.
- Сортировка истории запросов и списка шаблонов по дате и алфавиту.
- Возможность управления чатами с LLM.
- Уведомление пользователя зайти в приложение, чтобы спросить LLM.
- Сохранение избранных запросов и шаблонов.
- Работа с историей запросов и шаблонами в оффлайн-режиме.
- Экспорт истории запросов и ответов в текстовый файл на устройстве.
- Поддержка голосового ввода запроса.

### Архитектура
Архитектура приложения соответствует принципам Clean Architecture. Файлы проекта разделены в соответствии с выполняемыми функциями на слои Data, Domain, Presentation, UseCases, а также несколько других пакетов с доп. логикой. Внутри каждого пакета классы и функции так же распределены логически. 
Выбор архитектуры был основан на её актуальности, а также сочетаемости с применяемыми технологиями.

### Скриншоты приложения
![image](https://github.com/user-attachments/assets/74a804ce-eb1a-4e65-80ec-ca1b22a8fc96)
![image](https://github.com/user-attachments/assets/d25a0e53-4fef-42c6-b0ec-1837fa2f58da)
![image](https://github.com/user-attachments/assets/a3c011de-7fb7-44cf-84c8-90bc799606d2)
![image](https://github.com/user-attachments/assets/32310aae-a522-4973-b718-20c0ce390303)
![image](https://github.com/user-attachments/assets/3ee5dcab-34c8-47f1-83a7-3a3475f78de0)
![image](https://github.com/user-attachments/assets/b924c02e-cee6-4b93-9808-0c62f731ad90)
![image](https://github.com/user-attachments/assets/db3f807d-1068-48c1-bd0e-b17e488453e6)
![image](https://github.com/user-attachments/assets/a682c112-55ac-4cd7-a100-b11ceed69b99)
![image](https://github.com/user-attachments/assets/95c64786-5934-4175-8a38-a8351df53105)
![image](https://github.com/user-attachments/assets/4e3f4e2c-5c69-4838-a553-2d6ff97cec13)
![image](https://github.com/user-attachments/assets/33f36e83-cf68-4c69-a7e6-1c7e55101eef)
![image](https://github.com/user-attachments/assets/3a23f54b-c9ab-4a79-9092-13c6cf21c6b9)
![image](https://github.com/user-attachments/assets/6b0c1e91-b1df-4fd4-9334-36dbb5d58fb0)
![image](https://github.com/user-attachments/assets/fb806299-c951-46fd-a374-42ef83eeffe4)
![image](https://github.com/user-attachments/assets/78a41ce9-a4cb-4298-9df7-9d014a01e606)
![image](https://github.com/user-attachments/assets/48efb2f0-91a0-4855-917f-38a975731976)
![image](https://github.com/user-attachments/assets/262a2d99-8c95-4a7d-9de0-fa6ac7336643)
![image](https://github.com/user-attachments/assets/5cc0f628-c250-43b2-b124-40a62a10e995)
![image](https://github.com/user-attachments/assets/c2f78781-567d-4ee1-9a54-94afe91fe414)
![image](https://github.com/user-attachments/assets/2479dc8d-2d29-4e73-a052-2a09e4b32a31)
![image](https://github.com/user-attachments/assets/6391e099-3cbf-4123-875c-57eb10110494)


## Инструкция по запуску
Для запуска необходимо установить Android Studio для сборки проекта из исходных данных с последующим запуском на эмуляторе или же на своем смартфоне, либо скачать apk и запустить его на смартфоне непосредственно.

+ Минимальный Android SDK - 29
+ Target Android SDK - 35
+ Compile Android SDK - 35
+ JDK Version - 17 
+ Kotlin Version - 1.9.0
