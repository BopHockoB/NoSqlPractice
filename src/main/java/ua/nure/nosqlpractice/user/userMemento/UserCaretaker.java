package ua.nure.nosqlpractice.user.userMemento;

import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class UserCaretaker {
    private final List<UserMemento> mementoList = new ArrayList<>();
    private int index = 0;

    public UserMemento previous() {

        return (index > 0)?this.mementoList.get(--index):null;

    }
    public UserMemento next() {

        return (index < this.mementoList.size())?this.mementoList.get(++index):null;

    }

    public void addMemento(UserMemento memento) {
        if(index < this.mementoList.size())
        {
            for(int i = index; i < this.mementoList.size(); i++)
                this.mementoList.remove(i);
        }
        this.mementoList.add(memento);
        index++;
    }

    //Delete last memento from list
    public void delete(){
        if (index == mementoList.size())
            index--;
        mementoList.remove(mementoList.size() - 1);
    }

    //Delete memento by index
    public void delete(int i){
        if (i < 0 || i >= mementoList.size())
            return;
        if (i <= index - 1)
            index--;

        mementoList.remove(i);
    }
}
