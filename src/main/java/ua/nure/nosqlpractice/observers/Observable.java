package ua.nure.nosqlpractice.observers;

public interface Observable {
    void registerObserver(Observer observer); // Метод для регистрации наблюдателей
    void removeObserver(Observer observer); // Метод для удаления наблюдателей
    void notifyObservers(Object o); // Метод для оповещения наблюдателей об изменениях
}
