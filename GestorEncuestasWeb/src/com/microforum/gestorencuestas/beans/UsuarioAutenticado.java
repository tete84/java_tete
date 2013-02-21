package com.microforum.gestorencuestas.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorencuestas.entities.DatosLogin;
import com.microforum.gestorencuestas.entities.Usuario;

@ManagedBean(name="userBean")  //Cambiamo el nombre del bean
@SessionScoped	//Se construye y se asocia al ambito de la sesion
public class UsuarioAutenticado {
	private String loginName="LoginName";
	private Usuario user;
	
	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public UsuarioAutenticado(){
		FacesContext fc=FacesContext.getCurrentInstance();
		ExternalContext ext=fc.getExternalContext();		//Contexto Externo
		String loginUserName=ext.getRemoteUser();		//Nombre de usuario para autenticarse
		if(loginUserName!=null){
			Configuration conf=new Configuration();
			SessionFactory sf=conf.configure().buildSessionFactory();
			Session sesion=sf.openSession();
			Transaction trans=sesion.beginTransaction();
			
			DatosLogin dl=(DatosLogin) sesion.get(DatosLogin.class, loginUserName);
			Usuario user=dl.getUsuario();
			this.user=user;	//LIGAR USUARIO
			trans.commit();
			sesion.close();
			
			/*//SE PUEDE SUSTITUIR CREANDO UN ATRIBUTO USER Y LIGARLO 
			HttpServletRequest request=(HttpServletRequest) ext.getRequest();	//Contexto Servlet
			HttpSession session=request.getSession(); //Obtenemos la sesion
			session.setAttribute("usuario",user);	//Damos valor al atributo sesion*/
		}
	}
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}
