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
        final List<Graph.Arista<Integer>> aristasDisponibles = new ArrayList<>();

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
                int cheapest_index = 0;
                Graph.Arista<Integer> arista_barata = aristasDisponibles.get(cheapest_index);
                for (int i = 0; i < aristasDisponibles.size(); i++) //Usando una lista iteramos por la mas barata de ellas
                {
                    if (arista_barata.getCost() > aristasDisponibles.get(i).getCost())
                    {
                        cheapest_index=i;
                        arista_barata=aristasDisponibles.get(i);
                    }
                }
                if (unvisited.contains(arista_barata.getToNode()))
                {
                    cost += arista_barata.getCost(); //Acumulamos esta minima
                    node = arista_barata.getToNode(); //Avanzamos
                    camino.add(arista_barata); // Vamos guardando esta ruta
                    break;
                }
                aristasDisponibles.remove(cheapest_index);
            }

            unvisited.remove(node);     //Vamos quitando sin visitar
        }
        return (new Graph.CostPathPair<Integer>(cost,camino));
    }

    public Graph.CostPathPair<Integer> Prim_Heap(Graph<Integer> graph,Graph.Node<T> inicio)
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

    public Graph.CostPathPair<Integer> Kruskal(Graph<Integer> graph)
    {
        final List<Graph.Arista<Integer>> camino = new ArrayList<>();
        int cost = 0;

        Collections.sort(graph.getEdges());
        //AQUI YA TENEMOS ORDENADOS DE MENOR A MAYOR LOS PESOS

        for (Graph.Arista a: graph.getEdges()) //Iteramos de menor a mayor
        {
            boolean ciclado=false;

            //Aqui verificamos al agregar a no nos genere un ciclo

            if (!ciclado)
                camino.add(a);
        }
        return (new Graph.CostPathPair<Integer>(cost,camino));
    }

    public Graph.CostPathPair<Integer> Kruskal_UNION(Graph<Integer> graph)
    {
        final List<Graph.Arista<Integer>> camino = new ArrayList<>();
        int cost = 0;

        Collections.sort(graph.getEdges());
        //AQUI YA TENEMOS ORDENADOS DE MENOR A MAYOR LOS PESOS

        DisjointSet<Graph.Node<Integer>> djs = new DisjointSet<>(); //Inicializar el UNION struct
        List<DisjointSet.Item<Graph.Node<Integer>>> visited_disjoint_nodes = new ArrayList<>(); //Aqui meteremos los nodos ya visitados

        //estas son banderas temporales
        DisjointSet.Item<Graph.Node<Integer>> from=null;
        DisjointSet.Item<Graph.Node<Integer>> to=null;

        for (Graph.Arista arista: graph.getEdges()) //Iteramos sobre la lista de aristas ordenada por costo
        {
            int from_index = djs.indexOfNode(visited_disjoint_nodes,arista.getFromNode()); //Indice de nodos origen/destino
            int to_index = djs.indexOfNode(visited_disjoint_nodes,arista.getToNode());

            if (from_index==-1) //Significa que no ha sido visitado o no tenemos registro de el
                from = djs.makeSet(arista.getFromNode());
            else
                from = visited_disjoint_nodes.get(from_index); //Ya existe y pertenece a alguna raiz, obtenerlo
            if (to_index==-1)
                to = djs.makeSet(arista.getToNode()); //Lo mismo que con el from
            else
                to = visited_disjoint_nodes.get(to_index);


            if (djs.find(from)!=djs.find(to)) //SI NO ESTAN UNIDOS POR LA MISMA RAIZ, CONECTARLOS
            {
                djs.union(from, to);        //Unir bosques
                cost += arista.getCost();   //Sumar costo de arista
                camino.add(arista);         //Agregar al arbol final
                if (to_index == -1) visited_disjoint_nodes.add(to);     //Agregar a visitados
                if (from_index == -1) visited_disjoint_nodes.add(from); //Agregar a visitados
            }
        }
        return (new Graph.CostPathPair<Integer>(cost,camino));
    }

}
