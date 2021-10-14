/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Paquetes;
import Vista.ElementoBandeja;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author dilan
 */
public class Bandeja extends Paquete {
    List<ElementoBandeja> elementos;
    
    public Bandeja(){
        elementos = new ArrayList<ElementoBandeja>();
    }

    public List<ElementoBandeja> getElementos() {
        return elementos;
    }

    public void agregarElemento(ElementoBandeja elemento) {
        elementos.add(elemento);
    }    
}
