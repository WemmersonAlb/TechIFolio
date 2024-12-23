package com.ifpe.techifolio.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ifpe.techifolio.entities.Aluno;



public interface AlunoRepository extends MongoRepository<Aluno, ObjectId> {
    Aluno findByEmail(String email);
    Aluno findByEmailAndSenha(String email, String senha);
}
