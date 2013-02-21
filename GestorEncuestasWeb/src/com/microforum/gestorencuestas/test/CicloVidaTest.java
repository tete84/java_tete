package com.microforum.gestorencuestas.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorencuestas.entities.DocumentoId;
import com.microforum.gestorencuestas.entities.Usuario;



public class CicloVidaTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration conf=new Configuration();
		SessionFactory sf=conf.configure().buildSessionFactory();
		Session sesion=sf.openSession();
		Transaction trans=sesion.beginTransaction();
		
		DocumentoId doc=new DocumentoId();
		doc.setNumeroId("222222");		//Id
		doc.setTipoDocumento(1);
		
		Usuario user=(Usuario) sesion.get(Usuario.class, doc);
		user.setApellido1("Gutierrez");	//Update de campo apellido1 con referencia al Id
		System.out.println(user.getNombre());
		
		
		trans.commit();
		sesion.close();

	}

}
