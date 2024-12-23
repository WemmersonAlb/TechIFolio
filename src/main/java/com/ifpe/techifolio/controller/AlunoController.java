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

import com.ifpe.techifolio.entities.Aluno;
import com.ifpe.techifolio.repository.AlunoRepository;



@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoRepository repository;

    public AlunoController(AlunoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> createAluno(@RequestBody Aluno aluno) {
        if (aluno.nullFieldAluno()) {
            return ResponseEntity.badRequest().body("Campos obrigatórios não podem ser nulos");
        }
        repository.save(aluno);
        return ResponseEntity.ok("Aluno criado com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getAlunoById(@PathVariable ObjectId id) {
        Optional<Aluno> aluno = repository.findById(id);
        return aluno.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Aluno> getAllAlunos() {
        return repository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAluno(@PathVariable ObjectId id, @RequestBody Aluno alunoDetails) {
        Optional<Aluno> aluno = repository.findById(id);
        if (aluno.isPresent()) {
            if (alunoDetails.nullFieldAluno()) {
                return ResponseEntity.badRequest().body("Campos obrigatórios não podem ser nulos");
            }
            Aluno updatedAluno = aluno.get();
            updatedAluno.setNome(alunoDetails.getNome());
            updatedAluno.setEmail(alunoDetails.getEmail());
            updatedAluno.setSenha(alunoDetails.getSenha());
            updatedAluno.setFaculdade(alunoDetails.getFaculdade());
            repository.save(updatedAluno);
            return ResponseEntity.ok("Aluno atualizado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable ObjectId id) {
        Optional<Aluno> aluno = repository.findById(id);
        if (aluno.isPresent()) {
            repository.delete(aluno.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Aluno> login(@RequestParam String email, @RequestParam String senha) {
        Aluno aluno = repository.findByEmailAndSenha(email, senha);
        if (aluno != null) {
            return ResponseEntity.ok(aluno);
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
