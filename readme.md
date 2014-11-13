Windows launch example
======================
from project dir
----------------
`java -Dfile.encoding=Cp866 -jar target/sentenceProcessor.jar`

or if you put .jar and .properties in same directory
----------------------------------------------------
`java -Dfile.encoding=Cp866 -jar sentenceProcessor.jar`

Featurs list
============
* [Spring] инъекция значений из `.properties`, использование кодировки UTF-8
* Валидация значений с помощью *Hibernate Validator*
* получение версии и имени артифакта из pom.xml через `version.properties` с помощью фильтрации ресурсов `pom.xml:74`
* инъекция из обработанного `target\classes\version.properties` версии и имени артифакта в `@Autowired` бин `Version.java`