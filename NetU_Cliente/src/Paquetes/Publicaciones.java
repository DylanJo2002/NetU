
package Paquetes;

import java.util.List;

public class Publicaciones extends Paquete {
    
   private List<Publicacion> publicaciones;
   
   public void asignarPublicaciones(List<Publicacion> publicaciones){
       this.publicaciones = publicaciones;
   }
   
   public List<Publicacion> obtenerPublicaciones(){
       return publicaciones;
   }

}
