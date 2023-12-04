package ua.nure.nosqlpractice.observers;

public interface Observer {
    void onDataChanged(Object o); // Метод, который будет вызываться при изменениях в базе данных
}