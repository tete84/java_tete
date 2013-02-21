package com.microforum.gestorencuestas.beans;

import java.io.File;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.microforum.gestorencuestas.entities.Administrador;
import com.microforum.gestorencuestas.entities.Encuesta;
import com.microforum.gestorencuestas.entities.Pregunta;
import com.microforum.gestorencuestas.entities.Usuario;

/**
 * @author alumno
 *
 */
@ManagedBean
@SessionScoped
public class EncuestaSesion {
	private List<String> preguntasEncuesta=new ArrayList<String>();
	private List<RegistroEncuesta> registrosEncuesta=
			new ArrayList<RegistroEncuesta>();
	private String nuevaPregunta="";
	private String visibilidadDetalle="display: none;"; 
	private String proposito;
	private String nombre;
	private int valoracion;
	
	
	private List<String> getRefList(List<RegistroEncuesta> regList){
		List<String> refList=new ArrayList<String>();
		for(RegistroEncuesta  re:regList){
			refList.add(re.getRef());
		}
		return refList;
	}
	
	//Metodo Navegacion REGISTRAR/INSERTAR ENCUESTA
	public String registrarEncuesta(){
				//agregar control de navegacion
				//Obtenemos la Entidad
				FacesContext fContext=FacesContext.getCurrentInstance();
				ExternalContext ec=fContext.getExternalContext();   //Contexto Externo
				HttpServletRequest request= (HttpServletRequest) ec.getRequest();
				HttpSession sesion=request.getSession(); 
				Object obj=sesion.getAttribute("userBean");
				if(obj==null){
					sesion.invalidate();
					return "index.jsp";
				}else{
					UsuarioAutenticado authUser= (UsuarioAutenticado)obj; //Obtenemos el atributo del Bean UsuarioAutenticado
					Administrador user=(Administrador) authUser.getUser();  //Obtenemos la Entidad Usuario Administrador
					Configuration conf=new Configuration();
					SessionFactory sf=conf.configure().buildSessionFactory();
					Session hSession=sf.openSession();
					Transaction tr=hSession.beginTransaction();
					
					//HQL
					Query query=hSession.createQuery("from Pregunta where ref in (:preguntasRef)");  //Parametro HQL
					List<String> refList=getRefList(registrosEncuesta);
					query.setParameterList("preguntasRef", refList);  //Parametro que queremos sustituir , Collection
					List<Pregunta> preguntas=query.list();
					hSession.merge(user);  //Le hacemos merge para quede vinculado, para poder sincronizar Hibernate
					//Creamos Encuesta para añadirle sus atributos creados
					Encuesta encuesta=new Encuesta();
					encuesta.setAutor(user);
					encuesta.setProposito(proposito);
					for(Pregunta p:preguntas){
						encuesta.getPreguntas().add(p);
					}
					hSession.save(encuesta);  
					tr.commit();
					hSession.close();
					encuesta.setPreguntas(preguntas);
					return "/encuestas/administracion/administracionIndex";
				}
	}
	
	public void validarNombre(FacesContext context, UIComponent componentToValidate, Object value) throws ValidatorException{
		String nombre=(String) value;
		if(!nombre.startsWith("Encuesta")){
			FacesMessage msg=new FacesMessage("El nombre debe empezar por Encuesta");
			 throw new ValidatorException(msg);
		}
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if(!nombre.startsWith("Encuesta")){
			FacesMessage fc=new FacesMessage("El nombre debe ser Encuesta<X>");
			FacesContext ctx=FacesContext.getCurrentInstance();  //Obtenemos el contexto
			ctx.addMessage("nombreE", fc);  //Mensaje asociado al contexto del input text del mensaje 
		}else{
			this.nombre = nombre;
		}
	}

	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		if(valoracion<0 || valoracion>10){
			FacesMessage fc=new FacesMessage("La valoracion debe estar entre 0 y 10");
			FacesContext ctx=FacesContext.getCurrentInstance();  //Obtenemos el contexto
			ctx.addMessage("importanciaE", fc); //Mensaje asociado al contexto del input text del mensaje 
		}
		this.valoracion = valoracion;
	}

	//Dependiedo del valor del proposito nos movemos o no
	public String getProposito() {
		return proposito;
	}

	public void setProposito(String proposito) {
		this.proposito = proposito;
	}
	//INICIAR ENCUESTA
	public String iniciarEncuesta(){
		/*FacesMessage fc=new FacesMessage("ERROR");
		FacesContext ctx=FacesContext.getCurrentInstance();  //Obtenemos el contexto
		ctx.addMessage(null, fc);  //Mensaje asociado al contexto
		return null;		//continuamos en la misma pagina
		*/
		return "DetalleEncuesta";
		
	}
	
	public void eliminarPregunta(ActionEvent e){
		UIComponent component=e.getComponent();
		component= component.findComponent("preguntaRef");
		if(component!=null){
			if(component instanceof UIParameter){
				UIParameter parameter=(UIParameter)component;
				RegistroEncuesta reg=(RegistroEncuesta) parameter.getValue();
				registrosEncuesta.remove(reg);
				if(registrosEncuesta.isEmpty())
					visibilidadDetalle="display: none;";
			}
		}
	}
	//TERMINAR ENCUESTA
	public String terminarEncuesta(){
		
		return "resumenEncuesta";
	}
	
	public String getVisibilidadDetalle() {
		return visibilidadDetalle;
	}

	public void setVisibilidadDetalle(String visibilidadDetalle) {
		this.visibilidadDetalle = visibilidadDetalle;
	}

	public List<RegistroEncuesta> getRegistrosEncuesta() {
		return registrosEncuesta;
	}

	public String getNuevaPregunta() {
		return nuevaPregunta;
	}

	public void setNuevaPregunta(String nuevaPregunta) {
		this.nuevaPregunta = nuevaPregunta;
	}

	public List<String> getPreguntasEncuesta() {
		return preguntasEncuesta;
	}

	public void setPreguntasEncuesta(List<String> preguntasEncuesta) {
		this.preguntasEncuesta = preguntasEncuesta;
	}
	
	public void addPregunta(ActionEvent e){
		System.out.println("ActionEvent");
	}
	
	public void selectPregunta(ValueChangeEvent e){
		System.out.println(e.getNewValue());
		System.out.println("ValueChangeEvent");
		String ref=(String) e.getNewValue();
		Configuration conf=new Configuration();
		SessionFactory sf=conf.configure().buildSessionFactory();
		Session session=sf.openSession();
		Pregunta p=(Pregunta) session.get(Pregunta.class,ref);
		if(p!=null){
			visibilidadDetalle="display:inline-block;";
			RegistroEncuesta re=new RegistroEncuesta();
			re.setRef(p.getRef());
			re.setTexto(p.getTexto());
			re.setTipo(re.getTipo());
			registrosEncuesta.add(re);
		}
		session.close();
		//preguntasEncuesta.add((String) e.getNewValue());
	}
}
