package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/*
    Archivo helper tomado de https://github.com/phishman3579
 */
@SuppressWarnings("unchecked")
public class Graph<T extends Comparable<T>> {

    private List<Node<T>> allNodes = new ArrayList<Node<T>>();
    private List<Arista<T>> allAristas = new ArrayList<Arista<T>>();

    public enum TYPE {
        DIRECTED, UNDIRECTED
    }

    private TYPE type = TYPE.UNDIRECTED;

    public Graph() { }

    public Graph(TYPE type) {
        this.type = type;
    }

    public Graph(Graph<T> g) {
        type = g.getType();

        // Copy the Nodes which also copies the aristas
        for (Node<T> v : g.getNodes())
            this.allNodes.add(new Node<T>(v));

        for (Node<T> v : this.getNodes()) {
            for (Arista<T> e : v.getAristas()) {
                this.allAristas.add(e);
            }
        }
    }

    public Graph(Collection<Node<T>> Nodes, Collection<Arista<T>> aristas) {
        this(TYPE.UNDIRECTED, Nodes, aristas);
    }

    public Graph(TYPE type, Collection<Node<T>> Nodes, Collection<Arista<T>> aristas) {
        this(type);

        this.allNodes.addAll(Nodes);
        this.allAristas.addAll(aristas);

        for (Arista<T> e : aristas) {
            final Node<T> from = e.from;
            final Node<T> to = e.to;

            if (!this.allNodes.contains(from) || !this.allNodes.contains(to))
                continue;

            from.addEdge(e);
            if (this.type == TYPE.UNDIRECTED) {
                Arista<T> reciprical = new Arista<T>(e.cost, to, from);
                to.addEdge(reciprical);
                this.allAristas.add(reciprical);
            }
        }
    }

    public TYPE getType() {
        return type;
    }

    public List<Node<T>> getNodes() {
        return allNodes;
    }

    public List<Arista<T>> getEdges() {
        return allAristas;
    }

    @Override
    public int hashCode() {
        int code = this.type.hashCode() + this.allNodes.size() + this.allAristas.size();
        for (Node<T> v : allNodes)
            code *= v.hashCode();
        for (Arista<T> e : allAristas)
            code *= e.hashCode();
        return 31 * code;
    }

    @Override
    public boolean equals(Object g1) {
        if (!(g1 instanceof Graph))
            return false;

        final Graph<T> g = (Graph<T>) g1;

        final boolean typeEquals = this.type == g.type;
        if (!typeEquals)
            return false;

        final boolean NodesSizeEquals = this.allNodes.size() == g.allNodes.size();
        if (!NodesSizeEquals)
            return false;

        final boolean edgesSizeEquals = this.allAristas.size() == g.allAristas.size();
        if (!edgesSizeEquals)
            return false;

        // Nodes can contain duplicates and appear in different order but both arrays should contain the same elements
        final Object[] ov1 = this.allNodes.toArray();
        Arrays.sort(ov1);
        final Object[] ov2 = g.allNodes.toArray();
        Arrays.sort(ov2);
        for (int i=0; i<ov1.length; i++) {
            final Node<T> v1 = (Node<T>) ov1[i];
            final Node<T> v2 = (Node<T>) ov2[i];
            if (!v1.equals(v2))
                return false;
        }

        // Edges can contain duplicates and appear in different order but both arrays should contain the same elements
        final Object[] oe1 = this.allAristas.toArray();
        Arrays.sort(oe1);
        final Object[] oe2 = g.allAristas.toArray();
        Arrays.sort(oe2);
        for (int i=0; i<oe1.length; i++) {
            final Arista<T> e1 = (Arista<T>) oe1[i];
            final Arista<T> e2 = (Arista<T>) oe2[i];
            if (!e1.equals(e2))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Node<T> v : allNodes)
            builder.append(v.toString());
        return builder.toString();
    }

    public static class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

        private T value = null;
        private int weight = 0;
        private List<Arista<T>> aristas = new ArrayList<Arista<T>>();

        public Node(T value) {
            this.value = value;
        }

        public Node(T value, int weight) {
            this(value);
            this.weight = weight;
        }

        public Node(Node<T> node)
        {
            this(node.value, node.weight);

            this.aristas.addAll(node.aristas);
        }

        public T getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void addEdge(Arista<T> e) {
            aristas.add(e);
        }

        public List<Arista<T>> getAristas() {
            return aristas;
        }

        public Arista<T> getEdge(Node<T> v) {
            for (Arista<T> e : aristas) {
                if (e.to.equals(v))
                    return e;
            }
            return null;
        }

        public boolean pathTo(Node<T> v) {
            for (Arista<T> e : aristas) {
                if (e.to.equals(v))
                    return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            final int code = this.value.hashCode() + this.weight + this.aristas.size();
            return 31 * code;
        }

        @Override
        public boolean equals(Object v1) {
            if (!(v1 instanceof Graph.Node))
                return false;

            final Node<T> v = (Node<T>) v1;

            final boolean weightEquals = this.weight == v.weight;
            if (!weightEquals)
                return false;

            final boolean edgesSizeEquals = this.aristas.size() == v.aristas.size();
            if (!edgesSizeEquals)
                return false;

            final boolean valuesEquals = this.value.equals(v.value);
            if (!valuesEquals)
                return false;

            final Iterator<Arista<T>> iter1 = this.aristas.iterator();
            final Iterator<Arista<T>> iter2 = v.aristas.iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                // Only checking the cost
                final Arista<T> e1 = iter1.next();
                final Arista<T> e2 = iter2.next();
                if (e1.cost != e2.cost)
                    return false;
            }

            return true;
        }

