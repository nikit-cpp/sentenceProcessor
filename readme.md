Задание
=======
Нужно вывести все предложения из файла, содержащие определённое количество слов.

Building
========
From project's folder `sentenceProcessor`:  
`mvn package`

Конфигурация
============
Нужно скопировать `my.properties.example` и переименовать в `my.properties`.  
Далее, как минимум, в `my.properties` нужно настроить/поменять значение опции `fileName` на правильное.  
Это может быть как файл, так и папка, содержащая текстовые файлы в одинаковой кодировке.  
**Программа выведет предложения, содержащие `wordCount` слов.**  
Программа циклически перезагружает значения опций.  
После работы на экране появится статистика `wasted time ...` и запрос выйти, нажав `q`.  
Можно поменять значения опций и нажать `Enter`, чтобы программа отработала с новыми опциями.

Windows launch example
======================
from project folder
-------------------
`java -Dfile.encoding=Cp866 -jar target/sentenceProcessor.jar`

or if you put `.jar` and `.properties` in same directory
--------------------------------------------------------
`java -Dfile.encoding=Cp866 -jar sentenceProcessor.jar`

Features list
=============
* [Spring] инъекция значений из `.properties`, использование кодировки UTF-8
* Валидация значений с помощью *Hibernate Validator*
* получение версии и имени артифакта из pom.xml через `version.properties` с помощью фильтрации ресурсов `pom.xml:74`
* инъекция из обработанного `target\classes\version.properties` версии и имени артифакта в `@Autowired` бин `Version.java`