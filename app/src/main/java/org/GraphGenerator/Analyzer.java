package org.GraphGenerator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Analyzer {
    public static void BFS(ArrayList<Line> lines) {
        HashMap<Vertex, HashSet<Vertex>> adjList = new HashMap<>();

        for(Line l : lines) {
            if(!adjList.containsKey(l.startingPoint)) adjList.put(l.startingPoint, 
                new HashSet<>());
            if(!adjList.containsKey(l.endingPoint)) adjList.put(l.endingPoint, 
                new HashSet<>());

            adjList.get(l.startingPoint).add(l.endingPoint);
            adjList.get(l.endingPoint).add(l.startingPoint);
        }

        // BFS here
        Set<Vertex> verts = adjList.keySet();
        ArrayDeque<Vertex> q = new ArrayDeque<>();

        verts.forEach((v) -> {
            v.visited = false;
            q.add(v);
        });

        while(!q.isEmpty()) {
            Vertex v = q.removeFirst();
            v.visited = true;

            HashSet<Vertex> set = adjList.get(v);
            for(Vertex n : set) {
                q.add(n);
            }
        }
    } 
}