        @Override
        public int compareTo(Node<T> v) {
            final int valueComp = this.value.compareTo(v.value);
            if (valueComp != 0)
                return valueComp;

            if (this.weight < v.weight)
                return -1;
            if (this.weight > v.weight)
                return 1;

            if (this.aristas.size() < v.aristas.size())
                return -1;
            if (this.aristas.size() > v.aristas.size())
                return 1;

            final Iterator<Arista<T>> iter1 = this.aristas.iterator();
            final Iterator<Arista<T>> iter2 = v.aristas.iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                // Only checking the cost
                final Arista<T> e1 = iter1.next();
                final Arista<T> e2 = iter2.next();
                if (e1.cost < e2.cost)
                    return -1;
                if (e1.cost > e2.cost)
                    return 1;
            }

            return 0;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Value=").append(value).append(" weight=").append(weight).append("\n");
            for (Arista<T> e : aristas)
                builder.append("\t").append(e.toString());
            return builder.toString();
        }
    }

    public static class Arista<T extends Comparable<T>> implements Comparable<Arista<T>> {

        private Node<T> from = null;
        private Node<T> to = null;
        private int cost = 0;

        public Arista(int cost, Node<T> from, Node<T> to) {
            if (from == null || to == null)
                throw (new NullPointerException("Both 'to' and 'from' Nodes need to be non-NULL."));

            this.cost = cost;
            this.from = from;
            this.to = to;
        }

        public Arista(Arista<T> e) {
            this(e.cost, e.from, e.to);
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public Node<T> getFromNode() {
            return from;
        }

        public Node<T> getToNode() {
            return to;
        }

        @Override
        public int hashCode() {
            final int cost = (this.cost * (this.getFromNode().hashCode() * this.getToNode().hashCode())); 
            return 31 * cost;
        }

        @Override
        public boolean equals(Object e1) {
            if (!(e1 instanceof Graph.Arista))
                return false;

            final Arista<T> e = (Arista<T>) e1;

            final boolean costs = this.cost == e.cost;
            if (!costs)
                return false;

            final boolean from = this.from.equals(e.from);
            if (!from)
                return false;

            final boolean to = this.to.equals(e.to);
            if (!to)
                return false;

            return true;
        }

        @Override
        public int compareTo(Arista<T> e) {
            if (this.cost < e.cost)
                return -1;
            if (this.cost > e.cost)
                return 1;

            final int from = this.from.compareTo(e.from);
            if (from != 0)
                return from;

            final int to = this.to.compareTo(e.to);
            if (to != 0)
                return to;

            return 0;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[ ").append(from.value).append("(").append(from.weight).append(") ").append("]").append(" -> ")
                   .append("[ ").append(to.value).append("(").append(to.weight).append(") ").append("]").append(" = ").append(cost).append("\n");
            return builder.toString();
        }
    }

    public static class CostNodePair<T extends Comparable<T>> implements Comparable<CostNodePair<T>> {

        private int cost = Integer.MAX_VALUE;
        private Node<T> node = null;

        public CostNodePair(int cost, Node<T> node) {
            if (node == null)
                throw (new NullPointerException("node cannot be NULL."));

            this.cost = cost;
            this.node = node;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public Node<T> getNode() {
            return node;
        }

        @Override
        public int hashCode() {
            return 31 * (this.cost * ((this.node !=null)?this.node.hashCode():1));
        }

        @Override
        public boolean equals(Object e1) {
            if (!(e1 instanceof CostNodePair))
                return false;

            final CostNodePair<?> pair = (CostNodePair<?>)e1;
            if (this.cost != pair.cost)
                return false;

            if (!this.node.equals(pair.node))
                return false;

            return true;
        }

        @Override
        public int compareTo(CostNodePair<T> p) {
            if (p == null)
                throw new NullPointerException("CostNodePair 'p' must be non-NULL.");

            if (this.cost < p.cost)
                return -1;
            if (this.cost > p.cost)
                return 1;
            return 0;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(node.getValue()).append(" (").append(node.weight).append(") ").append(" cost=").append(cost).append("\n");
            return builder.toString();
        }
    }

    public static class CostPathPair<T extends Comparable<T>> {

        private int cost = 0;
        private List<Arista<T>> path = null;

        public CostPathPair(int cost, List<Arista<T>> path) {
            if (path == null)
                throw (new NullPointerException("path cannot be NULL."));

            this.cost = cost;
            this.path = path;
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public List<Arista<T>> getPath() {
            return path;
        }

        @Override
        public int hashCode() {
            int hash = this.cost;
            for (Arista<T> e : path)
                hash *= e.cost;
            return 31 * hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CostPathPair))
                return false;

            final CostPathPair<?> pair = (CostPathPair<?>) obj;
            if (this.cost != pair.cost)
                return false;

            final Iterator<?> iter1 = this.getPath().iterator();
            final Iterator<?> iter2 = pair.getPath().iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                Arista<T> e1 = (Arista<T>) iter1.next();
                Arista<T> e2 = (Arista<T>) iter2.next();
                if (!e1.equals(e2))
                    return false;
            }

            return true;
        }

        @Override
        public String toString()
        {
            final StringBuilder builder = new StringBuilder();
            for (Arista<T> e : path)
                builder.append("\t").append(e);
            builder.append("Total Aristas = "+this.path.size()+"\n");
            builder.append("Costo = ").append(cost).append("\n");
            return builder.toString();
        }
    }

}