# Использование ключевого слова synchronized в Java

В многозадачных приложениях важно обеспечивать корректный доступ к разделяемым данным между потоками.
Когда несколько потоков одновременно изменяют одни и те же данные, возникает риск ошибок. 
Для того чтобы избежать таких проблем, в Java используется ключевое слово synchronized, которое гарантирует
последовательный доступ к критическим участкам кода.

## Ошибка Race Condition
Представьте ситуацию, когда несколько потоков одновременно изменяют одно и то же состояние объекта.
Если доступ к этому состоянию не синхронизирован, то возможны гонки потоков,
когда несколько потоков пытаются одновременно изменять данные.
Это может привести к неконсистентному состоянию объекта и непредсказуемым результатам.

### Пример Race Condition:
```java
public class BadCode {
    public static void main(String[] args) throws InterruptedException{
        Counter counter = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.decrement();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Counter  = " + counter.getCounter());
    }

    static class Counter {
        int cnt = 0;

        void increment() {
            cnt++;
        }

        void decrement() {
            cnt--;
        }

        int getCounter() {
            return cnt;
        }
    }
}
```

### Вывод в консоль Race Condition:
Поскольку потоки не синхронизированы, каждый раз мы можем получать некорректные результаты.
В случае, если два потока одновременно читают и записывают значение переменной, могут возникать неожиданные ситуации,
такие как потеря обновлений данных или несогласованность результатов. 
Они могут перезаписать изменения другого потока, нарушая целостность данных.
```text
Counter  = -7
Counter  = -5
Counter  = 21
```

## Слово `synchronized`
Чтобы избежать подобных ошибок, можно использовать ключевое слово `synchronized`,
которое синхронизирует доступ к методам или блокам кода.
Это гарантирует, что только один поток будет выполнять синхронизированную часть кода в одно время.

### Как работает `synchronized`?
Когда вы используете слово synchronized в Java, вы фактически "запрашиваете" так называемый монитор объекта,
для того чтобы ограничить доступ к критической секции кода. Для каждого объекта в Java существует свой монитор
(В том числе может быть монитор для всего класса, не только его экземпляра).
Если поток получает монитор какого-то объекта, другие потоки, которые тоже хотят захватить этот монитор,
вынуждены будут ждать, пока первый поток не завершит свою работу и не освободит монитор.

### Пример `synchronized`
Допустим у нас есть интерфейс `ICounter`:
```java
interface ICounter {
    void increment();
    void decrement();
    int getValue();
}
```
### Синхронизация методов с помощью `synchronized`
Самый простой способ синхронизировать доступ к данным — это использовать ключевое слово synchronized перед методами.
```java
public class GoodCode {
    public static void main(String[] args) throws InterruptedException{
        ICounter counter = new Counter1();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.decrement();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Counter  = " + counter.getValue());
    }

    static class Counter1 implements ICounter {
        int value = 0;
        @Override
        synchronized public void increment() {
            value++;
        }

        @Override
        synchronized public void decrement() {
            value--;
        }

        @Override
        synchronized public int getValue() {
            return value;
        }
    }
}
```
В данном случае мы синхронизируемся по объекту `counter`.

### 2. Синхронизация блоков с помощью synchronized и this
Если синхронизация требуется только для части метода, можно использовать блоки synchronized.
Интерфейс `ICounter` можно реализовать вот так:
```java
static class Counter2 implements ICounter {
        int value = 0;
        @Override
        public void increment() {
            synchronized (this) {
                value++;
            }
        }

        @Override
        public void decrement() {
            synchronized (this) {
                value--;
            }
        }

        @Override
        public int getValue() {
            synchronized (this) {
                return value;
            }
        }
    }
```
Данный класс аналогичен классу `Counter1`, так как мы также синхронизируемся по объекту `counter`.
### 3. Синхронизация с использованием внешнего монитора
Если требуется синхронизировать доступ к данным через несколько объектов или методов,
можно использовать внешний объект-монитор.
```java
 static class Counter3 implements ICounter {
        final Object monitor = new Object();
        int value = 0;
        @Override
        public void increment() {
            synchronized (monitor) {
                value++;
            }
        }

        @Override
        public void decrement() {
            synchronized (monitor) {
                value--;
            }
        }

        @Override
        public int getValue() {
            synchronized (monitor) {
                return value;
            }
        }
    }
```
В этом случае синхронизация осуществляется с использованием внешнего объекта monitor,
что позволяет более гибко контролировать доступ к данным.

### Вывод в консоль
При использовании любого класса (`Counter1`, `Counter2` или `Counter3`) в результате счетчик всегда будет равен 0
```text
Counter  = 0

Process finished with exit code 0
```

## Заключение
Использование ключевого слова `synchronized` в Java позволяет управлять доступом нескольких потоков к разделяемым данным,
предотвращая возможные ошибки, связанные с некорректным доступом. Это достигается путем захвата монитора объекта,
что обеспечивает эксклюзивный доступ потока к критической секции. Важно правильно выбирать,
какие части кода нужно синхронизировать,
чтобы избежать излишних блокировок и гарантировать корректную работу программы в многозадачной среде.




