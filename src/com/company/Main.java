package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Main
{
    private static final String path="src/com/company/test.txt";
    private static final Integer primStartNode = 0;

    public static void main(String[] args)
    {
        //LEER ARCHIVO
        List<Graph.Node> nodes = new ArrayList<>();
        List<Graph.Arista> aristas = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(path)))
        {
            int c=0;
            while (scanner.hasNext())
            {
                if (c++ == 0) //Primer linea
                {
                    scanner.nextLine();
                }
                else
                {
                    String[] temp = scanner.nextLine().split(" ");

                    int first_val =  Integer.parseInt(temp[0]);
                    int second_val =  Integer.parseInt(temp[1]);
                    int cost = Integer.parseInt(temp[2]);

                    int from_index = indexOfNodeWithValue(nodes,first_val);
                    int to_index = indexOfNodeWithValue(nodes,second_val);

                    Graph.Node from_node,to_node;

                    if (from_index>-1) //Ya existen
                    {
                        from_node = nodes.get(from_index);
                    }
                    else
                    {
                        from_node = new Graph.Node(first_val);
                        nodes.add(from_node);
                    }
                    if (to_index>-1)
                    {
                        to_node = nodes.get(to_index);
                    }
                    else
                    {
                        to_node = new Graph.Node(second_val);
                        nodes.add(to_node);
                    }
                    //Ya tenemos los dos nodos, solo hay que unirlos
                    aristas.add(new Graph.Arista(cost,from_node,to_node));
                }
            }
        } catch (IOException e) {e.printStackTrace();}

        //AQUI EMPIEZA
        Graph<Integer> g = new Graph<Integer>(Graph.TYPE.UNDIRECTED,(Collection) nodes, (Collection) aristas);
        System.out.println("Total de Nodos: "+g.getNodes().size());
        System.out.println("Total de Aristas NO dirigidas: "+g.getEdges().size()/2); //Por que como es no dirigido se duplican aristas

        Algoritmos<Integer> algoritmos = new Algoritmos<Integer>();
        Graph.CostPathPair<Integer> prim = algoritmos.Prim(g, g.getNodes().get(primStartNode));
        Graph.CostPathPair<Integer> krus_union = algoritmos.Kruskal_UNION(g);

        System.out.println(prim);
        System.out.println(krus_union);

        //for (Graph.Arista<Integer> a:prim.getPath()) if (!krus.getPath().contains(a)) System.out.print(a);
    }

    public static int indexOfNodeWithValue(List<Graph.Node> nodes,int val)
    {
        for (int i = 0; i<nodes.size();++i)
        {
            if (nodes.get(i).getValue().equals(val) )
            {
                return i;
            }
        }
        return -1;
    }
}
