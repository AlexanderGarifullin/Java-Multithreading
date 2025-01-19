# Способы создания потоков в Java

В Java существует несколько способов создания потоков, каждый из которых имеет свои преимущества и недостатки.
Рассмотрим четыре основных подхода с примерами кода и выводом.

## 1. Наследование от класса `Thread`

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread " +  Thread.currentThread().getName() + " run from MyThread!");
    }
}

MyThread thread1 = new MyThread();
thread1.start();
thread1.join();
```

### Преимущества:

1. Простота использования: не требует явного создания объекта `Runnable`.
2. Легко переопределить метод `run()`.

### Недостатки:

1. Ограничение на наследование: класс уже наследуется от `Thread`,
что исключает возможность наследования от другого класса.
2. Менее гибко, так как логика потока жестко связана с классом.

## 2. Реализация интерфейса `Runnable`

```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread " +  Thread.currentThread().getName() + " run from MyRunnable!");
    }
}

MyRunnable myRunnable = new MyRunnable();
Thread thread2 = new Thread(myRunnable);
thread2.start();
thread2.join();
```

### Преимущества:

1. Гибкость: класс может наследовать от другого класса и реализовывать интерфейс `Runnable`.
2. Логика потока отделена от механизма выполнения.

### Недостатки:

1. Чуть больше кода, так как необходимо создавать экземпляр `Runnable` и передавать его в `Thread`.

## 3. Анонимный класс, реализующий `Runnable`

```java
Thread thread3 = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Thread " +  Thread.currentThread().getName() + " run from Anonymous Runnable!");
    }
});
thread3.start();
thread3.join();
```

### Преимущества:

1. Удобство: позволяет быстро создавать одноразовые потоки.
2. Подходит для ситуаций, когда нужно использовать поток только один раз.

### Недостатки:

1. Меньшая читаемость кода для больших задач.
2. Трудно повторно использовать код.

## 4. Лямбда-выражение

```java
Thread thread4 = new Thread(() -> {
    System.out.println("Thread " +  Thread.currentThread().getName() + " run from Lambda Runnable!");
});

thread4.start();;
thread4.join();
```

### Преимущества:

1. Краткость: минимальное количество кода для создания потока.
2. Удобочитаемость: код легче читать и понимать.
3. Идеально подходит для небольших задач.

### Недостатки:

1. Ограниченная применимость: может быть сложно использовать для более сложной логики,
так как код может стать менее читаемым.


## Какой способ лучше?
Лучший способ зависит от конкретного случая:
* Для простых задач или когда нужно создать поток быстро, лучше использовать лямбда-выражения.
* Для повторного использования и гибкости лучше подходит реализация интерфейса `Runnable`.
* Для одноразовых задач можно использовать анонимные классы.
* Если нужно наследовать `Thread` (например, при переопределении методов `Thread`),
стоит использовать наследование от `Thread`.

## Вывод в консоль:
``` text
Thread Thread-0 run from MyThread!
Thread Thread-1 run from MyRunnable!
Thread Thread-2 run from Anonymous Runnable!
Thread Thread-3 run from Lambda Runnable!
```