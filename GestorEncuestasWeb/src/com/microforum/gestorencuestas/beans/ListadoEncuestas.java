package com.microforum.gestorencuestas.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorencuestas.entities.Encuesta;
import com.microforum.gestorencuestas.entities.Pregunta;

@ManagedBean
@SessionScoped
public class ListadoEncuestas {
	private Map<String,String> mapaEncuestas;
	private List<String> arrayEncuestas;	 
	//private int tipo; 	//Tipo de encuesta ??
	private List<SelectItem> encuestaItems;
	
	
	public Map<String, String> getMapaEncuestas() {
		return mapaEncuestas;
	}

	public void setMapaEncuestas(Map<String, String> mapaEncuestas) {
		this.mapaEncuestas = mapaEncuestas;
	}

	public List<String> getArrayEncuestas() {
		return arrayEncuestas;
	}

	public void setArrayEncuestas(List<String> arrayEncuestas) {
		this.arrayEncuestas = arrayEncuestas;
	}

	public List<SelectItem> getEncuestaItems() {
		return encuestaItems;
	}

	public void setEncuestaItems(List<SelectItem> encuestaItems) {
		this.encuestaItems = encuestaItems;
	}

	public void getListadoEncuestas(){
		Configuration conf=new Configuration();
		SessionFactory sf=conf.configure().buildSessionFactory();
		Session session=sf.openSession();
		Query query=session.createQuery("from Encuesta");
		List<Encuesta> listadoEncuestas=query.list();
		
		int tam=listadoEncuestas.size();  //Tamaño
		encuestaItems=new ArrayList<SelectItem>();
		arrayEncuestas=new ArrayList();
		
		encuestaItems.add(new SelectItem("----Encuestas----"));
		
		for(int i=0;i<listadoEncuestas.size();i++){
			arrayEncuestas.add(listadoEncuestas.get(i).getProposito());
			String ref=listadoEncuestas.get(i).getRef();
			String proposito=listadoEncuestas.get(i).getProposito();
			encuestaItems.add(new SelectItem(ref,proposito));
		}
		System.out.println(encuestaItems);
		session.close();
	}
	
	public void addEncuesta(ActionEvent e){
		Configuration conf=new Configuration();
		SessionFactory sf=conf.configure().buildSessionFactory();
		Session session=sf.openSession();
		Transaction tr=session.beginTransaction();
		Encuesta en=new Encuesta();
		en.setAutor(en.getAutor());
		en.setProposito(en.getProposito());
		en.setRef(en.getRef());
		
		session.save(en);
		tr.commit();
		session.close();
		SelectItem item=new SelectItem(en.getRef(),en.getProposito());
		encuestaItems.add(item);
	}
}
