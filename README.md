# Guava

**tl;dr**: Guava is a statically typed language with some handy features:

- multiple inheritance
- operator overloading
- generics, which can have variance
- a `Top` and `Bottom` type
- primitives which behave like normal objects
- and lots more!


## Overview

Guava is a new OOP language with a simple but powerful type system.

Guava has native types, but no primitive types. For example, a `Char` object is
implemented as a native type for efficiency. But unlike a Java `char`, `Char` has
methods, extends other types, and can be used as a generic argument.

Guava also has array types, but to the programmer, they look no different from
any other types. Although `Array` is a native type, it fits nicely into the type
hierarchy: it extends `Sequence`, which extends `Collection`, etc.

As in Scala, Guava lets you specify the variance of generic parameters. So an
`Array[Char]` is also an `Array[Top]`, and an `Ordering[Sequence]` is also an
`Ordering[Array]`.

Guava encourages immutability, but provides both mutable and immutable versions
of many data structures. For example, `MutableSet[T]` declares add and remove
methods which mutate the structure and return nothing, while `ImmutableSet[T]`
declares add and remove methods which create and return new `ImmutableSet`s
without modifying the current one. Both extend the more general `Set[T]`.

Guava has a `Top` type which, like Java's `Object`, sits at the top of the type
hierarchy. Guava also has a `Bottom` type, which behaves like Scala's `Nothing`:
it is a subtype of every other type, but it is impossible to instantiate. This
is very useful in combination with generic variance. The empty linked list
(`EmptyList`) type extends `List[Bottom]`, and since a `List[Bottom]` is also a
`List[T]` for any other `T`, you can use it as the basis of any other list.
Creating multiple empty lists is hence unncessary, and in fact impossible since
`EmptyList` is a singleton type.

Guava supports operator overloading. The expression `x + y` is just syntactic
sugar for `x.+(y)`. Types can also overload the subscript operator by
implementing `get` and/or `set` methods. For example, the type `Array[T]`
contains methods which look like this:

    T get(Int index);
    T set(Int index, T val);

Here are a few examples of what you can do with operator overloading:

    Sequence[Int] s = {1, 2, 3} * 10;
    repeat (s(5))
      Console.outln("hey " * 2 + "world");

You can even implement static get and set methods to make a type name behave
like a map. This is useful for writing pseudoconstructors which, unlike real
constructors, may perform interning.

Instance methods can be invoked statically. `Top.==(a, b)` will always
perform an identity comparison, whether or not `a`'s type implements its own `==`
method.

Guava has no null references. Instead, it has a `Maybe` type similar to Haskell's
`Maybe` or Scala's `Option`.

Guava's standard library embraces inheritance in a major way. For example, the
`MutableDeque` type extends `MutbleStack` and `MutableQueue`. If you write a function
which reverses a `MutableStack`, you can feed it an `ArrayDeque`, a `DynamicArray`, a
`MutableSinglyLinkedList`, etc. Guava's powerful type system and careful API
design allow these relationships to be expressed, while strictly adhering to
substitutability.


## Building

Building and running Guava is easy:

    git clone git@github.com:dlubarov/guava.git
    cd guava
    ./compile
    ./run-demos
    ./run-tests

Note that these scripts require Bash 4.0 or higher.

Since Guava has no dependencies or generated code, it is also trivial to build
it using any Java IDE.
