package com.company;

import java.util.List;

/*
    Archivo helper tomado de https://github.com/phishman3579
 */
@SuppressWarnings("unchecked")
public class DisjointSet<T extends Object> {

    public DisjointSet() { }

    public static Integer indexOfNode(List<Item<Graph.Node<Integer>>> v,Graph.Node<Integer> n)
    {
        for (int i = 0; i < v.size() ; i++)
        {
            if ( v.get(i).getValue().compareTo(n)==0) //Son iguales
            {
                return i; //Index
            }
        }
        return -1;
    }

    public static final <T extends Object> Item<T> makeSet(T v)
    {
        final Item<T> item = new Item<T>(null,v);
        item.parent = item;
        return item;
    }

    public static final <T extends Object> Item<T> find(Item<T> x) {
        if (x == null)
            return null;

        if ((x.parent!=null) && !(x.parent.equals(x)))
            return x.parent = find(x.parent);
        return x.parent;
    }

    public static final <T extends Object> Item<T> union(Item<T> x, Item<T> y) {
        final Item<T> xRoot = find(x);
        final Item<T> yRoot = find(y);
        if (xRoot==null && yRoot==null)
            return null;
        if (xRoot==null && yRoot!=null)
            return yRoot;
        if (yRoot==null && xRoot!=null)
            return xRoot;

        // x and y are in the same set
        if (xRoot.equals(yRoot))
            return xRoot;

        if (xRoot.rank < yRoot.rank) {
            xRoot.parent = yRoot;
            return yRoot;
        } else if (xRoot.rank > yRoot.rank) {
            yRoot.parent = xRoot;
            return xRoot;
        }
        // else
        yRoot.parent = xRoot;
        xRoot.rank = xRoot.rank + 1;
        return xRoot;
    }

    public static final class Item<T> {

        private Item<T> parent;
        private T value;
        private long rank;

        public Item(Item<T> parent, T value) {
            this.parent = parent;
            this.value = value;
            this.rank = 0;
        }

        public T getValue() {
            return value;
        }
        public long getRank() {
            return rank;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Item))
                return false;

            final Item<T> i = (Item<T>) o;
            if ((i.parent!=null && parent!=null) && !(i.parent.value.equals(parent.value)))
                return false;
            if ((i.value!=null && value!=null) && !(value.equals(value)))
                return false;
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "parent="+(parent!=null?parent.value:null)+" "+
                    (rank>0?"rank="+rank +" " : "") +
                    "value="+(value!=null?value:null);
        }
    }
}
