package com.ejemplo.agenda.controladores;

import com.ejemplo.agenda.entidades.Contacto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/contactos")
public class ContactoController {
    
    private final List<Contacto> contactos = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public ContactoController() {
        // Algunos contactos de ejemplo
        contactos.add(new Contacto(counter.incrementAndGet(), "Juan Pérez", "123456789", "juan@example.com"));
        contactos.add(new Contacto(counter.incrementAndGet(), "María García", "987654321", "maria@example.com"));
    }

    @GetMapping
    public List<Contacto> listarContactos() {
        return contactos;
    }

    @GetMapping("/{id}")
    public Contacto obtenerContacto(@PathVariable Long id) {
        return contactos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Contacto crearContacto(@RequestBody Contacto contacto) {
        contacto.setId(counter.incrementAndGet());
        contactos.add(contacto);
        return contacto;
    }

    @PutMapping("/{id}")
    public Contacto actualizarContacto(@PathVariable Long id, @RequestBody Contacto contactoActualizado) {
        for (int i = 0; i < contactos.size(); i++) {
            if (contactos.get(i).getId().equals(id)) {
                contactoActualizado.setId(id);
                contactos.set(i, contactoActualizado);
                return contactoActualizado;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminarContacto(@PathVariable Long id) {
        contactos.removeIf(c -> c.getId().equals(id));
    }
}
