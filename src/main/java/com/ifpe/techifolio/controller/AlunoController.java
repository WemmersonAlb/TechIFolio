package com.ifpe.techifolio.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ifpe.techifolio.entities.Aluno;
import com.ifpe.techifolio.repository.AlunoRepository;
import com.ifpe.techifolio.service.PasswordGenerator;
import com.ifpe.techifolio.dto.ErrorResponse;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoRepository repository;

    public AlunoController(AlunoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Object> createAluno(@RequestBody Aluno aluno) {
        String nullFieldMessage = aluno.getNullFieldMessageAluno();
        if (nullFieldMessage != null) {
            ErrorResponse errorResponse = new ErrorResponse("Erro: " + nullFieldMessage, aluno);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        Aluno verificaEmail = repository.findByEmail(aluno.getEmail());
        if (verificaEmail != null) {
            return ResponseEntity.status(409).body(new ErrorResponse("Erro: Já existe um aluno cadastrado com o email informado.", aluno));
        }
        Aluno savedAluno = repository.save(aluno);
        return ResponseEntity.status(201).body(savedAluno);
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
    public ResponseEntity<Object> updateAluno(@PathVariable ObjectId id, @RequestBody Aluno alunoDetails) {
        Optional<Aluno> aluno = repository.findById(id);
        if (aluno.isPresent()) {
            String nullFieldMessage = alunoDetails.getNullFieldMessageAluno();
            if (nullFieldMessage != null) {
                ErrorResponse errorResponse = new ErrorResponse("Erro: " + nullFieldMessage, alunoDetails);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            Aluno updatedAluno = aluno.get();
            updatedAluno.setNome(alunoDetails.getNome());
            updatedAluno.setEmail(alunoDetails.getEmail());
            updatedAluno.setSenha(alunoDetails.getSenha());
            updatedAluno.setFaculdade(alunoDetails.getFaculdade());
            repository.save(updatedAluno);
            return ResponseEntity.ok(updatedAluno);
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

    @PostMapping("/recuperar-senha")//implementar api para enviar a nova senha por email
    public ResponseEntity<Object> recuperarSenha(@RequestParam String email) {
        if(email == null || email.isEmpty()){
            return ResponseEntity.status(400).body(new ErrorResponse("Erro: Email não pode ser nulo ou vazio.", null));
        }
        Aluno aluno = repository.findByEmail(email);
        if (aluno == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Erro: Aluno não encontrado com o email informado.", null));
        }
        String novaSenha = PasswordGenerator.generateRandomPassword();
        aluno.setSenha(novaSenha);
        repository.save(aluno);
        return ResponseEntity.ok(new ErrorResponse("Senha atualizada com sucesso. Nova senha: " + novaSenha, aluno));
    }
}