package com.ifpe.techifolio.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ifpe.techifolio.entities.Empresario;



public interface EmpresarioRepository extends MongoRepository<Empresario, ObjectId> {
    Empresario findByEmail(String email);
    Empresario findByEmailAndSenha(String email, String senha);
}
