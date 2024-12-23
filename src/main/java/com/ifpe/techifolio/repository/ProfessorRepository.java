package com.ifpe.techifolio.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ifpe.techifolio.entities.Professor;



public interface ProfessorRepository extends MongoRepository<Professor, ObjectId> {
    Professor findByEmail(String email);
    Professor findByEmailAndSenha(String email, String senha);
}
