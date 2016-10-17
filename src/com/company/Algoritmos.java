package com.company;

import java.util.*;

/**
 * Created by Ruben on 10/16/16.
 */
public class Algoritmos<T extends Comparable<T>>
{
    public Graph.CostPathPair<Integer> Prim(Graph<T> graph, Graph.Node<T> inicio)
    {
        int cost = 0;
        final Set<Graph.Node<Integer>> unvisited = new HashSet<Graph.Node<Integer>>();
        unvisited.addAll((Collection)graph.getNodes());
        unvisited.remove(inicio);

        final List<Graph.Arista<Integer>> camino = new ArrayList<Graph.Arista<Integer>>();
        final Queue<Graph.Arista<Integer>> aristasDisponibles = new PriorityQueue<Graph.Arista<Integer>>();

        Graph.Node<Integer> node = (Graph.Node<Integer>) inicio;

        while (!unvisited.isEmpty()) // Todos los nodos seran NO visitados al inicio
        {
            for (Graph.Arista<Integer> arista : node.getAristas()) //Caminos desde nuestro nodo
            {
                if (unvisited.contains(arista.getToNode())) //Si aun no visitamos este
                    aristasDisponibles.add(arista);
            }

            //Nos da la de menor costo por la estructura de datos, pero puede caer en ciclos
            while (!aristasDisponibles.isEmpty())
            {
                final Graph.Arista<Integer> arista_barata = aristasDisponibles.remove();
                if (unvisited.contains(arista_barata.getToNode()))
                {
                    cost += arista_barata.getCost(); //Acumulamos esta minima
                    node = arista_barata.getToNode(); //Avanzamos
                    camino.add(arista_barata); // Vamos guardando esta ruta
                    break;
                }
            }

            unvisited.remove(node);     //Vamos quitando sin visitar
        }
        return (new Graph.CostPathPair<Integer>(cost,camino));
    }

    public Graph.CostPathPair<Integer> Kruskal_UNION(Graph<Integer> graph)
    {
        final List<Graph.Arista<Integer>> camino = new ArrayList<>();
        int cost = 0;

        Collections.sort(graph.getEdges(),(Graph.Arista<Integer> o1, Graph.Arista<Integer> o2)->
        {
            return o1.compareTo(o2);
        });

        //AQUI YA TENEMOS ORDENADOS DE MENOR A MAYOR LOS PESOS

        //Falta implementar kruskall


        return (new Graph.CostPathPair<Integer>(cost,camino));
    }
}
