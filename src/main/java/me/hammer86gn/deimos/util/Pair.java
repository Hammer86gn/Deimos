package me.hammer86gn.deimos.util;

/**
 * A record for holding two values of data together good for code cleanliness
 *
 * @param one the first value
 * @param two the second value
 * @param <T> the type of the first value
 * @param <E> the type of the second value
 */
public record Pair<T,E>(T one, E two) {

    @Override
    public String toString() {
        return this.one.toString() + ":" + this.two.toString() ;
    }
}
