package fr.arthix.skyarena.rewards;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    private NavigableMap<Double, E> navigableMap;
    private Random random;
    private double total;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(final Random random) {
        this.navigableMap = new TreeMap<>();
        this.total = 0.0;
        this.random = random;
    }

    public void add(final double weight, final E result) {
        if (weight <= 0.0) {
            return;
        }
        this.total += weight;
        this.navigableMap.put(this.total, result);
    }

    public E next() {
        final double value = this.random.nextDouble() * this.total;
        return this.navigableMap.ceilingEntry(value).getValue();
    }
}
