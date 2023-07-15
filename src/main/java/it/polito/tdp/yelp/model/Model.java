package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	YelpDao dao = new YelpDao();
	Graph<User,DefaultWeightedEdge> grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	
	
	public void creaGrafo(int n, int anno) {
		
		List<User> vertici = dao.getVertex(n);
		
		Graphs.addAllVertices(grafo, vertici);
		
		Map<String,Integer> mappaArchi = mappaUserPeso(anno);
		
		for(User u1 : grafo.vertexSet()) {
			for(User u2 : grafo.vertexSet()) {
				
				if(mappaArchi.containsKey(u1.getUserId()+u2.getUserId())) {
					
					int peso = mappaArchi.get(u1.getUserId()+u2.getUserId());
					
					Graphs.addEdge(grafo, u1, u2, peso);
				}
			}
		}
		
	}
	
	
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	
	public Map<String,Integer> mappaUserPeso(int anno){
		
		Map<String,Integer> mappaArchi = new HashMap<>();
		
		for(User u1 : grafo.vertexSet()) {
			for(User u2 : grafo.vertexSet()) {
				
            if( u1.getUserId().compareTo(u2.getUserId())!=0 ) {
					
					if( dao.getEdges(u1.getUserId(), u2.getUserId(), anno)!=0) {
						
						int peso = dao.getEdges(u1.getUserId(), u2.getUserId(), anno);
						mappaArchi.put(u1.getUserId()+u2.getUserId(), peso);
					}
				}
			}
		}
		return mappaArchi;
	}
	
	public Set<User> vertici(){
		return grafo.vertexSet();
	}
	
	public Map<User,Integer> listaUtentiSimili(User u){
		
		Map<User,Integer> mappaUtentiSimili = new HashMap<>();
		int max = 0;
		
		for(DefaultWeightedEdge e : grafo.edgesOf(u)) {
			if(grafo.getEdgeWeight(e)>max) {
				max = (int) grafo.getEdgeWeight(e);
			}
		}
		
		for(DefaultWeightedEdge e : grafo.edgesOf(u)) {
			if(grafo.getEdgeWeight(e)==max) {
				User u2 = Graphs.getOppositeVertex(grafo, e, u);
				mappaUtentiSimili.put(u2, max);
			}
		}
		return mappaUtentiSimili;
	}
	
	
}
