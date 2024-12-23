package com.ifpe.techifolio.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ifpe.techifolio.entities.Empresario;
import com.ifpe.techifolio.repository.EmpresarioRepository;

@RestController
@RequestMapping("/empresarios")
public class EmpresarioController {
    private final EmpresarioRepository repository;

    public EmpresarioController(EmpresarioRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> createEmpresario(@RequestBody Empresario empresario) {
        if (empresario.nullFieldEmpresario()) {
            return ResponseEntity.badRequest().body("Campos obrigatórios não podem ser nulos");
        }
        repository.save(empresario);
        return ResponseEntity.ok("Empresário criado com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresario> getEmpresarioById(@PathVariable ObjectId id) {
        Optional<Empresario> empresario = repository.findById(id);
        return empresario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Empresario> getAllEmpresarios() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmpresario(@PathVariable ObjectId id, @RequestBody Empresario empresarioDetails) {
        Optional<Empresario> empresario = repository.findById(id);
        if (empresario.isPresent()) {
            if (empresarioDetails.nullFieldEmpresario()) {
                return ResponseEntity.badRequest().body("Campos obrigatórios não podem ser nulos");
            }
            Empresario updatedEmpresario = empresario.get();
            updatedEmpresario.setNome(empresarioDetails.getNome());
            updatedEmpresario.setEmail(empresarioDetails.getEmail());
            updatedEmpresario.setSenha(empresarioDetails.getSenha());
            updatedEmpresario.setEmpresa(empresarioDetails.getEmpresa());
            repository.save(updatedEmpresario);
            return ResponseEntity.ok("Empresário atualizado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresario(@PathVariable ObjectId id) {
        Optional<Empresario> empresario = repository.findById(id);
        if (empresario.isPresent()) {
            repository.delete(empresario.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Empresario> login(@RequestParam String email, @RequestParam String senha) {
        Empresario empresario = repository.findByEmailAndSenha(email, senha);
        if (empresario != null) {
            return ResponseEntity.ok(empresario);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
