# Sber_Task_2
Зарегистрироваться на сервисе передачи данных о погоде - https://www.weatherapi.com/. Документация по API - https://www.weatherapi.com/docs/.
Используя любой BDD фреймворк реализовать:

- Позитивный тест:

1.	Запросить текущую погоду (метод /current.json) минимум по четырем городам на свой выбор.
2.	Распарсить результат, сравнить с ожидаемыми значениями из тестового набора. Ожидаемые тестовые данные можно определить или задать для каждого города корректные, либо можно задать\сгенерировать случайным образом с соблюдением формата (wind_speed, temperature и пр.).
3.	Вывести расхождения по результату сравнения по каждому значению в лог.
      
- Негативный тест:

1. Получить 4 варианта ошибок из списка API Errors (на выбор), сравнить с ожидаемым результатом.
      
Результат выполнения тестов должен быть в отчете Allure.
