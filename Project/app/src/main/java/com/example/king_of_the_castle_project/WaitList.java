package com.example.king_of_the_castle_project;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

public class WaitList extends ArrayList<Entrant> {
    private int numberOfEntrants;

    public WaitList(int initialCapacity) {
        super(initialCapacity);
    }

    public WaitList() {
    }

    public WaitList(@NonNull Collection c) {
        super(c);
    }

    public int getNumberOfEntrants() {
        return numberOfEntrants;
    }

    public void setNumberOfEntrants(int numberOfEntrants) {
        this.numberOfEntrants = numberOfEntrants;
    }
}
