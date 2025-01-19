# Использование ключевого слова volatile в Java

## Что такое volatile?

`volatile` — это ключевое слово в Java, которое используется для обозначения переменной как "летучей".
Это значит, что все потоки видят изменения этой переменной сразу же после их внесения в память.

## Основные свойства volatile:

1. Гарантия видимости: изменения в volatile переменной одним потоком немедленно становятся видимыми другим потокам.
2. Запрет кэширования: переменная с модификатором volatile всегда читается и записывается из основной памяти, 
а не из кэша процессора.
3. Отсутствие синхронизации: volatile не обеспечивает атомарность операций, что означает, 
что для более сложных операций (например, инкрементирования) потребуется дополнительная синхронизация.

## Зачем использовать volatile?
Использование volatile необходимо в многопоточных приложениях, когда:

1. Обеспечение видимости изменений: гарантируется, что изменения переменной, выполненные одним потоком,
будут немедленно видны другим потокам.

2. Минимизация синхронизации: когда нет необходимости в использовании полных механизмов синхронизации (synchronized),
но требуется гарантировать видимость изменений.

## Примеры кода

Пример с использованием volatile

```java
public class GoodCode {
    volatile static int i = 0;

    public static void main(String[] args) {
        System.out.println("GoodCode");
        new MyThreadWriter().start();
        new MyThreadReader().start();
    }

    static class MyThreadWriter extends Thread {
        @Override
        public void run() {
            while (i < 5) {
                System.out.println("Increment i to " + (++i));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Finished MyThreadWriter");
        }
    }

    static class MyThreadReader extends Thread {
        @Override
        public void start() {
            int li = i;
            while(li < 5) {
                if (li != i) {
                    System.out.println("New value of i is " + i);
                    li = i;
                }
            }
            System.out.println("Finished MyThreadRead");
        }
    }
}
```

Пример без использования volatile

```java
public class BadCode {
    static int i = 0;

    public static void main(String[] args) {
        System.out.println("BadCode");
        new MyThreadWriter().start();
        new MyThreadReader().start();
    }

    static class MyThreadWriter extends Thread {
        @Override
        public void run() {
            while (i < 5) {
                System.out.println("Increment i to " + (++i));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Finished MyThreadWriter");
        }
    }

    static class MyThreadReader extends Thread {
        @Override
        public void start() {
            int li = i;
            while(li < 5) {
                if (li != i) {
                    System.out.println("New value of i is " + i);
                    li = i;
                }
            }
            System.out.println("Finished MyThreadRead");
        }
    }
}
```

## Анализ примеров

В первом примере (GoodCode) использование volatile гарантирует, что изменения переменной i, сделанные в MyThreadWriter,
немедленно видны в MyThreadReader. В результате программа корректно выводит обновления переменной i.

Во втором примере (BadCode) отсутствует модификатор volatile, что может привести к ситуации,
когда изменения переменной i не сразу видны другим потокам. Это может привести к некорректному поведению программы,
например, к "зависанию" в цикле из-за того, что поток MyThreadReader не видит обновлений переменной i.

## Заключение
Ключевое слово volatile — это простой способ обеспечить видимость изменений переменной между потоками в Java.
Однако, оно не заменяет синхронизацию для сложных операций. Используйте volatile, когда нужно гарантировать,
что изменения переменной будут немедленно видны другим потокам, но для атомарных операций или сложных сценариев лучше
использовать другие механизмы синхронизации.

## Вывод в консоль:
### BadCode
```text
BadCode
Increment i to 1
Increment i to 2
Increment i to 3
Increment i to 4
Increment i to 5
Finished MyThreadWriter

```

### GoodCode
```text
GoodCode
Increment i to 1
Increment i to 2
New value of i is 2
Increment i to 3
New value of i is 3
New value of i is 4
Increment i to 4
New value of i is 5
Increment i to 5
Finished MyThreadRead
Finished MyThreadWriter

Process finished with exit code 0
```