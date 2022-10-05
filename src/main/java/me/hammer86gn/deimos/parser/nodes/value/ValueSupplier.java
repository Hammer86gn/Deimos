package me.hammer86gn.deimos.parser.nodes.value;

public interface ValueSupplier {

    <U> U getValue();

    String toString();

}
